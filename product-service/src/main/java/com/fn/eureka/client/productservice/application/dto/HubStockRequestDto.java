package com.fn.eureka.client.productservice.application.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class HubStockRequestDto {
	// @NotNull
	private UUID productId;

	// @Min(value = 1, message = "최소 1 이상이어야 합니다.")
	private int quantity;
}