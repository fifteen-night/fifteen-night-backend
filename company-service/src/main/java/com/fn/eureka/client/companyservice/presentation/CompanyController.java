package com.fn.eureka.client.companyservice.presentation;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.companyservice.domain.service.CompanyService;
import com.fn.eureka.client.companyservice.presentation.request.CompanyRequestDto;
import com.fn.eureka.client.companyservice.application.dto.CompanyResponseDto;

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
	public ResponseEntity<CommonResponse<CompanyResponseDto>> createCompany(
		@RequestBody CompanyRequestDto companyRequestDto,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") UUID userId
	) {
		CompanyResponseDto companyResponseDto = companyService.addCompany(companyRequestDto, userRole, userId);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/companies").build().toUri();
		return ResponseEntity.created(location).body(new CommonResponse<>(SuccessCode.COMPANY_CREATE, companyResponseDto));
	}

	// 업체 조회
	@GetMapping("/{companyId}")
	public ResponseEntity<CommonResponse<CompanyResponseDto>> getCompany(@PathVariable("companyId") UUID companyId) {
		CompanyResponseDto companyResponseDto = companyService.findTheCompany(companyId);
		return ResponseEntity.ok().body(new CommonResponse<>(SuccessCode.COMPANY_SEARCH_ONE, companyResponseDto));
	}

	// 업체 리스트 조회 + 검색
	@GetMapping
	public ResponseEntity<CommonResponse<Page<CompanyResponseDto>>> getCompanies(
		@RequestParam(required = false) UUID hubId,
		@RequestParam(defaultValue = "whole", required = false) String type,
		@RequestParam(required = false) String keyword,
		@RequestParam(defaultValue = "0", required = false) int page,
		@RequestParam(defaultValue = "10", required = false) int size,
		@RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
		@RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy,
		@RequestHeader("X-User-Role") String userRole
		) {
		Page<CompanyResponseDto> companies = companyService.findAllCompanies(hubId, type, keyword, page, size, sortDirection, sortBy, userRole);
		return ResponseEntity.ok().body(new CommonResponse<>(SuccessCode.COMPANY_SEARCH_ALL, companies));
	}

	// 업체 수정
	@PutMapping("/{companyId}")
	public ResponseEntity<CommonResponse<CompanyResponseDto>> updateCompany(
		@PathVariable("companyId") UUID companyId,
		@RequestBody CompanyRequestDto companyRequestDto,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") UUID userId) {
		CompanyResponseDto companyResponseDto = companyService.modifyCompany(companyId, companyRequestDto, userRole, userId);
		return ResponseEntity.ok().body(new CommonResponse<>(SuccessCode.COMPANY_UPDATE, companyResponseDto));
	}

	// 업체 삭제
	@DeleteMapping("/{companyId}")
	public ResponseEntity<CommonResponse> deleteCompany(
		@PathVariable("companyId") UUID companyId,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") UUID userId) {
		companyService.removeCompany(companyId, userRole, userId);
		return ResponseEntity.status(SuccessCode.COMPANY_DELETE.getStatusCode()).body(new CommonResponse<>(SuccessCode.COMPANY_DELETE, companyId));
	}

	// for other services...

	// 허브에 소속된 업체ID 목록 조회
	@GetMapping("/company-list/{hubId}")
	public List<UUID> readCompaniesByHubId(@PathVariable("hubId") UUID hubId) {
		return companyService.findAllCompaniesByHubId(hubId);
	}

	// 업체담당자ID로 업체 조회
	@GetMapping("/company-manager/{companyManagerId}")
	UUID readCompanyIdByCompanyManagerId(@PathVariable("companyManagerId")UUID companyManagerId) {
		return companyService.findCompanyIdByCompanyManagerId(companyManagerId);
	}

	// 업체ID로 허브ID 반환
	@GetMapping("/hub/{companyId}")
	UUID readCompanyIdByHubId(@PathVariable("companyId") UUID companyId) {
		return companyService.findHubIdByCompanyId(companyId);
	}
}
