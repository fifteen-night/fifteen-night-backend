package com.fn.eureka.client.hubservice.hub_stock.presentation;

import java.time.LocalDateTime;
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

import com.fn.common.global.dto.CommonPageResponse;
import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.hubservice.hub_stock.application.HubStockService;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub-stock")
public class HubStockController {

	private final HubStockService hubStockService;

	@PostMapping("/{hubId}")
	public ResponseEntity<CommonResponse<CreateHubStockResponse>> createHubStock(@PathVariable("hubId") UUID hubId,
		@RequestBody CreateHubStockRequest request) {

		CreateHubStockResponse response = hubStockService.createHubStock(hubId, request);

		return ResponseEntity.status(SuccessCode.HUB_STOCK_CREATE.getStatusCode())
			.body(new CommonResponse<>(SuccessCode.HUB_STOCK_CREATE, response));
	}

	@GetMapping("/{hubId}/{stockId}")
	public ResponseEntity<CommonResponse<ReadHubStockResponse>> readHubStock(@PathVariable("hubId") UUID hubId,
		@PathVariable("stockId") UUID stockId) {

		ReadHubStockResponse response = hubStockService.readHubStock(hubId, stockId);

		return ResponseEntity.status(SuccessCode.HUB_STOCK_SEARCH.getStatusCode())
			.body(new CommonResponse<>(SuccessCode.HUB_STOCK_SEARCH, response));
	}

	@GetMapping("/{hubId}")
	public ResponseEntity<CommonPageResponse<ReadHubStockResponse>> searchHubStock(
		@PathVariable("hubId") UUID hubId,
		Pageable pageable,
		@RequestParam(value = "productId", required = false) UUID productId,
		@RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
		@RequestParam(required = false, defaultValue = "1970-01-01") LocalDateTime startDateTime,
		@RequestParam(required = false, defaultValue = "9999-01-01") LocalDateTime endDateTime
	) {
		Page<ReadHubStockResponse> page = hubStockService.searchHubStock(hubId, pageable, productId, quantity,
			startDateTime,
			endDateTime);

		return ResponseEntity.status(SuccessCode.HUB_STOCK_SEARCH.getStatusCode()).body(new CommonPageResponse<>(page));
	}
}