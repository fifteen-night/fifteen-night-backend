package com.fn.eureka.client.productservice.infrastructure.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "delivery-service", path = "/api/deliveries")
public interface DeliveryServiceClient {

	// TODO 메서드 이름 같은지 확인하기!!
	@GetMapping("/delivery-manager/{deliveryManagerId}")
	List<UUID> readDeliveriesByDeliveryManagerId(@PathVariable("deliveryManagerId") UUID deliveryManagerId);

}
