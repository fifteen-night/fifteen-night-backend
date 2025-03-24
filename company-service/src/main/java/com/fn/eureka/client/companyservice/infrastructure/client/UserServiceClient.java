package com.fn.eureka.client.companyservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.fn.eureka.client.companyservice.application.dto.UserResponseDto;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {

	// 유저 조회
	@GetMapping("/{userId}")
	UserResponseDto getUser(
		@PathVariable("userId") UUID userId,
		@RequestHeader("X-User-Role") String userRole,
		@RequestHeader("X-User-Id") String xuserId,
		@RequestHeader("X-User-Name") String userName);
}
