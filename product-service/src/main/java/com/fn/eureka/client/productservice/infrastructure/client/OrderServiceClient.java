package com.fn.eureka.client.productservice.infrastructure.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service", path = "/api/orders")
public interface OrderServiceClient {

	@PostMapping("/order-products")
	List<UUID> readOrderProductIdListByDeliveryId(@RequestBody List<UUID> deliveries);
}
