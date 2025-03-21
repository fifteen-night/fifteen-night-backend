package com.fn.eureka.client.deliverymanagerservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserFeignClient {

	@GetMapping("/internal/{userId}")
	boolean checkUserExists(@PathVariable("userId") UUID userId);
}

