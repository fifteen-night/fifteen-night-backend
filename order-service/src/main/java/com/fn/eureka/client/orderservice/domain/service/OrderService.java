package com.fn.eureka.client.orderservice.domain.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.orderservice.presentation.request.OrderRequestDto;
import com.fn.eureka.client.orderservice.application.dto.OrderResponseDto;

public interface OrderService {
	OrderResponseDto addOrder(OrderRequestDto requestDto);

	OrderResponseDto findOrder(UUID orderId);

	Page<OrderResponseDto> findAllOrdersByRole(String keyword, int page, int size, Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy, String userRole, UUID userId);

	OrderResponseDto modifyOrder(UUID orderId, Map<String, Object> updates, String userRole, UUID userId);

	void removeOrder(UUID orderId, String userRole, UUID userId);

	List<UUID> findOrderProductIdListByDeliveryId(List<UUID> deliveries);
}
