package com.fn.eureka.client.orderservice.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.exception.ConflictException;
import com.fn.common.global.exception.CustomApiException;
import com.fn.eureka.client.orderservice.domain.Order;
import com.fn.eureka.client.orderservice.domain.OrderRepository;
import com.fn.eureka.client.orderservice.exception.OrderException;
import com.fn.eureka.client.orderservice.infrastructure.CompanyServiceClient;
import com.fn.eureka.client.orderservice.infrastructure.DeliveryServiceClient;
import com.fn.eureka.client.orderservice.infrastructure.HubServiceClient;
import com.fn.eureka.client.orderservice.infrastructure.UserServiceClient;
import com.fn.eureka.client.orderservice.presentation.dto.CompanyInfoDto;
import com.fn.eureka.client.orderservice.presentation.dto.DeliveryRequestDto;
import com.fn.eureka.client.orderservice.presentation.dto.DeliveryResponseDto;
import com.fn.eureka.client.orderservice.presentation.dto.HubStockResponseDto;
import com.fn.eureka.client.orderservice.presentation.dto.OrderRequestDto;
import com.fn.eureka.client.orderservice.presentation.dto.OrderResponseDto;
import com.fn.eureka.client.orderservice.presentation.dto.UserResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

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
}
