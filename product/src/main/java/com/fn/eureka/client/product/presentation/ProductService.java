package com.fn.eureka.client.product.presentation;

import java.util.UUID;

import com.fn.eureka.client.product.application.dto.ProductRequestDto;
import com.fn.eureka.client.product.application.dto.ProductResponseDto;

public interface ProductService {
	ProductResponseDto addProduct(ProductRequestDto requestDto, String userRole, UUID userId);
}
