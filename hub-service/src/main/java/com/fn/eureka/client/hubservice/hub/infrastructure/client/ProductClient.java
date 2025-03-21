package com.fn.eureka.client.hubservice.hub.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadProductResponse;

@FeignClient(name = "product-service", path = "/api/products")
public interface ProductClient {

	@GetMapping("/{productId}")
	ReadProductResponse readProduct(@PathVariable("productId") UUID productId);
}