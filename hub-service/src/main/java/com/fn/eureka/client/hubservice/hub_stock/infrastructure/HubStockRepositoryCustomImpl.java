package com.fn.eureka.client.hubservice.hub_stock.infrastructure;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.fn.eureka.client.hubservice.hub.application.ProductClientService;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadProductResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.QHubStock;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HubStockRepositoryCustomImpl implements HubStockRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private final ProductClientService productClientService;

	QHubStock qHubStock = QHubStock.hubStock;

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
				qHubStock.hsQuantity,
				Expressions.nullExpression(ReadProductResponse.class)))
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

		List<UUID> products = content.stream()
			.map(ReadHubStockResponse::getHsProductId)
			.distinct()
			.toList();

		List<ReadProductResponse> productDetails = productClientService.readProductList(products);

		Map<UUID, ReadProductResponse> detailMap = productDetails.stream()
			.collect(Collectors.toMap(ReadProductResponse::getProductId, Function.identity()));

		List<ReadHubStockResponse> finalContent = content.stream()
			.map(e -> ReadHubStockResponse.builder()
				.hsId(e.getHsId())
				.hsProductId(e.getHsProductId())
				.hsHubId(e.getHsHubId())
				.hsQuantity(e.getHsQuantity())
				.detail(detailMap.get(e.getHsProductId()))
				.build()
			).toList();

		return new PageImpl<>(finalContent, pageable, total);
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