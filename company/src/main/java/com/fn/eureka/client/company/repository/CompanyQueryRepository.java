package com.fn.eureka.client.company.repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.company.dto.CompanyResponseDto;
import com.fn.eureka.client.company.entity.Company;
import com.fn.eureka.client.company.entity.QCompany;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class CompanyQueryRepository {

	private final JPAQueryFactory queryFactory;

	// TODO 질문 - common 모듈에서 QuerydslConfig 설정했는데 queryFactory 부분에 자꾸 빨간줄이 뜸
	public CompanyQueryRepository(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	public Page<CompanyResponseDto> findCompaniesByType(UUID hubId, String type, String keyword, Pageable pageable,
		Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy,
		String userRole) {
		QCompany company = QCompany.company;
		BooleanBuilder builder = new BooleanBuilder();

		// 허브별 업체 조회 조건
		if ("HUB MANAGER".equals(userRole) && "hub".equalsIgnoreCase(type) && hubId != null) {
			builder.and(company.companyHubId.eq(hubId));
		}

		// 검색어 조건 (회사명 또는 주소)
		if (keyword != null && !keyword.isEmpty()) {
			builder.and(company.companyName.containsIgnoreCase(keyword)
				.or(company.companyAddress.containsIgnoreCase(keyword)));
		}

		// 정렬 조건
		OrderSpecifier<?> orderSpecifier = PageUtils.getCommonOrderSpecifier(company, sortDirection, sortBy);

		// QueryDSL 조회
		JPAQuery<Company> query = queryFactory
			.selectFrom(company)
			.where(builder)
			.orderBy(orderSpecifier)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		List<Company> result = query.fetch();
		long total = query.fetchCount();

		// 엔티티 → DTO 변환
		List<CompanyResponseDto> dtoList = result.stream()
			.map(CompanyResponseDto::new)
			.collect(Collectors.toList());

		return new PageImpl<>(dtoList, pageable, total);
	}
}
