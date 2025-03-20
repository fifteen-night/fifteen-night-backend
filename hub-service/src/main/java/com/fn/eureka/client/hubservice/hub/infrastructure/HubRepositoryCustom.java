package com.fn.eureka.client.hubservice.hub.infrastructure;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;

public interface HubRepositoryCustom {

	Page<ReadHubResponse> searchHubs(Pageable pageable, String hubName);

	ReadHubStockResponse readHubStock(UUID hubId, UUID productId);

	Page<ReadHubStockResponse> searchHubStock(UUID hubId, Pageable pageable, UUID productId, int quantity,
		LocalDateTime startDateTime, LocalDateTime endDateTime);
}