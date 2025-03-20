package com.fn.eureka.client.deliveryservice.presentation;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fn.common.global.dto.CommonPageResponse;
import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.deliveryservice.application.DeliveryService;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.request.CreateDeliveryRequestDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.request.UpdateDeliveryRequestDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.CreateDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.DeleteDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.GetAllDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.GetDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.UpdateDeliveryResponseDto;

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

	@GetMapping("/deliveries/{deliveryId}")
	@Operation(summary = "배송 단건 조회" , description = "배송 단건 조회는 'ALL' 가능")
	public ResponseEntity<CommonResponse<GetDeliveryResponseDto>> getDelivery(@PathVariable UUID deliveryId){

		GetDeliveryResponseDto deliveryResponseDto = deliveryService.searchOneDelivery(deliveryId);

		return ResponseEntity.ok()
			.body(new CommonResponse<>(SuccessCode.DELIVERY_SEARCH_ONE , deliveryResponseDto));
	}

	@GetMapping("/deliveries")
	@Operation(summary = "모든 배송 조회" , description = "모든 배송 조회는 'ALL' 가능")
	public ResponseEntity<CommonResponse<CommonPageResponse<GetAllDeliveryResponseDto>>> getAllDelivery(
		@PageableDefault(size = 10, sort = "createdAt" , direction = Sort.Direction.ASC) Pageable pageable
	){

		CommonPageResponse<GetAllDeliveryResponseDto> getAllDeliveryResponseDtoCommonPageResponse
			= deliveryService.searchAllDelivery(pageable);

		return ResponseEntity.ok()
			.body(new CommonResponse<>(SuccessCode.DELIVERY_SEARCH_ALL , getAllDeliveryResponseDtoCommonPageResponse));
	}

	@DeleteMapping("/deliveries/{deliveryId}")
	@Operation(summary = "배송 소프트 삭제" , description = "배송삭제는 'MASTER' , 'HUB_MANAGER' 만 가능")
	public ResponseEntity<CommonResponse<DeleteDeliveryResponseDto>> deleteDelivery(@PathVariable UUID deliveryId) {

		deliveryService.deleteDelivery(deliveryId);

		return ResponseEntity
			.ok()
			.body(new CommonResponse<>(SuccessCode.DELIVERY_DELETE , null));
	}

	@PatchMapping("/deliveries/{deliveryId}")
	@Operation(summary = "배송 수정하기" , description = "배송 수정은 'MASTER' , 'HUB_MANAGER' , 'DELIVERY_MANAGER' 만 가능")
	public ResponseEntity<CommonResponse<UpdateDeliveryResponseDto>> updateDelivery(
		@PathVariable UUID deliveryId,
		@RequestBody UpdateDeliveryRequestDto updateDeliveryRequestDto){

		UpdateDeliveryResponseDto updateDeliveryResponseDto = deliveryService.updateDelivery(deliveryId, updateDeliveryRequestDto);

		return ResponseEntity.ok()
			.body(new CommonResponse<>(SuccessCode.DELIVERY_UPDATE , updateDeliveryResponseDto));

	}
}
