package com.fn.eureka.client.orderservice.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fn.eureka.client.orderservice.application.OrderService;
import com.fn.eureka.client.orderservice.infrastructure.CompanyServiceClient;
import com.fn.eureka.client.orderservice.presentation.dto.OrderRequestDto;
import com.fn.eureka.client.orderservice.presentation.dto.OrderResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto) {
		log.info("Create order request: {}", requestDto);
		OrderResponseDto response = orderService.addOrder(requestDto);
		return ResponseEntity.ok(response);
	}
}
