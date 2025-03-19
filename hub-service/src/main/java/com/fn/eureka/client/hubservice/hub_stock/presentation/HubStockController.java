package com.fn.eureka.client.hubservice.hub_stock.presentation;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.hubservice.hub_stock.application.HubStockService;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;

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
}