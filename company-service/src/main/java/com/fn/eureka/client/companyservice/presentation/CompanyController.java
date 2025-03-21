package com.fn.eureka.client.companyservice.presentation;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.companyservice.application.CompanyService;
import com.fn.eureka.client.companyservice.presentation.dto.CompanyRequestDto;
import com.fn.eureka.client.companyservice.presentation.dto.CompanyResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

	private final CompanyService companyService;

	// 업체 생성
	@PostMapping
	public ResponseEntity<CompanyResponseDto> createCompany(
		@RequestBody CompanyRequestDto requestDto,
		@RequestHeader("X-User-Role") String userRole
	) {
		CompanyResponseDto response = companyService.addCompany(requestDto, userRole);
		return ResponseEntity.ok(response);
	}

	// 업체 조회
	@GetMapping("/{companyId}")
	public ResponseEntity<CompanyResponseDto> getCompany(@PathVariable("companyId") UUID companyId) {
		CompanyResponseDto response = companyService.findTheCompany(companyId);
		return ResponseEntity.ok(response);
	}

	// 업체 리스트 조회 + 검색
	@GetMapping
	public ResponseEntity<Page<CompanyResponseDto>> getCompanies(
		@RequestParam(required = false) UUID hubId,
		@RequestParam(defaultValue = "whole", required = false) String type,
		@RequestParam(required = false) String keyword,
		@RequestParam(defaultValue = "0", required = false) int page,
		@RequestParam(defaultValue = "10", required = false) int size,
		@RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
		@RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy,
		@RequestHeader("X-User-Role") String userRole
		) {
		Page<CompanyResponseDto> companies = companyService.findAllCompaniesByType(hubId, type, keyword, page, size, sortDirection, sortBy, userRole);
		return ResponseEntity.ok(companies);
	}

	// 업체 수정
	@PutMapping("/{companyId}")
	public ResponseEntity<CompanyResponseDto> updateCompany(@PathVariable("companyId") UUID companyId,
		@RequestBody CompanyRequestDto requestDto) {
		CompanyResponseDto response = companyService.modifyCompany(companyId, requestDto);
		return ResponseEntity.ok(response);
	}

	// 업체 삭제
	@DeleteMapping("/{companyId}")
	public ResponseEntity<Void> deleteCompany(@PathVariable("companyId") UUID companyId) {
		companyService.removeCompany(companyId);
		return ResponseEntity.noContent().build();
	}

	// for other services...

	// 허브에 소속된 업체ID 목록 조회
	@GetMapping("/hub/{hubId}")
	public List<UUID> readCompaniesByHubId(@PathVariable("hubId") UUID hubId) {
		return companyService.findAllCompaniesByHubId(hubId);
	}

	// 업체담당자ID로 업체 조회
	@GetMapping("/company-manager/{companyManagerId}")
	UUID readCompanyIdByCompanyManagerId(@PathVariable("companyManagerId")UUID companyManagerId) {
		return companyService.findCompanyIdByCompanyManagerId(companyManagerId);
	}
}
