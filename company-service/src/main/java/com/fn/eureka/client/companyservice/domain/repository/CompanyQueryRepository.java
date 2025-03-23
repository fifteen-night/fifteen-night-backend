package com.fn.eureka.client.companyservice.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.companyservice.application.dto.CompanyResponseDto;

public interface CompanyQueryRepository {

	Page<CompanyResponseDto> findCompanies(UUID hubId, String type, String keyword, Pageable pageable,
		Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy,
		String userRole);

	List<UUID> findCompanyIdByCompanyHubId(UUID hubId);

	UUID findCompanyIdByCompanyManagerId(UUID companyManagerId);

	UUID findHubIdByCompanyId(UUID companyId);
}
