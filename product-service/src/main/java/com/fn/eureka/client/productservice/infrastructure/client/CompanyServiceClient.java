package com.fn.eureka.client.productservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.eureka.client.productservice.application.dto.CompanyInfoDto;

@FeignClient(name = "company-service", url = "http://localhost:19094/api/companies")
public interface CompanyServiceClient {

	@GetMapping("/{companyId}")
	CompanyInfoDto getCompany(@PathVariable("companyId") UUID companyId);

}
