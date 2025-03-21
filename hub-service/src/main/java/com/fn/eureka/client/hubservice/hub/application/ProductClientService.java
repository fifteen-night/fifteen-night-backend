package com.fn.eureka.client.hubservice.hub.application;

import java.util.UUID;

public interface ProductClientService {
	boolean checkProductIfPresent(UUID productId);
}