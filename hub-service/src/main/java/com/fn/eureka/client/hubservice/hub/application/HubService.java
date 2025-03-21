package com.fn.eureka.client.hubservice.hub.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fn.eureka.client.hubservice.hub.application.dto.request.CheckHubManagerRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.request.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.request.UpdateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.response.CheckHubManagerResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;
import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.UpdateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;

public interface HubService {
	CreateHubResponse createHub(CreateHubRequest request);

	ReadHubResponse readHub(UUID hubId);

	Page<ReadHubResponse> searchHub(Pageable pageable, String hubName);

	void updateHub(UUID hubId, UpdateHubRequest request);

	void deleteHub(UUID hubId);

	Hub findHubById(UUID id);

	CheckHubManagerResponse checkHubManager(CheckHubManagerRequest request);

	CreateHubStockResponse createHubStock(UUID hubId, CreateHubStockRequest request);

	ReadHubStockResponse readHubStock(UUID HubId, UUID productId);

	Page<ReadHubStockResponse> searchHubStock(UUID hubId, Pageable pageable, UUID productId, int quantity,
		LocalDateTime startDateTime, LocalDateTime endDateTime);

	void updateHubStock(UUID hubId, UUID productId, UpdateHubStockRequest request);

	void deleteHubStock(UUID hubId, UUID productId);

	HubStock findHubStockByHubIdAndProductId(UUID hubId, UUID productId);
}