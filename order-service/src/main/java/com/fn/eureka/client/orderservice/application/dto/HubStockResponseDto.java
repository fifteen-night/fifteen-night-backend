package com.fn.eureka.client.orderservice.application.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HubStockResponseDto {
	private UUID hubStockId;
	private UUID hsId;
	private UUID hsProductId;
	private UUID hsHubId;
	private int hsQuantity;
}
