package com.fn.eureka.client.orderservice.presentation;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.orderservice.domain.service.OrderService;
import com.fn.eureka.client.orderservice.presentation.request.OrderRequestDto;
import com.fn.eureka.client.orderservice.application.dto.OrderResponseDto;

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

	// 주문 리스트 조회(전체, 허브별, 배송담당자별, 업체별) + 검색
	@GetMapping
	public ResponseEntity<CommonResponse<Page<OrderResponseDto>>> readOrders(
		@RequestParam(required = false) String keyword,
		@RequestParam(defaultValue = "0", required = false) int page,
		@RequestParam(defaultValue = "10", required = false) int size,
		@RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
		@RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") UUID userId
	) {
		Page<OrderResponseDto> orders = orderService.findAllOrdersByRole(keyword, page, size, sortDirection, sortBy, userRole, userId);
		return ResponseEntity.ok().body(new CommonResponse<>(SuccessCode.ORDER_SEARCH_ALL, orders));
	}

	// 주문 수정
	@PatchMapping("/{orderId}")
	public ResponseEntity<CommonResponse<OrderResponseDto>> updateOrder(
		@PathVariable("orderId") UUID orderId,
		@RequestBody Map<String, Object> updates,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") UUID userId) {
		OrderResponseDto orderResponseDto = orderService.modifyOrder(orderId, updates, userRole, userId);
		return ResponseEntity.ok().body(new CommonResponse<>(SuccessCode.ORDER_UPDATE, orderResponseDto));
	}

	// 주문 삭제
	@DeleteMapping("/{orderId}")
	public ResponseEntity<CommonResponse> deleteOrder(
		@PathVariable("orderId") UUID orderId,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") UUID userId) {
		orderService.removeOrder(orderId, userRole, userId);
		return ResponseEntity.status(SuccessCode.ORDER_DELETE.getStatusCode()).body(new CommonResponse<>(SuccessCode.ORDER_DELETE, orderId));
	}
}
