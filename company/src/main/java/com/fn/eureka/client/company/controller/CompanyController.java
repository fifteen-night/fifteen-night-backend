package com.fn.eureka.client.company.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fn.eureka.client.company.service.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

	private final CompanyService companyService;

	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		companyService.errorTest();
		return ResponseEntity.ok("Hello Exception");
	}
}
