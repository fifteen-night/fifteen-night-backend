package com.fn.eureka.client.orderservice.infrastructure.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.exception.CustomApiException;
import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.orderservice.application.dto.CompanyInfoDto;
import com.fn.eureka.client.orderservice.application.dto.DeliveryRequestDto;
import com.fn.eureka.client.orderservice.application.dto.DeliveryResponseDto;
import com.fn.eureka.client.orderservice.application.dto.HubStockResponseDto;
import com.fn.eureka.client.orderservice.application.dto.UserResponseDto;
import com.fn.eureka.client.orderservice.domain.model.Order;
import com.fn.eureka.client.orderservice.domain.service.OrderService;
import com.fn.eureka.client.orderservice.infrastructure.repository.OrderQueryRepositoryImpl;
import com.fn.eureka.client.orderservice.domain.repository.OrderRepository;
import com.fn.eureka.client.orderservice.infrastructure.exception.OrderException;
import com.fn.eureka.client.orderservice.infrastructure.client.CompanyServiceClient;
import com.fn.eureka.client.orderservice.infrastructure.client.DeliveryServiceClient;
import com.fn.eureka.client.orderservice.infrastructure.client.HubServiceClient;
import com.fn.eureka.client.orderservice.infrastructure.client.UserServiceClient;
import com.fn.eureka.client.orderservice.presentation.request.OrderRequestDto;
import com.fn.eureka.client.orderservice.application.dto.OrderResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderQueryRepositoryImpl orderQueryRepository;

	private final UserServiceClient userServiceClient;
	private final CompanyServiceClient companyServiceClient;
	private final HubServiceClient hubServiceClient;
	private final DeliveryServiceClient deliveryServiceClient;

	// TODO CustomApiException으로 바꾸기...

	// 주문 생성
	@Override
	@Transactional
	public OrderResponseDto addOrder(OrderRequestDto orderRequestDto) {
		// 상품 재고 조회
		// 공급업체 ID
		UUID supplyCompanyId = orderRequestDto.getOrderSupplyCompanyId();
		// 공급업체의 소속 허브ID
		UUID supplyCompanyHubId = companyServiceClient.readHubIdByCompanyId(supplyCompanyId);
		// 주문상품 ID
		UUID orderProductId = orderRequestDto.getOrderProductId();
		// 허브 재고 조회
		HubStockResponseDto hubStockInfo = hubServiceClient.readHubStock(supplyCompanyHubId, orderProductId);
		// 재고 부족 예외 처리
		if (hubStockInfo.getHsQuantity() < orderRequestDto.getOrderProductQuantity()) {
			throw new CustomApiException(OrderException.HUB_INSUFFICIENT_STOCK);
		}

		// 주문 생성
		Order order = orderRepository.save(Order.from(orderRequestDto));

		// 배송 생성 요청
		// 주문자(수령업체) 업체 조회
		UUID receiveCompanyId = orderRequestDto.getOrderReceiveCompanyId();	// 수령업체 ID
		CompanyInfoDto receiveCompanyInfo = companyServiceClient.getCompany(receiveCompanyId);
		// 주문자(수령업체) 업체담당자 유저 정보 조회
		UserResponseDto receiveCompanyManagerInfo = userServiceClient.getUser(receiveCompanyInfo.getCompanyManagerId());
		// 배송 생성
		DeliveryRequestDto deliveryRequestDto = DeliveryRequestDto.builder()
			.orderId(order.getOrderId())
			.supplyCompanyHubId(supplyCompanyHubId)
			.receiveCompanyHubId(receiveCompanyInfo.getCompanyHubId())
			.receiveCompanyAddress(receiveCompanyInfo.getCompanyAddress())
			.deliveryReceiverCompanyManagerName(receiveCompanyManagerInfo.getUserNickname())
			.receiverSlackId(receiveCompanyManagerInfo.getUserSlackId())
			.build();
		DeliveryResponseDto deliveryInfo = deliveryServiceClient.createdDelivery(deliveryRequestDto);
		// 생성된 배송ID 받아 저장
		order.saveOrderDeliveryId(deliveryInfo.getDeliveryId());

		return OrderResponseDto.from(order);
	}

	// 주문 조회
	@Override
	public OrderResponseDto findOrder(UUID orderId) {
		Order order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId)
			.orElseThrow(() -> new CustomApiException(OrderException.ORDER_NOT_FOUND));
		return OrderResponseDto.from(order);
	}

	// 주문 리스트 조회
	@Override
	public Page<OrderResponseDto> findAllOrdersByRole(String keyword, int page, int size, Sort.Direction sortDirection,
		PageUtils.CommonSortBy sortBy, String userRole, UUID userId) {
		List<UUID> companies = null;
		// List<UUID> deliveries = null;
		UUID companyId = null;
		switch (userRole) {
			case "MASTER" :
				break;
			case "HUB_MANAGER" :
				// 로그인 유저가 허브관리자인 경우, 유저ID(허브관리자ID)로 허브ID 조회
				UUID hubId = hubServiceClient.readHubIdByHubManagerId(userId);
				// 허브에 소속된 업체ID 목록 - 공급업체ID/수령업체ID 중에 해당되는 주문 리스트 조회
				companies = companyServiceClient.readCompaniesByHubId(hubId);
				break;
			case "DELIVERY_MANAGER" :
				// // 로그인 유저(배송담당자)가 담당하는 배송ID 리스트 받기
				// deliveries = deliveryServiceClient.readDeliveriesByDeliveryManagerId(userId);
				break;
			case "COMPANY_MANAGER" :
				// 공급업체ID/수령업체ID 중에 해당되는 주문 리스트 조회
				companyId = companyServiceClient.readCompanyIdByCompanyManagerId(userId);
				break;
			default: throw new CustomApiException(OrderException.ORDER_NOT_FOUND);
		}
		Page<OrderResponseDto> orders = orderQueryRepository.findAllOrdersByRole(keyword, PageUtils.pageable(page, size), userRole, userId, companyId, companies, sortDirection, sortBy);
		return orders;
	}

	// 주문 수정
	@Override
	@Transactional
	public OrderResponseDto modifyOrder(UUID orderId, Map<String, Object> updates, String userRole, UUID userId) {
		Order order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId)
			.orElseThrow(() -> new CustomApiException(OrderException.ORDER_NOT_FOUND));
		validateUserPermission(order, userRole, userId);
		updates.forEach((key, value) -> order.modifyOrderInfo(key, value));
		return OrderResponseDto.from(order);
	}

	// 주문 삭제
	@Override
	@Transactional
	public void removeOrder(UUID orderId, String userRole, UUID userId) {
		Order order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId)
			.orElseThrow(() -> new CustomApiException(OrderException.ORDER_NOT_FOUND));
		validateUserPermission(order, userRole, userId);
		order.markAsDeleted();
	}

	// 주문 수정 삭제는 마스터, 허브 관리자(담당 허브일 경우)만 가능
	private void validateUserPermission(Order order, String userRole, UUID userId) {
		if ("MASTER".equals(userRole)) {
			return;
		}
		if ("HUB_MANAGER".equals(userRole)) {
			// 로그인 유저가 허브관리자인 경우, 유저ID(허브관리자ID)로 허브ID 조회
			UUID hubId = hubServiceClient.readHubIdByHubManagerId(userId);
			// 업체ID로 업체 소속 허브ID 조회
			// 공급업체 허브ID
			UUID supplyCompanyHubId = companyServiceClient.readHubIdByCompanyId(order.getOrderSupplyCompanyId());
			// 수령업체 허브 ID
			UUID receiveCompanyHubId = companyServiceClient.readHubIdByCompanyId(order.getOrderReceiveCompanyId());
			// 공급업체/수령업체의 허브가 아닌 경우 권한 없음
			if (!hubId.equals(supplyCompanyHubId) && !hubId.equals(receiveCompanyHubId)) {
				throw new CustomApiException(OrderException.ORDER_UNAUTHORIZED);
			}
			return;
		}
		throw new CustomApiException(OrderException.ORDER_UNAUTHORIZED);
	}

	// for other services...

	@Override
	public List<UUID> findOrderProductIdListByDeliveryId(List<UUID> deliveries) {
		return orderQueryRepository.findOrderProductIdListByDeliveryId(deliveries);
	}

}
