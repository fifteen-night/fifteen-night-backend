package com.fn.eureka.client.hubservice.hub_stock.application.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadHubStockResponse {
	private UUID hsId;
	private UUID hsProductId;
	private UUID hsHubId;
	private int hsQuantity;
}