package com.fn.eureka.client.productservice.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.productservice.application.dto.ProductResponseDto;

public interface ProductQueryRepository {
	Page<ProductResponseDto> findProductsByType(String type, UUID id, String keyword, Pageable pageable, Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy);
}
