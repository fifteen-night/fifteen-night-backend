package com.fn.eureka.client.deliveryservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.common.global.config.FeignInterceptor;

@FeignClient(name = "delivery-manager-service", path = "/api/delivery-managers", configuration = FeignInterceptor.class)
public interface DeliveryManagerServiceClient {

	@GetMapping("/assign/company/{hubId}")
	UUID findCompanyDeliver(@PathVariable("hubId") UUID hubId);

	@GetMapping("/assign/hub")
	UUID findHubDeliver();
}