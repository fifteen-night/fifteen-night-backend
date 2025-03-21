package com.fn.eureka.client.companyservice.domain.repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.companyservice.presentation.dto.CompanyResponseDto;
import com.fn.eureka.client.companyservice.domain.entity.Company;
import com.fn.eureka.client.companyservice.domain.entity.QCompany;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class CompanyQueryRepository {

	private final JPAQueryFactory queryFactory;

	// public CompanyQueryRepository(EntityManager entityManager) {
	// 	this.queryFactory = new JPAQueryFactory(entityManager);
	// }

	@Autowired
	public CompanyQueryRepository(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	// 업체 리스트 조회
	public Page<CompanyResponseDto> findCompaniesByType(UUID hubId, String type, String keyword, Pageable pageable,
		Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy,
		String userRole) {
		QCompany company = QCompany.company;
		BooleanBuilder builder = new BooleanBuilder();

		// 삭제되지 않은 항목만 조회
		builder.and(company.isDeleted.eq(false));

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

		JPAQuery<Company> query = queryFactory
			.selectFrom(company)
			.where(builder)
			.orderBy(orderSpecifier)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		List<Company> result = query.fetch();
		long total = query.fetchCount();

		List<CompanyResponseDto> dtoList = result.stream()
			.map(CompanyResponseDto::new)
			.collect(Collectors.toList());

		return new PageImpl<>(dtoList, pageable, total);
	}

	// 허브별 업체ID 목록 조회
	public List<UUID> findCompanyIdByCompanyHubId(UUID hubId) {
		QCompany company = QCompany.company;
		return queryFactory
			.select(company.companyId)
			.from(company)
			.where(company.companyHubId.eq(hubId))
			.fetch();
	}

	// 업체담당자ID로 업체ID 조회
	public UUID findCompanyIdByCompanyManagerId(UUID companyManagerId) {
		QCompany company = QCompany.company;
		return queryFactory
			.select(company.companyId)
			.from(company)
			.where(company.companyManagerId.eq(companyManagerId))
			.fetchOne();
	}

}
