package com.fn.eureka.client.hubservice.hub.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadUserResponse;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserClient {

	@GetMapping("/{userId}")
	CommonResponse<ReadUserResponse> readUser(@PathVariable("userId") UUID userId);
}