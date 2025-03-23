package com.fn.eureka.client.companyservice.domain.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.companyservice.presentation.request.CompanyRequestDto;
import com.fn.eureka.client.companyservice.application.dto.CompanyResponseDto;

public interface CompanyService {

	CompanyResponseDto addCompany(CompanyRequestDto companyRequestDto, String userRole, UUID userId);

	CompanyResponseDto findTheCompany(UUID companyId);

	Page<CompanyResponseDto> findAllCompanies(UUID hubId, String type, String keyword, int page, int size, Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy, String userRole);

	CompanyResponseDto modifyCompany(UUID companyId, CompanyRequestDto requestDto, String userRole, UUID userId);

	void removeCompany(UUID companyId, String userRole, UUID userId);

	List<UUID> findAllCompaniesByHubId(UUID hubId);

	UUID findCompanyIdByCompanyManagerId(UUID companyManagerId);

	UUID findHubIdByCompanyId(UUID companyId);
}
