package com.fn.eureka.client.product.infrastructure;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.eureka.client.product.application.dto.CompanyInfoDto;

@FeignClient(name = "company-service", path = "/api/companies")
public interface CompanyServiceClient {

	@GetMapping("/{companyId}")
	CompanyInfoDto getCompany(@PathVariable("companyId") UUID companyId);

}
