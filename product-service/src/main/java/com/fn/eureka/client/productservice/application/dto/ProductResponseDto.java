package com.fn.eureka.client.productservice.application.dto;

import java.util.UUID;

import com.fn.eureka.client.productservice.domain.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {

	private UUID productId;
	private String productName;
	private UUID productCompanyId;
	private Integer productQuantity;

	public static ProductResponseDto from(Product product) {
		return ProductResponseDto.builder()
			.productId(product.getProductId())
			.productName(product.getProductName())
			.productCompanyId(product.getProductCompanyId())
			.productQuantity(product.getProductQuantity())
			.build();
	}
}
