package com.fn.eureka.client.productservice.infrastructure.repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.productservice.application.dto.ProductResponseDto;
import com.fn.eureka.client.productservice.domain.model.Product;
import com.fn.eureka.client.productservice.domain.QProduct;
import com.fn.eureka.client.productservice.domain.repository.ProductQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

	private final JPAQueryFactory queryFactory;

	public ProductQueryRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	// @Autowired
	// public ProductQueryRepository(JPAQueryFactory queryFactory) {
	// 	this.queryFactory = queryFactory;
	// }

	// TODO client 문제 해결하면 List<UUID> companies 넣기
	public Page<ProductResponseDto> findProductsByType(String type, UUID id, String keyword, Pageable pageable, Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy) {
		QProduct product = QProduct.product;
		BooleanBuilder builder = new BooleanBuilder();

		// 삭제되지 않은 항목만 조회
		builder.and(product.isDeleted.eq(false));

		// 전체/허브별/업체별 조회 조건
		if ("whole".equalsIgnoreCase(type)) {
			// 전체 조회
		} else if ("hub".equalsIgnoreCase(type)) {
		// } else if ("hub".equalsIgnoreCase(type) && companies != null && !companies.isEmpty()) {
			// 허브별 조회 : 허브ID가 일치하는 업체ID를 가지고 있는 상품 조회
			// builder.and(product.productCompanyId.in(companies));
		} else if ("company".equalsIgnoreCase(type) && id != null) {
			// 업체별 조회
			builder.and(product.productCompanyId.eq(id));
		}

		// 검색어 조건 (상품명)
		if (keyword != null && !keyword.isEmpty()) {
			builder.and(product.productName.containsIgnoreCase(keyword));
		}

		// 정렬 조건
		OrderSpecifier<?> orderSpecifier = PageUtils.getCommonOrderSpecifier(product, sortDirection, sortBy);

		// QueryDSL 조회
		JPAQuery<Product> query = queryFactory
			.selectFrom(product)
			.where(builder)
			.orderBy(orderSpecifier)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		List<Product> result = query.fetch();
		long total = query.fetchCount();

		List<ProductResponseDto> dtoList = result.stream()
			.map(ProductResponseDto::new)
			.collect(Collectors.toList());

		return new PageImpl<>(dtoList, pageable, total);
	}
}
