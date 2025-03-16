package com.fn.eureka.client.company.service;

import java.util.UUID;

import com.fn.eureka.client.company.dto.CompanyCreateRequestDto;
import com.fn.eureka.client.company.dto.CompanyResponseDto;

public interface CompanyService {

	CompanyResponseDto addCompany(CompanyCreateRequestDto companyCreateRequestDto, String userRole);

	CompanyResponseDto findTheCompany(UUID companyId);
}
