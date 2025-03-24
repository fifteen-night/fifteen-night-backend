package com.fn.eureka.client.deliverymanagerservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service", path = "/api/hubs")
public interface HubClient {
	// 허브 존재 여부 확인
	@GetMapping("/{hubId}/check")
	boolean checkHub(@PathVariable("hubId") UUID hubId);
}
