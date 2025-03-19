package com.fn.eureka.client.orderservice.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fn.eureka.client.orderservice.application.OrderService;
import com.fn.eureka.client.orderservice.presentation.dto.OrderRequestDto;
import com.fn.eureka.client.orderservice.presentation.dto.OrderResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto,
		@RequestHeader("X-User-Role") String userRole) {
		OrderResponseDto response = orderService.addOrder(requestDto, userRole);
		return ResponseEntity.ok(response);
	}
}
