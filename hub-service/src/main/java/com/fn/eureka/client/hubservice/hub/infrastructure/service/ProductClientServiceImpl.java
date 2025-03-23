package com.fn.eureka.client.hubservice.hub.infrastructure.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fn.common.global.dto.CommonResponse;
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
		ReadProductResponse response = readProduct(productId);

		return response.getProductId().equals(productId);
	}

	@Override
	public ReadProductResponse readProduct(UUID productId) {
		CommonResponse<ReadProductResponse> response = productClient.readProduct(productId);

		return response.getData();
	}

	@Override
	public List<ReadProductResponse> readProductList(List<UUID> products) {
		return productClient.readProductList(products);
	}
}