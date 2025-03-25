package com.fn.eureka.client.slackservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.common.global.config.FeignInterceptor;
import com.fn.eureka.client.slackservice.application.dto.response.ProductInfoDto;

@FeignClient(name = "product-service", path = "/api/products", configuration = FeignInterceptor.class)
public interface ProductServiceClient {

	@GetMapping("/{productId}")
	ProductInfoDto readProduct(@PathVariable("productId") UUID productId);

}
