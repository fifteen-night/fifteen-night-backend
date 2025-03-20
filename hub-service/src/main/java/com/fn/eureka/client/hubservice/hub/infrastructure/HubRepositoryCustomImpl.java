package com.fn.eureka.client.hubservice.hub.infrastructure;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;
import com.fn.eureka.client.hubservice.hub.domain.QHub;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.QHubStock;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HubRepositoryCustomImpl implements HubRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	QHub qHub = QHub.hub;
	QHubStock qHubStock = QHubStock.hubStock;

	@Override
	public Page<ReadHubResponse> searchHubs(Pageable pageable, String hubName) {

		BooleanBuilder builder = new BooleanBuilder();
		List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort(), qHub);

		if (hubName != null && !hubName.isEmpty()) {
			builder.and(qHub.hubName.containsIgnoreCase(hubName));
		}

		builder.and(qHub.isDeleted.isFalse());

		List<ReadHubResponse> hubs = queryFactory
			.select(Projections.constructor(
				ReadHubResponse.class,
				qHub.hubId,
				qHub.hubName,
				qHub.hubAddress,
				qHub.hubType,
				qHub.hubManagerId,
				qHub.hubLatitude,
				qHub.hubLongitude))
			.from(qHub)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
			.fetch();

		long total = queryFactory
			.select(qHub.count())
			.from(qHub)
			.where(builder)
			.fetchOne();

		return new PageImpl<>(hubs, pageable, total);
	}

	@Override
	public ReadHubStockResponse readHubStock(UUID hubId, UUID productId) {
		return queryFactory
			.select(Projections.constructor(
				ReadHubStockResponse.class,
				qHubStock.hsId,
				qHubStock.hsProductId,
				qHubStock.hsHub.hubId,
				qHubStock.hsQuantity))
			.from(qHubStock)
			.where(
				qHubStock.hsHub.hubId.eq(hubId),
				qHubStock.hsProductId.eq(productId),
				qHubStock.isDeleted.isFalse())
			.fetchOne();
	}

	@Override
	public Page<ReadHubStockResponse> searchHubStock(UUID hubId, Pageable pageable, UUID productId, int quantity,
		LocalDateTime startDateTime, LocalDateTime endDateTime) {

		BooleanBuilder builder = new BooleanBuilder();
		List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort(), qHubStock);

		if (productId != null) {
			builder.and(qHubStock.hsProductId.eq(productId));
		}

		builder.and(qHubStock.hsHub.hubId.eq(hubId))
			.and(qHubStock.isDeleted.isFalse())
			.and(qHubStock.hsQuantity.goe(quantity))
			.and(qHubStock.createdAt.between(startDateTime, endDateTime));

		List<ReadHubStockResponse> content = queryFactory
			.select(Projections.constructor(
				ReadHubStockResponse.class,
				qHubStock.hsId,
				qHubStock.hsProductId,
				qHubStock.hsHub.hubId,
				qHubStock.hsQuantity))
			.from(qHubStock)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
			.fetch();

		long total = queryFactory
			.select(qHubStock.count())
			.from(qHubStock)
			.where(builder)
			.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}

	private <T> List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort, EntityPath<T> entityPath) {
		List<OrderSpecifier<?>> orders = new ArrayList<>();
		PathBuilder<T> pathBuilder = new PathBuilder<>(entityPath.getType(), entityPath.getMetadata());

		for (Sort.Order s : sort) {
			Order order = s.isAscending() ? Order.ASC : Order.DESC;
			orders.add(new OrderSpecifier(order, pathBuilder.get(s.getProperty())));
		}

		return orders;
	}
}