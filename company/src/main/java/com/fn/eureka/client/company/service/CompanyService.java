package com.fn.eureka.client.company.service;

import com.fn.eureka.client.company.dto.CompanyCreateRequestDto;
import com.fn.eureka.client.company.dto.CompanyCreateResponseDto;

public interface CompanyService {

	CompanyCreateResponseDto addCompany(CompanyCreateRequestDto companyCreateRequestDto, String userRole);

}
