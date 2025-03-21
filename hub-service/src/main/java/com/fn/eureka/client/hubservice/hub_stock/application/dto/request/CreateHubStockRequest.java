package com.fn.eureka.client.hubservice.hub_stock.application.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateHubStockRequest {
	@NotNull
	private UUID productId;

	@Min(value = 1, message = "최소 1 이상이어야 합니다.")
	private int quantity;
}