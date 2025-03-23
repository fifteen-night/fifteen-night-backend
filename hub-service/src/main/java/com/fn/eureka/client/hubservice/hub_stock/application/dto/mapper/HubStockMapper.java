package com.fn.eureka.client.hubservice.hub_stock.application.dto.mapper;

import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadProductResponse;
import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.UpdateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;

public class HubStockMapper {
	public static HubStock toEntity(CreateHubStockRequest request, Hub hub) {
		return HubStock.builder()
			.hsHub(hub)
			.hsProductId(request.getProductId())
			.hsQuantity(request.getQuantity())
			.build();
	}

	public static CreateHubStockResponse toCreateDto(HubStock hubStock) {
		return new CreateHubStockResponse(hubStock.getHsId());
	}

	public static ReadHubStockResponse toReadDto(HubStock hubStock, ReadProductResponse detail) {
		return ReadHubStockResponse.builder()
			.hsId(hubStock.getHsId())
			.hsProductId(hubStock.getHsProductId())
			.hsHubId(hubStock.getHsHub().getHubId())
			.hsQuantity(hubStock.getHsQuantity())
			.detail(detail)
			.build();
	}

	public static UpdateHubStockResponse toUpdateDto(HubStock hubStock) {
		return UpdateHubStockResponse.builder()
			.hsId(hubStock.getHsId())
			.hsProductId(hubStock.getHsProductId())
			.hsHubId(hubStock.getHsHub().getHubId())
			.hsQuantity(hubStock.getHsQuantity())
			.build();
	}
}