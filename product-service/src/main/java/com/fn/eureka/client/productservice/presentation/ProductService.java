package com.fn.eureka.client.productservice.presentation;

import java.util.UUID;

import com.fn.eureka.client.productservice.application.dto.ProductRequestDto;
import com.fn.eureka.client.productservice.application.dto.ProductResponseDto;

public interface ProductService {
	ProductResponseDto addProduct(ProductRequestDto requestDto, String userRole, UUID userId);

	ProductResponseDto findProduct(UUID productId);
}
