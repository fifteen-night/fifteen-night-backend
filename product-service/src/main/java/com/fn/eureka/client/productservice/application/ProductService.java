package com.fn.eureka.client.productservice.application;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.productservice.presentation.requeset.ProductRequestDto;
import com.fn.eureka.client.productservice.application.dto.ProductResponseDto;

public interface ProductService {
	ProductResponseDto addProduct(ProductRequestDto requestDto, String userRole, UUID userId);

	ProductResponseDto findProduct(UUID productId);

	Page<ProductResponseDto> findAllProducts(String keyword, int page, int size, Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy, String userRole, UUID userId);

	ProductResponseDto modifyProduct(UUID productId, Map<String, Object> updates, String userRole, UUID userId);

	void removeProduct(UUID productId, String userRole, UUID userId);
}
