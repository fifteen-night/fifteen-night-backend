package com.fn.eureka.client.orderservice.infrastructure;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fn.eureka.client.orderservice.presentation.dto.DeliveryRequestDto;
import com.fn.eureka.client.orderservice.presentation.dto.DeliveryResponseDto;

@FeignClient(name = "delivery-service", path = "/api/deliveries")
public interface DeliveryServiceClient {

	// TODO 메서드 이름 같은지 확인하기!!
	@PostMapping
	DeliveryResponseDto createdDelivery(@RequestBody DeliveryRequestDto deliveryRequestDto);
}
