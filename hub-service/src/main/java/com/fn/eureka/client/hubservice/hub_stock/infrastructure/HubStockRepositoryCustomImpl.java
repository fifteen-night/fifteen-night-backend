package com.fn.eureka.client.hubservice.hub_stock.infrastructure;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.fn.eureka.client.hubservice.hub_stock.application.dto.mapper.HubStockMapper;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;
import com.fn.eureka.client.hubservice.hub_stock.domain.QHubStock;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HubStockRepositoryCustomImpl implements HubStockRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	QHubStock qHubStock = QHubStock.hubStock;

	@Override
	public Page<ReadHubStockResponse> searchHubStock(UUID hubId, Pageable pageable, UUID productId, int quantity,
		LocalDateTime startDateTime, LocalDateTime endDateTime) {

		BooleanBuilder builder = new BooleanBuilder();
		List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort(), qHubStock);

		if (productId != null) {
			builder.and(qHubStock.hsProductId.eq(productId));
		}

		builder.and(qHubStock.hsHub.hubId.eq(hubId));
		builder.and(qHubStock.isDeleted.isFalse());
		builder.and(qHubStock.hsQuantity.goe(quantity));
		builder.and(qHubStock.createdAt.between(startDateTime, endDateTime));

		List<HubStock> hubStocks = queryFactory
			.selectFrom(qHubStock)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
			.fetch();

		List<ReadHubStockResponse> content = hubStocks.stream().map(e -> HubStockMapper.toDto(e, e.getHsId())).toList();

		long total = queryFactory
			.select(qHubStock.count())
			.from(qHubStock)
			.where(builder)
			.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}

	private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort, QHubStock qHubStock) {
		List<OrderSpecifier<?>> orders = new ArrayList<>();
		PathBuilder<HubStock> pathBuilder = new PathBuilder<>(HubStock.class, qHubStock.getMetadata());

		for (Sort.Order s : sort) {
			Order order = s.isAscending() ? Order.ASC : Order.DESC;
			orders.add(new OrderSpecifier(order, pathBuilder.get(s.getProperty())));
		}

		return orders;
	}
}