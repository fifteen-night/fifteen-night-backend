package com.fn.eureka.client.company.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.company.dto.CompanyCreateRequestDto;
import com.fn.eureka.client.company.dto.CompanyResponseDto;

public interface CompanyService {

	CompanyResponseDto addCompany(CompanyCreateRequestDto companyCreateRequestDto, String userRole);

	CompanyResponseDto findTheCompany(UUID companyId);

	Page<CompanyResponseDto> findAllCompaniesByType(UUID hubId, String type, String keyword, int page, int size, Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy, String userRole);
}
