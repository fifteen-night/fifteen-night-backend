package com.fn.eureka.client.productservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service", path = "/api/hubs")
public interface HubServiceClient {

	// 허브관리자ID로 허브ID 조회
	@GetMapping("/hub-id/{hubManagerId}")
	UUID readHubIdByHubManagerId(@PathVariable("hubManagerId") UUID hubManagerId);
}
