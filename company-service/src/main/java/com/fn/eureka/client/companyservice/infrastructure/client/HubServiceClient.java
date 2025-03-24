package com.fn.eureka.client.companyservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.companyservice.application.dto.HubResponseDto;

import feign.FeignException;

@FeignClient(name = "hub-service", path = "/api/hubs")
public interface HubServiceClient {

	// 허브 조회
	@GetMapping("/{hubId}")
	HubResponseDto readHub(
		@PathVariable("hubId") UUID hubId,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") String userId,
		@RequestHeader("X-User-Name") String userName
		// @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken
	);

	// 허브관리자ID로 허브ID 조회
	@GetMapping("/hub-id/{hubManagerId}")
	UUID readHubIdByHubManagerId(@PathVariable("hubManagerId") UUID hubManagerId);
}