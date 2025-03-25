package com.fn.eureka.client.orderservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fn.common.global.config.FeignInterceptor;
import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.orderservice.application.dto.DeliveriesUUIDDto;
import com.fn.eureka.client.orderservice.application.dto.DeliveryRequestDto;
import com.fn.eureka.client.orderservice.application.dto.DeliveryResponseDto;

import io.swagger.v3.oas.annotations.Operation;

@FeignClient(name = "delivery-service", path = "/api/deliveries", configuration = FeignInterceptor.class)
public interface DeliveryServiceClient {

	// 배송 생성
	@PostMapping
	DeliveryResponseDto createDelivery(@RequestBody DeliveryRequestDto deliveryRequestDto);

	@GetMapping("/allDeliveries/{deliveryManagerId}")
	DeliveriesUUIDDto queryAllDeliveries(@PathVariable("deliveryManagerId") UUID deliveryManagerId);
}
