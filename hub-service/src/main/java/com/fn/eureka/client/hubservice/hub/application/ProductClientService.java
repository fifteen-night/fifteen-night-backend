package com.fn.eureka.client.hubservice.hub.application;

import java.util.List;
import java.util.UUID;

import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadProductResponse;

public interface ProductClientService {
	boolean checkProductIfPresent(UUID productId);

	ReadProductResponse readProduct(UUID productId);

	List<ReadProductResponse> readProductList(List<UUID> products);
}