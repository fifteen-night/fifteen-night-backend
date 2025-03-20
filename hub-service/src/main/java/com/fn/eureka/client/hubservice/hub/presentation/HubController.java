package com.fn.eureka.client.hubservice.hub.presentation;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fn.common.global.dto.CommonPageResponse;
import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.hubservice.hub.application.HubService;
import com.fn.eureka.client.hubservice.hub.application.dto.request.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.request.UpdateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.response.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.UpdateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hubs")
public class HubController {
	private final HubService hubService;

	// 허브 관련 시작
	@PostMapping
	public ResponseEntity<CommonResponse<CreateHubResponse>> createHub(@RequestBody CreateHubRequest request) {

		CreateHubResponse response = hubService.createHub(request);

		return ResponseEntity.status(SuccessCode.HUB_CREATE.getStatusCode())
			.body(new CommonResponse<>(SuccessCode.HUB_CREATE, response));
	}

	@GetMapping("/{hubId}")
	public ResponseEntity<CommonResponse<ReadHubResponse>> readHub(@PathVariable("hubId") UUID hubId) {

		ReadHubResponse response = hubService.readHub(hubId);

		return ResponseEntity.status(SuccessCode.HUB_SEARCH.getStatusCode())
			.body(new CommonResponse<>(SuccessCode.HUB_SEARCH, response));
	}

	@GetMapping
	public ResponseEntity<CommonPageResponse<ReadHubResponse>> searchHub(Pageable pageable,
		@RequestParam(value = "hubName", required = false) String hubName) {

		Page<ReadHubResponse> response = hubService.searchHub(pageable, hubName);

		return ResponseEntity.status(SuccessCode.HUB_SEARCH.getStatusCode())
			.body(new CommonPageResponse<>(response));
	}

	@PatchMapping("/{hubId}")
	public ResponseEntity<Void> updateHub(@PathVariable("hubId") UUID hubId, @RequestBody UpdateHubRequest request) {

		hubService.updateHub(hubId, request);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{hubId}")
	public ResponseEntity<Void> deleteHub(@PathVariable("hubId") UUID hubId) {

		hubService.deleteHub(hubId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	// 허브 관련 끝

	// 허브 재고 관련 시작
	@PostMapping("/{hubId}/stock")
	public ResponseEntity<CommonResponse<CreateHubStockResponse>> createHubStock(@PathVariable("hubId") UUID hubId,
		@RequestBody CreateHubStockRequest request) {

		CreateHubStockResponse response = hubService.createHubStock(hubId, request);

		return ResponseEntity.status(SuccessCode.HUB_STOCK_CREATE.getStatusCode())
			.body(new CommonResponse<>(SuccessCode.HUB_STOCK_CREATE, response));
	}

	@GetMapping("/{hubId}/stock/{productId}")
	public ResponseEntity<CommonResponse<ReadHubStockResponse>> readHubStock(@PathVariable("hubId") UUID hubId,
		@PathVariable("productId") UUID productId) {

		ReadHubStockResponse response = hubService.readHubStock(hubId, productId);

		return ResponseEntity.status(SuccessCode.HUB_STOCK_SEARCH.getStatusCode())
			.body(new CommonResponse<>(SuccessCode.HUB_STOCK_SEARCH, response));
	}

	@GetMapping("/{hubId}/stock")
	public ResponseEntity<CommonPageResponse<ReadHubStockResponse>> searchHubStock(
		@PathVariable("hubId") UUID hubId,
		Pageable pageable,
		@RequestParam(value = "productId", required = false) UUID productId,
		@RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
		@RequestParam(value = "startDateTime", required = false, defaultValue = "1970-01-01T00:00:00") LocalDateTime startDateTime,
		@RequestParam(value = "endDateTime", required = false, defaultValue = "9999-01-01T00:00:00") LocalDateTime endDateTime
	) {
		Page<ReadHubStockResponse> page = hubService.searchHubStock(hubId, pageable, productId, quantity,
			startDateTime, endDateTime);

		return ResponseEntity.status(SuccessCode.HUB_STOCK_SEARCH.getStatusCode()).body(new CommonPageResponse<>(page));
	}

	@PatchMapping("/{hubId}/stock/{productId}")
	public ResponseEntity<Void> updateHubStock(
		@PathVariable("hubId") UUID hubId,
		@PathVariable("productId") UUID productId,
		@RequestBody UpdateHubStockRequest request
	) {
		hubService.updateHubStock(hubId, productId, request);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{hubId}/stock/{productId}")
	public ResponseEntity<Void> deleteHubStock(
		@PathVariable("hubId") UUID hubId,
		@PathVariable("productId") UUID productId
	) {
		hubService.deleteHubStock(hubId, productId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	// 허브 재고 관련 끝
}