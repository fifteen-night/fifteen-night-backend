package com.fn.eureka.client.hubservice.hub.infrastructure.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fn.eureka.client.hubservice.hub.application.ProductClientService;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadProductResponse;
import com.fn.eureka.client.hubservice.hub.infrastructure.client.ProductClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductClientServiceImpl implements ProductClientService {

	private final ProductClient productClient;

	@Override
	public boolean checkProductIfPresent(UUID productId) {
		ReadProductResponse response = productClient.readProduct(productId);

		return response.getProductId().equals(productId);
	}
}