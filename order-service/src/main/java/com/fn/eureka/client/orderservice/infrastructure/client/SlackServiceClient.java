package com.fn.eureka.client.orderservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fn.eureka.client.orderservice.application.dto.DeliveryResponseDto;
import com.fn.eureka.client.orderservice.application.dto.GeminiResponseDto;

@FeignClient(name = "slack-service", path = "/api/slack")
public interface SlackServiceClient {
	@PostMapping("/ai")
	GeminiResponseDto sendAiMessage(@RequestBody DeliveryResponseDto deliveryResponseDto);
}
