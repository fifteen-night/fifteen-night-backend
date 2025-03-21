package com.fn.eureka.client.orderservice.presentation;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.orderservice.application.OrderService;
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

	// 주문 생성
	@PostMapping
	public ResponseEntity<CommonResponse<OrderResponseDto>> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
		OrderResponseDto orderResponseDto = orderService.addOrder(orderRequestDto);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/orders").build().toUri();
		return ResponseEntity.created(location).body(new CommonResponse<>(SuccessCode.ORDER_CREATE, orderResponseDto));
	}

	// 주문 조회
	@GetMapping("/{orderId}")
	public ResponseEntity<CommonResponse<OrderResponseDto>> readOrder(@PathVariable("orderId")UUID orderId) {
		OrderResponseDto orderResponseDto = orderService.findOrder(orderId);
		return ResponseEntity.ok().body(new CommonResponse<>(SuccessCode.ORDER_SEARCH_ONE, orderResponseDto));
	}
}
