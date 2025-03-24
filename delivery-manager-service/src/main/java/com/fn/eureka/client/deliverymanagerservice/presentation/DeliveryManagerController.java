package com.fn.eureka.client.deliverymanagerservice.presentation;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerCreateRequestDto;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerSearchCondition;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerUpdateRequestDto;
import com.fn.eureka.client.deliverymanagerservice.application.dto.response.DeliveryManagerGetResponseDto;
import com.fn.eureka.client.deliverymanagerservice.application.dto.response.DeliveryManagerUpdateResponseDto;
import com.fn.eureka.client.deliverymanagerservice.application.service.DeliveryManagerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery-managers")
public class DeliveryManagerController {

	private final DeliveryManagerService deliveryManagerService;

	// [CREATE] 배송 담당자 생성 - MASTER 또는 HUB_MANAGER
	@PostMapping
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<CommonResponse<DeliveryManagerGetResponseDto>> createDeliveryManager(
		@Valid @RequestBody DeliveryManagerCreateRequestDto requestDto) {

		CommonResponse<DeliveryManagerGetResponseDto> response = deliveryManagerService.createDeliveryManager(requestDto);
		return ResponseEntity.status(SuccessCode.DELIVERY_MANAGER_CREATED.getStatusCode()).body(response);
	}

	// [READ] 배송 담당자 단건 조회 - 모두 가능 (MASTER, HUB_MANAGER, 본인)
	@GetMapping("/{dmId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<CommonResponse<DeliveryManagerGetResponseDto>> getDeliveryManager(@PathVariable UUID dmId) {
		CommonResponse<DeliveryManagerGetResponseDto> response = deliveryManagerService.getDeliveryManager(dmId);
		return ResponseEntity.status(SuccessCode.DELIVERY_MANAGER_FOUND.getStatusCode()).body(response);
	}

	// [READ] 배송 담당자 목록 조회 - 모두 가능 (MASTER, HUB_MANAGER, 본인)
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<CommonResponse<Page<DeliveryManagerGetResponseDto>>> getDeliveryManagers(
		DeliveryManagerSearchCondition condition,
		Pageable pageable) {

		CommonResponse<Page<DeliveryManagerGetResponseDto>> response =
			deliveryManagerService.getDeliveryManagers(condition, pageable);

		return ResponseEntity
			.status(SuccessCode.DELIVERY_MANAGER_LIST_FOUND.getStatusCode())
			.body(response);
	}

	// [UPDATE] 배송 담당자 수정 - MASTER, HUB_MANAGER
	@PatchMapping("/{dmId}")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<CommonResponse<DeliveryManagerUpdateResponseDto>> updateDeliveryManager(
		@PathVariable UUID dmId,
		@Valid @RequestBody DeliveryManagerUpdateRequestDto requestDto) {

		CommonResponse<DeliveryManagerUpdateResponseDto> response =
			deliveryManagerService.updateDeliveryManager(dmId, requestDto);

		return ResponseEntity.status(SuccessCode.DELIVERY_MANAGER_UPDATED.getStatusCode()).body(response);
	}

	// [DELETE] 배송 담당자 삭제 - MASTER, HUB_MANAGER
	@DeleteMapping("/{dmId}")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<CommonResponse<Void>> deleteDeliveryManager(@PathVariable UUID dmId) {
		CommonResponse<Void> response = deliveryManagerService.deleteDeliveryManager(dmId);
		return ResponseEntity
			.status(SuccessCode.DELIVERY_MANAGER_DELETED.getStatusCode())
			.body(response);
	}

	// [ROUND-ROBIN] 허브 배송 담당자 배정 - MASTER만 가능
	@GetMapping("/assign/hub")
	public ResponseEntity<UUID> assignHubDeliveryManager() {
		UUID assignedId = deliveryManagerService.assignHubDeliveryManager();
		return ResponseEntity.ok(assignedId);
	}

	// [ROUND-ROBIN] 업체 배송 담당자 배정 - MASTER만 가능
	@GetMapping("/assign/company/{hubId}")
	public ResponseEntity<UUID> assignCompanyDeliveryManager(@PathVariable UUID hubId) {
		UUID assignedId = deliveryManagerService.assignCompanyDeliveryManager(hubId);
		return ResponseEntity.ok(assignedId);
	}
}

