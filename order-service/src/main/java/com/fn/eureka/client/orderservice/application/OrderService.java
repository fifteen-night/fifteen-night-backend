package com.fn.eureka.client.orderservice.application;

import java.util.UUID;

import com.fn.eureka.client.orderservice.presentation.dto.OrderRequestDto;
import com.fn.eureka.client.orderservice.presentation.dto.OrderResponseDto;

public interface OrderService {
	OrderResponseDto addOrder(OrderRequestDto requestDto);

	OrderResponseDto findOrder(UUID orderId);
}
