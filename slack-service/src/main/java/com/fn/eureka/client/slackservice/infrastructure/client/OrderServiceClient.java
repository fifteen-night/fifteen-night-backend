package com.fn.eureka.client.slackservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.common.global.config.FeignInterceptor;
import com.fn.eureka.client.slackservice.application.dto.response.OrderInfoDto;

@FeignClient(name = "order-service", path = "/api/orders", configuration = FeignInterceptor.class)
public interface OrderServiceClient {

	@GetMapping("/{orderId}")
	OrderInfoDto readOrder(@PathVariable("orderId") UUID orderId);
}