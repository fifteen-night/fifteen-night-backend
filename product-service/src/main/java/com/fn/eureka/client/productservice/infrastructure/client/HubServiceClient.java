package com.fn.eureka.client.productservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.productservice.application.dto.HubStockRequestDto;
import com.fn.eureka.client.productservice.application.dto.HubStockResponseDto;

@FeignClient(name = "hub-service", path = "/api/hubs")
public interface HubServiceClient {

	// 허브관리자ID로 허브ID 조회
	@GetMapping("/hub-id/{hubManagerId}")
	UUID readHubIdByHubManagerId(@PathVariable("hubManagerId") UUID hubManagerId);

	// 허브 재고 생성
	@PostMapping("/{hubId}/stock")
	CommonResponse<HubStockResponseDto> createHubStock(@PathVariable("hubId") UUID hubId, @RequestBody HubStockRequestDto hubStockRequestDto);
}
