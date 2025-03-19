package com.fn.eureka.client.hubservice.hub_stock.application.dto.request;

import java.util.UUID;

import lombok.Getter;

@Getter
public class CreateHubStockRequest {
	private UUID productId;
	private int quantity;
}