package com.fn.eureka.client.hubservice.hub_stock.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;

public interface HubStockService {
	CreateHubStockResponse createHubStock(UUID hubId, CreateHubStockRequest request);

	ReadHubStockResponse readHubStock(UUID hubId, UUID stockId);

	Page<ReadHubStockResponse> searchHubStock(UUID hubId, Pageable pageable, UUID productId, int quantity,
		LocalDateTime startDateTime, LocalDateTime endDateTime);

	HubStock findHubStockById(UUID id);
}