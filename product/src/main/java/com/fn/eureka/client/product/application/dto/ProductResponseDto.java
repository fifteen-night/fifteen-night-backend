package com.fn.eureka.client.product.application.dto;

import java.util.UUID;

import com.fn.eureka.client.product.domain.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponseDto {

	private UUID productId;
	private String productName;
	private UUID productCompanyId;
	private Integer productQuantity;

	public ProductResponseDto(Product product) {
		this.productName = product.getProductName();
		this.productCompanyId = product.getProductCompanyId();
		this.productQuantity = product.getProductQuantity();
	}
}
