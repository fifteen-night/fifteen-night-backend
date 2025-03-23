package com.fn.eureka.client.hubservice.hub_stock.application.dto.response;

import java.util.UUID;

import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadProductResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReadHubStockResponse {
	private UUID hsId;
	private UUID hsProductId;
	private UUID hsHubId;
	private int hsQuantity;
	private ReadProductResponse detail;
}