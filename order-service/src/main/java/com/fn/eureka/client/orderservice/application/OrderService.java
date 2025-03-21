package com.fn.eureka.client.orderservice.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.orderservice.presentation.dto.OrderRequestDto;
import com.fn.eureka.client.orderservice.presentation.dto.OrderResponseDto;

public interface OrderService {
	OrderResponseDto addOrder(OrderRequestDto requestDto);

	OrderResponseDto findOrder(UUID orderId);

	Page<OrderResponseDto> findAllOrdersByRole(String keyword, int page, int size, Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy, String userRole, UUID userId);
}
