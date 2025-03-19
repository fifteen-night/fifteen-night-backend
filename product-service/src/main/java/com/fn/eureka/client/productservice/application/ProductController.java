package com.fn.eureka.client.productservice.application;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.productservice.application.dto.ProductRequestDto;
import com.fn.eureka.client.productservice.application.dto.ProductResponseDto;
import com.fn.eureka.client.productservice.presentation.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	// TODO url을 못찾아 404 Not Found 나는데 원인을 모르겠음.
	// 상품 생성
	@PostMapping
	public ResponseEntity<ProductResponseDto> createProduct(
		@RequestBody ProductRequestDto requestDto,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") UUID userId
		) {
		ProductResponseDto response = productService.addProduct(requestDto, userRole, userId);
		return ResponseEntity.ok(response);
	}

	// 상품 조회
	@GetMapping("/{productId}")
	public ResponseEntity<ProductResponseDto> getProduct(@PathVariable UUID productId) {
		ProductResponseDto response = productService.findProduct(productId);
		return ResponseEntity.ok(response);
	}

	// 상품 리스트 조회 (전체, 허브별, 업체별)
	@GetMapping
	public ResponseEntity<Page<ProductResponseDto>> getProducts(
		@RequestParam(defaultValue = "whole", required = false) String type,
		@RequestParam(required = false) UUID id,
		@RequestParam(required = false) String keyword,
		@RequestParam(defaultValue = "0", required = false) int page,
		@RequestParam(defaultValue = "10", required = false) int size,
		@RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
		@RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy
	) {
		Page<ProductResponseDto> products = productService.findAllProductsByType(type, id, keyword, page, size, sortDirection, sortBy);
		return ResponseEntity.ok(products);
	}

	// 상품 수정
	@PatchMapping("/{productId}")
	public ResponseEntity<ProductResponseDto> updateProduct(
		@PathVariable UUID productId,
		@RequestBody Map<String, Object> updates,
		@RequestHeader("X-User-Role") String userRole) {
		ProductResponseDto response = productService.modifyProduct(productId, updates, userRole);
		return ResponseEntity.ok(response);
	}

}
