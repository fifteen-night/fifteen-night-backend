package com.fn.eureka.client.deliverymanagerservice.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerSearchCondition;
import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManager;
import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManagerType;
import com.fn.eureka.client.deliverymanagerservice.domain.entity.QDeliveryManager;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class DeliveryManagerRepositoryImpl implements DeliveryManagerRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public DeliveryManagerRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<DeliveryManager> search(DeliveryManagerSearchCondition condition, Pageable pageable) {
		QDeliveryManager manager = QDeliveryManager.deliveryManager;

		List<DeliveryManager> content = queryFactory
			.selectFrom(manager)
			.where(
				manager.deletedAt.isNull(),
				eqHubId(condition.getDmHubId()),
				eqType(condition.getDmType()),
				containsSlackId(condition.getDmSlackId()),
				createdAtBetween(condition.getCreatedAtFrom(), condition.getCreatedAtTo())
			)
			.orderBy(getSort(condition.getSortBy(), condition.getSortDir(), manager))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory
			.select(manager.count())
			.from(manager)
			.where(
				manager.deletedAt.isNull(),
				eqHubId(condition.getDmHubId()),
				eqType(condition.getDmType()),
				eqUserId(condition.getDmUserId()),  // ← 추가
				containsSlackId(condition.getDmSlackId()),
				createdAtBetween(condition.getCreatedAtFrom(), condition.getCreatedAtTo())
			)
			.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}

	private BooleanExpression eqUserId(UUID dmUserId) {
		return dmUserId != null ? QDeliveryManager.deliveryManager.dmUserId.eq(dmUserId) : null;
	}

	private BooleanExpression eqHubId(UUID dmHubId) {
		return dmHubId != null ? QDeliveryManager.deliveryManager.dmHubId.eq(dmHubId) : null;
	}

	private BooleanExpression eqType(DeliveryManagerType dmType) {
		return dmType != null ? QDeliveryManager.deliveryManager.dmType.eq(dmType) : null;
	}

	private BooleanExpression containsSlackId(String slackId) {
		return slackId != null && !slackId.isBlank()
			? QDeliveryManager.deliveryManager.dmSlackId.containsIgnoreCase(slackId)
			: null;
	}

	private BooleanExpression createdAtBetween(LocalDateTime from, LocalDateTime to) {
		if (from != null && to != null) {
			return QDeliveryManager.deliveryManager.createdAt.between(from, to);
		} else if (from != null) {
			return QDeliveryManager.deliveryManager.createdAt.goe(from);
		} else if (to != null) {
			return QDeliveryManager.deliveryManager.createdAt.loe(to);
		} else {
			return null;
		}
	}

	private OrderSpecifier<?> getSort(String sortBy, String sortDir, QDeliveryManager manager) {
		boolean isAsc = "asc".equalsIgnoreCase(sortDir);

		if ("updatedAt".equalsIgnoreCase(sortBy)) {
			return isAsc ? manager.updatedAt.asc() : manager.updatedAt.desc();
		}
		// 기본 정렬: createdAt
		return isAsc ? manager.createdAt.asc() : manager.createdAt.desc();
	}
}

