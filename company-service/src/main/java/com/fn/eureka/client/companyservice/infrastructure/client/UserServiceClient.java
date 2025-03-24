package com.fn.eureka.client.companyservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.companyservice.application.dto.UserResponseDto;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {

	// 유저 조회
	@GetMapping("/{userId}")
	CommonResponse<UserResponseDto> getUser(@PathVariable("userId") UUID userId);
}
