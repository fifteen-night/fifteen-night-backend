package com.fn.eureka.client.hubservice.hub.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub.domain.QHub;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HubRepositoryCustomImpl implements HubRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	QHub qHub = QHub.hub;

	@Override
	public Page<Hub> searchHubs(Pageable pageable, String hubName) {

		BooleanBuilder builder = new BooleanBuilder();
		List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort(), qHub);

		if (hubName != null || hubName.isEmpty()) {
			builder.and(qHub.hubName.containsIgnoreCase(hubName));
		}

		builder.and(qHub.deletedAt.isNull());

		List<Hub> content = queryFactory
			.selectFrom(qHub)
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

		return new PageImpl<>(content, pageable, total);
	}

	private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort, QHub hub) {
		List<OrderSpecifier<?>> orders = new ArrayList<>();
		PathBuilder<Hub> pathBuilder = new PathBuilder<>(Hub.class, hub.getMetadata());

		for (Sort.Order s : sort) {
			Order order = s.isAscending() ? Order.ASC : Order.DESC;
			orders.add(new OrderSpecifier(order, pathBuilder.get(s.getProperty())));
		}

		return orders;
	}
}