package com.fn.eureka.client.hubservice.hub_stock.application.dto.response;

import java.util.UUID;

import lombok.Getter;

@Getter
public class CreateHubStockResponse {
	private final UUID hubStockId;

	public CreateHubStockResponse(UUID hubStockId) {
		this.hubStockId = hubStockId;
	}
}