package com.fn.eureka.client.orderservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.eureka.client.orderservice.application.dto.UserResponseDto;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {

	// 유저 조회
	@GetMapping("/users/{userId}")
	UserResponseDto getUser(@PathVariable("userId") UUID userId);

}
