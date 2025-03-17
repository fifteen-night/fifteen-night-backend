package com.fn.eureka.client.product.infrastructure;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service", path = "/api/hubs")
public interface HubServiceClient {

	// TODO 요청 - 허브관리자ID로 허브ID 검색
	// @GetMapping("/{hubManagerId}")
	// UUID getHubByHubManagerId(@PathVariable("hubManagerId") UUID hubManagerId);
}
