package com.fn.eureka.client.hubservice.hub_stock.application.dto.mapper;

import java.util.UUID;

import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;

public class HubStockMapper {
	public static HubStock toEntity(CreateHubStockRequest request, Hub hub) {
		return HubStock.builder()
			.hsHub(hub)
			.hsProductId(request.getProductId())
			.hsQuantity(request.getQuantity())
			.build();
	}

	public static CreateHubStockResponse toDto(HubStock hubStock) {
		return new CreateHubStockResponse(hubStock.getHsId());
	}

	public static ReadHubStockResponse toDto(HubStock hubStock, UUID stockId) {
		return ReadHubStockResponse.builder()
			.hsId(stockId)
			.hsProductId(hubStock.getHsProductId())
			.hsHubId(hubStock.getHsHub().getHubId())
			.hsQuantity(hubStock.getHsQuantity())
			.build();
	}
}