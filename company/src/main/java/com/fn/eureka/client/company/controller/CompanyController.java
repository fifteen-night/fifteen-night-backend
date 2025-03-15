package com.fn.eureka.client.company.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fn.eureka.client.company.dto.CompanyCreateRequestDto;
import com.fn.eureka.client.company.dto.CompanyCreateResponseDto;
import com.fn.eureka.client.company.service.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

	private final CompanyService companyService;
	
	@PostMapping
	public ResponseEntity<CompanyCreateResponseDto> createCompany(
		@RequestBody CompanyCreateRequestDto companyCreateRequestDto,
		@RequestHeader("X-User-Role") String userRole
	) {
		CompanyCreateResponseDto responseDto = companyService.addCompany(companyCreateRequestDto, userRole);
		return ResponseEntity.ok(responseDto);
	}

}
