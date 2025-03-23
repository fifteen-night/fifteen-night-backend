package com.fn.eureka.client.orderservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fn.eureka.client.orderservice.application.dto.HubStockResponseDto;

@FeignClient(name = "hub-service", path = "/api/hubs")
public interface HubServiceClient {

	// 허브 재고 조회
	@GetMapping("/{hubId}/stock/{productId}")
	HubStockResponseDto readHubStock(@PathVariable("hubId") UUID hubId, @PathVariable("productId") UUID productId);

	// TODO 만들어주세요 SELECT h.hubId FROM Hub h WHERE h.hubManagerId = :hubManagerId
	// 허브관리자ID로 허브ID 조회
	@GetMapping("/hub-id/{hubManagerId}")
	UUID readHubIdByHubManagerId(@PathVariable("hubManagerId") UUID hubManagerId);
}
