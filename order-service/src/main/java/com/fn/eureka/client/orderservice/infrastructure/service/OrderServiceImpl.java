package com.fn.eureka.client.orderservice.infrastructure.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.exception.CustomApiException;
import com.fn.common.global.util.PageUtils;
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
		// UUID supplyCompanyId = orderRequestDto.getOrderSupplyCompanyId();	// 공급업체 ID
		// CompanyInfoDto supplyCompanyInfo = companyServiceClient.getCompany(supplyCompanyId);
		// // 공급업체의 허브ID 검색 후 허브 재고 조회
		// UUID supplyCompanyHubId = supplyCompanyInfo.getCompanyHubId();	// 공급업체 담당 허브 ID
		// UUID orderProductId = orderRequestDto.getOrderProductId();	// 주문상품 ID
		// HubStockResponseDto hubStockInfo = hubServiceClient.searchHubStock(supplyCompanyHubId, orderProductId);
		// // 재고 부족 예외 처리
		// if (hubStockInfo.getHsQuantity() < orderRequestDto.getOrderProductQuantity()) {
		// 	throw new CustomApiException(OrderException.HUB_INSUFFICIENT_STOCK);
		// }

		// 주문 생성
		Order order = orderRepository.save(new Order(orderRequestDto));
		// // 배송 생성 요청
		// // 주문자(수령업체) 업체 조회
		// UUID receiveCompanyId = orderRequestDto.getOrderReceiveCompanyId();	// 수령업체 ID
		// CompanyInfoDto receiveCompanyInfo = companyServiceClient.getCompany(receiveCompanyId);
		// // 주문자(수령업체) 업체담당자 유저 정보 조회
		// UserResponseDto receiveCompanyManagerInfo = userServiceClient.getUser(receiveCompanyInfo.getCompanyManagerId());
		// // 배송 생성
		// DeliveryRequestDto deliveryRequestDto = DeliveryRequestDto.builder()
		// 	.orderId(order.getOrderId())
		// 	.supplyCompanyHubId(supplyCompanyHubId)
		// 	.receiveCompanyHubId(receiveCompanyId)
		// 	.receiveCompanyAddress(receiveCompanyInfo.getCompanyAddress())
		// 	.deliveryReceiverCompanyManagerName(receiveCompanyManagerInfo.getUserNickname())
		// 	.receiverSlackId(receiveCompanyManagerInfo.getUserSlackId())
		// 	.build();
		// DeliveryResponseDto deliveryInfo = deliveryServiceClient.createdDelivery(deliveryRequestDto);
		//
		// // 생성된 배송ID 받아 저장
		// order.saveOrderDeliveryId(deliveryInfo.getDeliveryId());
		return new OrderResponseDto(order);
	}

	// 주문 조회
	@Override
	public OrderResponseDto findOrder(UUID orderId) {
		Order order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId)
			.orElseThrow(() -> new CustomApiException(OrderException.ORDER_NOT_FOUND));
		return new OrderResponseDto(order);
	}

	// 주문 리스트 조회
	@Override
	public Page<OrderResponseDto> findAllOrdersByRole(String keyword, int page, int size, Sort.Direction sortDirection,
		PageUtils.CommonSortBy sortBy, String userRole, UUID userId) {
		// List<UUID> companies = null;
		// List<UUID> deliveries = null;
		UUID companyId = null;
		switch (userRole) {
			case "MASTER" :
				break;
			case "HUB_MANAGER" :
				// UUID hubId = hubServiceClient.readHubIdByHubManagerId(userId);
				// // 허브에 소속된 업체ID 목록
				// companies = companyServiceClient.readCompaniesByHubId(hubId);
				// // orderSupplyCompanyId, orderReceiveCompanyId 중에 해당되는 주문 리스트 조회
				break;
			case "DELIVERY_MANAGER" :
				// // 로그인 유저(배송담당자)가 담당하는 배송ID 리스트 받기
				// deliveries = deliveryServiceClient.readeDeliveriesByDeliveryManagerId(userId);
				break;
			case "COMPANY_MANAGER" :
				// orderSupplyCompanyId, orderReceiveCompanyId 중에 해당되는 주문 리스트 조회
				companyId = companyServiceClient.readCompanyIdByCompanyManagerId(userId);
				break;
			default: throw new CustomApiException(OrderException.ORDER_NOT_FOUND);
		}
		Page<OrderResponseDto> orders = orderQueryRepository.findAllOrdersByRole(keyword, PageUtils.pageable(page, size), userRole, userId, companyId, sortDirection, sortBy);
		return orders;
	}

	// 주문 수정
	@Override
	@Transactional
	public OrderResponseDto modifyOrder(UUID orderId, Map<String, Object> updates, String userRole, UUID userId) {
		Order order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId)
			.orElseThrow(() -> new CustomApiException(OrderException.ORDER_NOT_FOUND));
		switch(userRole) {
			case "MASTER" : break;
			case "HUB_MANAGER" :
				// UUID hubId = hubServiceClient.readHubIdByHubManagerId(userId);
				// UUID supplyCompanyHubId = companyServiceClient.readHubIdByCompanyId(order.getOrderSupplyCompanyId());
				// UUID receiveCompanyHubId = companyServiceClient.readHubIdByCompanyId(order.getOrderReceiveCompanyId());
				// if (!hubId.equals(supplyCompanyHubId) && !hubId.equals(receiveCompanyHubId)) {
				// 	throw new CustomApiException(OrderException.ORDER_UNAUTHORIZED);
				// }
				break;
			default: throw new CustomApiException(OrderException.ORDER_UNAUTHORIZED);
		}
		updates.forEach((key, value) -> order.modifyOrderInfo(key, value));
		return new OrderResponseDto(order);
	}
}
