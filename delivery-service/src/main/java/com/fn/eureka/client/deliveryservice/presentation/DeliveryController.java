package com.fn.eureka.client.deliveryservice.presentation;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.deliveryservice.application.DeliveryService;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.request.CreateDeliveryRequestDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.CreateDeliveryResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "배송", description = "배송 컨트롤러")
public class DeliveryController {

	private final DeliveryService deliveryService;

	@PostMapping("/deliveries")
	@Operation(summary = "배송 생성", description = "배송 생성은 'MASTER'만 가능")
	public ResponseEntity<CommonResponse<CreateDeliveryResponseDto>> createDelivery(
		@RequestBody @Validated CreateDeliveryRequestDto createDeliveryRequestDto) {

		log.info("request: {}", createDeliveryRequestDto.getOrderId());

		CreateDeliveryResponseDto createDeliveryResponseDto = deliveryService.createDelivery(createDeliveryRequestDto);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath()
			.path("/api/deliveries")
			.build()
			.toUri();

		return ResponseEntity
			.created(location)
			.body(new CommonResponse<>(SuccessCode.DELIVERY_CREATE, createDeliveryResponseDto));
	}
}
