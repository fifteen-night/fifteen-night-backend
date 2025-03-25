package com.fn.eureka.client.orderservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.fn.common.global.config.FeignInterceptor;
import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.orderservice.application.dto.HubStockResponseDto;
import com.fn.eureka.client.orderservice.application.dto.HubStockUpdateDto;

import jakarta.validation.Valid;

@FeignClient(name = "hub-service", path = "/api/hubs", configuration = FeignInterceptor.class)
public interface HubServiceClient {

	// 허브 재고 조회
	@GetMapping("/{hubId}/stock/{productId}")
	HubStockResponseDto readHubStock(@PathVariable("hubId") UUID hubId, @PathVariable("productId") UUID productId);

	// 허브관리자ID로 허브ID 조회
	@GetMapping("/hub-id/{hubManagerId}")
	UUID readHubIdByHubManagerId(@PathVariable("hubManagerId") UUID hubManagerId);

	// 허브 재고 업데이트
	@PatchMapping("/{hubId}/stock/{productId}")
	HubStockResponseDto updateHubStock(
		@PathVariable("hubId") UUID hubId,
		@PathVariable("productId") UUID productId,
		@Valid @RequestBody HubStockUpdateDto hubStockUpdateDto
	);
}
