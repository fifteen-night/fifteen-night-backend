package com.fn.eureka.client.orderservice.application;

import org.springframework.stereotype.Service;

import com.fn.eureka.client.orderservice.domain.OrderRepository;
import com.fn.eureka.client.orderservice.presentation.dto.OrderRequestDto;
import com.fn.eureka.client.orderservice.presentation.dto.OrderResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	// 주문 생성
	@Override
	public OrderResponseDto addOrder(OrderRequestDto requestDto, String userRole) {
		switch (userRole) {
			case "MASTER":
				break;
			case "HUB_MANAGER":
				// 담당 공급업체들의 주문만 생성?
				break;
			case "DELIVERY_MANAGER":
				// 배송담당자가 주문을 왜 함...??
				break;
			case "COMPANY_MANAGER":
				// 본인 업체 주문에서 주문하는 경우만 가능 (RECEIVER 입장)
				break;
		}

		return null;
	}
}
