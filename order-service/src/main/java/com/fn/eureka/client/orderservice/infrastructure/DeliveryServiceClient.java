package com.fn.eureka.client.orderservice.infrastructure;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fn.eureka.client.orderservice.presentation.dto.DeliveryRequestDto;
import com.fn.eureka.client.orderservice.presentation.dto.DeliveryResponseDto;

@FeignClient(name = "delivery-service", url = "http://localhost:19097/api/deliveries")
public interface DeliveryServiceClient {

	@PostMapping
	DeliveryResponseDto createdDelivery(@RequestBody DeliveryRequestDto deliveryRequestDto);
}
