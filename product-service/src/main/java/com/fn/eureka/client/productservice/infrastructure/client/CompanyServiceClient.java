package com.fn.eureka.client.productservice.infrastructure.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.eureka.client.productservice.application.dto.CompanyInfoDto;

@FeignClient(name = "company-service", url = "http://localhost:19094/api/companies")
public interface CompanyServiceClient {

	// 업체 조회
	@GetMapping("/{companyId}")
	CompanyInfoDto getCompany(@PathVariable("companyId") UUID companyId);

	// 허브별 업체ID 목록 조회
	@GetMapping("/company-list/{hubId}")
	List<UUID> readCompaniesByHubId(@PathVariable("hubId") UUID hubId);

	// 업체담당자ID로 업체ID 조회
	@GetMapping("/company-manager/{companyManagerId}")
	UUID readCompanyIdByCompanyManagerId(@PathVariable("companyManagerId")UUID companyManagerId);

	// 업체ID로 허브ID 반환
	@GetMapping("/hub/{companyId}")
	UUID readHubIdByCompanyId(@PathVariable("companyId") UUID companyId);

}
