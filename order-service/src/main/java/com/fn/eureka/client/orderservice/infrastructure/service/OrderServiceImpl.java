package com.fn.eureka.client.orderservice.infrastructure.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.fn.common.global.exception.CustomApiException;
import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.orderservice.application.dto.CompanyInfoDto;
import com.fn.eureka.client.orderservice.application.dto.DeliveriesUUIDDto;
import com.fn.eureka.client.orderservice.application.dto.DeliveryRequestDto;
import com.fn.eureka.client.orderservice.application.dto.DeliveryResponseDto;
import com.fn.eureka.client.orderservice.application.dto.GeminiResponseDto;
import com.fn.eureka.client.orderservice.application.dto.HubStockResponseDto;
import com.fn.eureka.client.orderservice.application.dto.HubStockUpdateDto;
import com.fn.eureka.client.orderservice.application.dto.OrderResponseDto;
import com.fn.eureka.client.orderservice.application.dto.UserResponseDto;
import com.fn.eureka.client.orderservice.domain.model.Order;
import com.fn.eureka.client.orderservice.domain.repository.OrderRepository;
import com.fn.eureka.client.orderservice.domain.service.OrderService;
import com.fn.eureka.client.orderservice.infrastructure.client.CompanyServiceClient;
import com.fn.eureka.client.orderservice.infrastructure.client.DeliveryServiceClient;
import com.fn.eureka.client.orderservice.infrastructure.client.HubServiceClient;
import com.fn.eureka.client.orderservice.infrastructure.client.SlackServiceClient;
import com.fn.eureka.client.orderservice.infrastructure.client.UserServiceClient;
import com.fn.eureka.client.orderservice.infrastructure.exception.OrderException;
import com.fn.eureka.client.orderservice.infrastructure.repository.OrderQueryRepositoryImpl;
import com.fn.eureka.client.orderservice.presentation.request.OrderRequestDto;

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
	private final SlackServiceClient slackServiceClient;

	// 주문 생성
	@Override
	@Transactional
	public OrderResponseDto addOrder(OrderRequestDto orderRequestDto) {
		// 상품 재고 조회
		// 공급업체 ID
		UUID supplyCompanyId = orderRequestDto.getOrderSupplyCompanyId();
		// 공급업체의 소속 허브ID
		UUID supplyCompanyHubId = companyServiceClient.readHubIdByCompanyId(supplyCompanyId);
		// 주문상품 IDi :19095

		UUID orderProductId = orderRequestDto.getOrderProductId();
		// 허브 재고 조회
		HubStockResponseDto hubStockInfo = hubServiceClient.readHubStock(supplyCompanyHubId, orderProductId);
		// 재고 부족 예외 처리
		if (hubStockInfo.getData() == null
			|| hubStockInfo.getData().getHsQuantity() < orderRequestDto.getOrderProductQuantity()) {
			throw new CustomApiException(OrderException.HUB_INSUFFICIENT_STOCK);
		}
		// 허브 재고 업데이트
		HubStockUpdateDto hubStockUpdateDto = HubStockUpdateDto.builder()
			.quantity(-orderRequestDto.getOrderProductQuantity())
			.build();
		HubStockResponseDto updatedHubStockInfo = hubServiceClient.updateHubStock(supplyCompanyHubId, orderProductId, hubStockUpdateDto);

		// 주문 생성
		Order order = orderRepository.save(Order.from(orderRequestDto));

		// 배송 생성 요청
		// 주문자(수령업체) 업체 조회
		UUID receiveCompanyId = orderRequestDto.getOrderReceiveCompanyId();    // 수령업체 ID
		CompanyInfoDto.CompanyData receiveCompanyInfo = Objects.requireNonNull(
			companyServiceClient.getCompany(receiveCompanyId).getData());
		UUID receiveCompanyManagerId = receiveCompanyInfo.getCompanyManagerId();
		// 주문자(수령업체) 업체담당자 유저 정보 조회
		UserResponseDto receiveCompanyManagerInfo = Objects.requireNonNull(
			userServiceClient.getUser(receiveCompanyManagerId));
		// 배송 생성
		DeliveryRequestDto deliveryRequestDto = DeliveryRequestDto.builder()
			.orderId(order.getOrderId())
			.supplyCompanyHubId(supplyCompanyHubId)
			.receiveCompanyHubId(receiveCompanyInfo.getCompanyHubId())
			.receiveCompanyAddress(receiveCompanyInfo.getCompanyAddress())
			.receiverName(receiveCompanyManagerInfo.getData().getUserNickname())
			.receiverSlackId(receiveCompanyManagerInfo.getData().getUserSlackId())
			.build();
		DeliveryResponseDto deliveryInfo = deliveryServiceClient.createDelivery(deliveryRequestDto);
		// 생성된 배송ID 받아 저장
		order.saveOrderDeliveryId(deliveryInfo.getData().getDeliveryId());

		TransactionSynchronizationManager.registerSynchronization(
			new org.springframework.transaction.support.TransactionSynchronization() {
				@Override
				public void afterCommit() {
					GeminiResponseDto geminiResponseDto = slackServiceClient.sendAiMessage(deliveryInfo);
				}
			});

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
		List<UUID> deliveries = new ArrayList<>();
		UUID companyId = null;
		switch (userRole) {
			case "MASTER":
				break;
			case "HUB_MANAGER":
				// 로그인 유저가 허브관리자인 경우, 유저ID(허브관리자ID)로 허브ID 조회
				UUID hubId = hubServiceClient.readHubIdByHubManagerId(userId);
				// 허브에 소속된 업체ID 목록 - 공급업체ID/수령업체ID 중에 해당되는 주문 리스트 조회
				companies = companyServiceClient.readCompaniesByHubId(hubId);
				break;
			case "DELIVERY_MANAGER":
				// 로그인 유저(배송담당자)가 담당하는 배송ID 리스트 받기
				DeliveriesUUIDDto deliveriesId = deliveryServiceClient.queryAllDeliveries(userId);
				if (deliveriesId.getData() == null) {
					throw new CustomApiException(OrderException.DELIVERY_NOT_FOUND);
				}
				for (UUID id : deliveriesId.getData().getDeliveryIds()) {
					deliveries.add(id);
				}
				break;
			case "COMPANY_MANAGER":
				// 공급업체ID/수령업체ID 중에 해당되는 주문 리스트 조회
				companyId = companyServiceClient.readCompanyIdByCompanyManagerId(userId);
				break;
			default:
				throw new CustomApiException(OrderException.ORDER_NOT_FOUND);
		}
		Page<OrderResponseDto> orders = orderQueryRepository.findAllOrdersByRole(keyword,
			PageUtils.pageable(page, size), userRole, userId, companyId, companies, deliveries, sortDirection, sortBy);
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