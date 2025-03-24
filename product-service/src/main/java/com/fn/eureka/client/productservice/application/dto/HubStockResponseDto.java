package com.fn.eureka.client.productservice.application.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HubStockResponseDto {
	private String message;
	private HubStockData data;

	@Getter
	public static class HubStockData {
		private UUID hubStockId;
	}
}