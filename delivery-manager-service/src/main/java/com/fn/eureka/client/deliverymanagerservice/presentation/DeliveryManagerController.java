package com.fn.eureka.client.deliverymanagerservice.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerCreateRequestDto;
import com.fn.eureka.client.deliverymanagerservice.application.dto.response.DeliveryManagerCreateResponseDto;
import com.fn.eureka.client.deliverymanagerservice.application.service.DeliveryManagerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery-managers")
public class DeliveryManagerController {

	private final DeliveryManagerService deliveryManagerService;

	@PostMapping
	public ResponseEntity<CommonResponse<DeliveryManagerCreateResponseDto>> createDeliveryManager(
		@Valid @RequestBody DeliveryManagerCreateRequestDto requestDto) {

		CommonResponse<DeliveryManagerCreateResponseDto> response = deliveryManagerService.createDeliveryManager(requestDto);

		return ResponseEntity.status(SuccessCode.DELIVERY_MANAGER_CREATED.getStatusCode()).body(response);
	}

}
