package com.fn.eureka.client.orderservice.infrastructure;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fn.eureka.client.orderservice.presentation.dto.HubStockResponseDto;

@FeignClient(name = "hub-service", path = "/api/hubs")
public interface HubServiceClient {

	// TODO 메서드 이름 같은지 확인하기!!
	// 허브 재고 조회
	@GetMapping("/{hubId}")
	HubStockResponseDto searchHubStock(
		@PathVariable("hubId") UUID hubId,
		@RequestParam(value = "productId", required = false) UUID productId);
}
