package com.fn.eureka.client.hubservice.hub_stock.application.dto.mapper;

import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;

public class HubStockMapper {
	public static HubStock toEntity(CreateHubStockRequest request, Hub hub) {
		return HubStock.builder()
			.hsHubId(hub)
			.hsProductId(request.getProductId())
			.hsQuantity(request.getQuantity())
			.build();
	}

	public static CreateHubStockResponse toDto(HubStock hubStock) {
		return new CreateHubStockResponse(hubStock.getHsId());
	}
}