package com.fn.eureka.client.hubservice.hub.application.dto.response;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ReadProductResponse {
	private UUID productId;
	private String productName;
	private UUID productCompanyId;
	private Integer productQuantity;
}