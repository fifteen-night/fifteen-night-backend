package com.fn.eureka.client.deliveryservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.eureka.client.deliveryservice.presentation.dto.response.HubClientResponseDto;

@FeignClient(name = "hub-service", path = "/api/hubs")
public interface HubServiceClient {

	@GetMapping("/{hubId}")
	HubClientResponseDto findHub(@PathVariable UUID hubId);
}
