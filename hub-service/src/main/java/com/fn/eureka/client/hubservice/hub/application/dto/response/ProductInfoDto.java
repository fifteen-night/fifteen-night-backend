package com.fn.eureka.client.hubservice.hub.application.dto.response;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductInfoDto {
	private String message;
	private ProductData data;
	@Getter
	public static class ProductData {
		private UUID productId;
		private String productName;
		private UUID productCompanyId;
		private Integer productQuantity;
	}
}
