package com.fn.eureka.client.deliverymanagerservice.presentation;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerCreateRequestDto;
import com.fn.eureka.client.deliverymanagerservice.application.dto.response.DeliveryManagerGetResponseDto;
import com.fn.eureka.client.deliverymanagerservice.application.service.DeliveryManagerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery-managers")
public class DeliveryManagerController {

	private final DeliveryManagerService deliveryManagerService;

	@PostMapping
	public ResponseEntity<CommonResponse<DeliveryManagerGetResponseDto>> createDeliveryManager(
		@Valid @RequestBody DeliveryManagerCreateRequestDto requestDto) {

		CommonResponse<DeliveryManagerGetResponseDto> response = deliveryManagerService.createDeliveryManager(requestDto);
		return ResponseEntity.status(SuccessCode.DELIVERY_MANAGER_CREATED.getStatusCode()).body(response);
	}

	@GetMapping("/{deliveryManagerId}")
	public ResponseEntity<CommonResponse<DeliveryManagerGetResponseDto>> getDeliveryManager(
		@PathVariable UUID deliveryManagerId) {

		CommonResponse<DeliveryManagerGetResponseDto> response = deliveryManagerService.getDeliveryManager(deliveryManagerId);
		return ResponseEntity.status(SuccessCode.DELIVERY_MANAGER_FOUND.getStatusCode()).body(response);
	}

	@GetMapping
	public ResponseEntity<CommonResponse<Page<DeliveryManagerGetResponseDto>>> getDeliveryManagers(
		@RequestParam(required = false) String keyword,
		Pageable pageable) {

		CommonResponse<Page<DeliveryManagerGetResponseDto>> response =
			deliveryManagerService.getDeliveryManagers(keyword, pageable);

		return ResponseEntity
			.status(SuccessCode.DELIVERY_MANAGER_LIST_FOUND.getStatusCode())
			.body(response);
	}

}
