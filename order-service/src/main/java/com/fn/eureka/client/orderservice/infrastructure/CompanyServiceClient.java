package com.fn.eureka.client.orderservice.infrastructure;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.orderservice.presentation.dto.CompanyInfoDto;

@FeignClient(name = "company-service", path = "/api/companies")
public interface CompanyServiceClient {

	// 업체 조회
	@GetMapping("/{companyId}")
	CompanyInfoDto getCompany(@PathVariable("companyId") UUID companyId);

	// 허브별 업체ID 목록 조회
	@GetMapping("/hub/{hubId}")
	List<UUID> readCompaniesByHubId(@PathVariable("hubId") UUID hubId);

	// 업체담당자ID로 업체ID 조회
	@GetMapping("/company-manager/{companyManagerId}")
	UUID readCompanyIdByCompanyManagerId(@PathVariable("companyManagerId")UUID companyManagerId);
}
