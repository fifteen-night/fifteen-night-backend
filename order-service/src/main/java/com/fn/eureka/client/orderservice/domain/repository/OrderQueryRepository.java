package com.fn.eureka.client.orderservice.domain.repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.orderservice.domain.Order;
import com.fn.eureka.client.orderservice.domain.QOrder;
import com.fn.eureka.client.orderservice.presentation.dto.OrderResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class OrderQueryRepository {

	private final JPAQueryFactory queryFactory;

	public OrderQueryRepository(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	// List<UUID> companies, List<UUID> deliveries,
	public Page<OrderResponseDto> findAllOrdersByRole(
		String keyword, Pageable pageable, String userRole, UUID userId, UUID companyId,
		Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy) {
		QOrder order = QOrder.order;
		BooleanBuilder builder = new BooleanBuilder();

		// 기본 조건: 삭제되지 않은 주문
		builder.and(order.isDeleted.eq(false));

		// // 허브별 조회
		// if ("HUB_MANAGER".equals(userRole) && companies != null && !companies.isEmpty()) {
		// 	builder.and(order.orderSupplyCompanyId.in(companies)
		// 		.or(order.orderReceiveCompanyId.in(companies)));
		// }
		//
		// // 배송담당자별 조회
		// if ("DELIVERY_MANAGER".equals(userRole) && deliveries != null && !deliveries.isEmpty()) {
		// 	builder.and(order.orderDeliveryId.in(deliveries));
		// }

		// 업체별 조회
		if ("COMPANY_MANAGER".equals(userRole) && companyId != null) {
			builder.and(order.orderSupplyCompanyId.in(companyId)
				.or(order.orderReceiveCompanyId.in(companyId)));
		}

		// 검색어 (주문번호) - 근데 주문은 뭐로 검색해야 맞을지 모르겠긴 함...
		if (keyword != null && !keyword.isEmpty()) {
			builder.and(Expressions.stringTemplate("CAST({0} AS string)", order.orderId)
				.containsIgnoreCase(keyword));
		}

		// 정렬 적용
		OrderSpecifier<?> orderSpecifier = PageUtils.getCommonOrderSpecifier(order, sortDirection, sortBy);

		// QueryDSL 실행
		JPAQuery<Order> query = queryFactory
			.selectFrom(order)
			.where(builder)
			.orderBy(orderSpecifier)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		List<Order> orders = query.fetch();
		long total = query.fetchCount();

		// DTO 변환
		List<OrderResponseDto> dtoList = orders.stream()
			.map(OrderResponseDto::new)
			.collect(Collectors.toList());

		return new PageImpl<>(dtoList, pageable, total);
	}

}
