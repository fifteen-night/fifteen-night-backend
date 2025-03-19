package com.fn.eureka.client.hubservice.hub_stock.application;

import java.util.UUID;

import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;

public interface HubStockService {
	CreateHubStockResponse createHubStock(UUID hubId, CreateHubStockRequest request);

	HubStock findHubStockById(UUID id);
}