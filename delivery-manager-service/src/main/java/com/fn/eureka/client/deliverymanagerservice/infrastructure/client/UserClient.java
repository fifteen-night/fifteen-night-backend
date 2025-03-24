package com.fn.eureka.client.deliverymanagerservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.common.global.config.FeignInterceptor;

@FeignClient(name = "user-service", path = "/api/users", configuration = FeignInterceptor.class)
public interface UserClient {

	@GetMapping("/internal/{userId}")
	boolean checkUserExists(@PathVariable("userId") UUID userId);
}

