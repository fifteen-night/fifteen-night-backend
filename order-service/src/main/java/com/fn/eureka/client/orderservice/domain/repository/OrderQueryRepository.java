package com.fn.eureka.client.orderservice.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.orderservice.application.dto.OrderResponseDto;

public interface OrderQueryRepository {

	Page<OrderResponseDto> findAllOrdersByRole(
		String keyword, Pageable pageable, String userRole, UUID userId, UUID companyId, List<UUID> companies,
		Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy);

	List<UUID> findOrderProductIdListByDeliveryId(List<UUID> deliveries);
}
