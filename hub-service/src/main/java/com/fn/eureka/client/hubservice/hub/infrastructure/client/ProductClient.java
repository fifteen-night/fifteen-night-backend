package com.fn.eureka.client.hubservice.hub.infrastructure.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadProductResponse;

@FeignClient(name = "product-service", path = "/api/products")
public interface ProductClient {

	@GetMapping("/{productId}")
	CommonResponse<ReadProductResponse> readProduct(@PathVariable("productId") UUID productId);

	@PostMapping("/product-list")
	List<ReadProductResponse> readProductList(@RequestBody List<UUID> products);
}