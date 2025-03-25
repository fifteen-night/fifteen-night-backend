package com.fn.eureka.client.orderservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fn.common.global.config.FeignInterceptor;
import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.orderservice.application.dto.DeliveryRequestDto;
import com.fn.eureka.client.orderservice.application.dto.DeliveryResponseDto;

@FeignClient(name = "delivery-service", path = "/api/deliveries", configuration = FeignInterceptor.class)
public interface DeliveryServiceClient {

	// 배송 생성
	@PostMapping
	DeliveryResponseDto createDelivery(@RequestBody DeliveryRequestDto deliveryRequestDto);
}
