package com.fn.eureka.client.hubservice.hub_stock.application.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateHubStockResponse {
	private final UUID hubStockId;
}