package com.fn.eureka.client.hubservice.hub_stock.infrastructure;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;

public interface HubStockRepositoryCustom {
	Page<ReadHubStockResponse> searchHubStock(UUID hubId, Pageable pageable, UUID productId, int quantity,
		LocalDateTime startDateTime, LocalDateTime endDateTime);
}