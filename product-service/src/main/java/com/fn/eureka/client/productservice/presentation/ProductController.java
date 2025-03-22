package com.fn.eureka.client.productservice.presentation;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.productservice.application.ProductService;
import com.fn.eureka.client.productservice.presentation.requeset.ProductRequestDto;
import com.fn.eureka.client.productservice.application.dto.ProductResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	// 상품 생성
	@PostMapping
	public ResponseEntity<CommonResponse<ProductResponseDto>> createProduct(
		@RequestBody ProductRequestDto productRequestDto,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") UUID userId
		) {
		ProductResponseDto productResponseDto = productService.addProduct(productRequestDto, userRole, userId);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/products").build().toUri();
		return ResponseEntity.created(location).body(new CommonResponse<>(SuccessCode.PRODUCT_CREATE, productResponseDto));
	}

	// 상품 조회
	@GetMapping("/{productId}")
	public ResponseEntity<CommonResponse<ProductResponseDto>> getProduct(@PathVariable("productId") UUID productId) {
		ProductResponseDto productResponseDto = productService.findProduct(productId);
		return ResponseEntity.ok().body(new CommonResponse<>(SuccessCode.ORDER_SEARCH_ONE, productResponseDto));
	}

	// 상품 리스트 조회 (전체, 허브별, 업체별)
	@GetMapping
	public ResponseEntity<CommonResponse<Page<ProductResponseDto>>> getProducts(
		@RequestParam(required = false) String keyword,
		@RequestParam(defaultValue = "0", required = false) int page,
		@RequestParam(defaultValue = "10", required = false) int size,
		@RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
		@RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") UUID userId
	) {
		Page<ProductResponseDto> products = productService.findAllProducts(keyword, page, size, sortDirection, sortBy, userRole, userId);
		return ResponseEntity.ok().body(new CommonResponse<>(SuccessCode.ORDER_SEARCH_ALL, products));
	}

	// 상품 수정
	@PatchMapping("/{productId}")
	public ResponseEntity<CommonResponse<ProductResponseDto>> updateProduct(
		@PathVariable("productId") UUID productId,
		@RequestBody Map<String, Object> updates,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") UUID userId) {
		ProductResponseDto productResponseDto = productService.modifyProduct(productId, updates, userRole, userId);
		return ResponseEntity.ok().body(new CommonResponse<>(SuccessCode.PRODUCT_UPDATE, productResponseDto));
	}

	// 상품 삭제
	@DeleteMapping("/{productId}")
	public ResponseEntity<CommonResponse> deleteProduct(
		@PathVariable("productId") UUID productId,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") UUID userId) {
		productService.removeProduct(productId, userRole, userId);
		return ResponseEntity.status(SuccessCode.PRODUCT_DELETE.getStatusCode()).body(new CommonResponse<>(SuccessCode.PRODUCT_DELETE, productId));
	}

}