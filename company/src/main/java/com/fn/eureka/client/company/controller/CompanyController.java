package com.fn.eureka.client.company.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fn.eureka.client.company.dto.CompanyCreateRequestDto;
import com.fn.eureka.client.company.dto.CompanyResponseDto;
import com.fn.eureka.client.company.service.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

	private final CompanyService companyService;

	// 업체 생성
	@PostMapping
	public ResponseEntity<CompanyResponseDto> createCompany(
		@RequestBody CompanyCreateRequestDto companyCreateRequestDto,
		@RequestHeader("X-User-Role") String userRole
	) {
		CompanyResponseDto response = companyService.addCompany(companyCreateRequestDto, userRole);
		return ResponseEntity.ok(response);
	}

	// 업체 조회
	@GetMapping("/{companyId}")
	public ResponseEntity<CompanyResponseDto> getTheCompany(@PathVariable("companyId")UUID companyId) {
		CompanyResponseDto response = companyService.findTheCompany(companyId);
		return ResponseEntity.ok(response);
	}

}
