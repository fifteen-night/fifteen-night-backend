package com.fn.eureka.client.productservice.application.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRequestDto {

	private String productName;
	private UUID productCompanyId;
	private Integer productQuantity;

}
