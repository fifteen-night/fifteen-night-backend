package com.fn.eureka.client.slackservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.common.global.config.FeignInterceptor;
import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.slackservice.application.dto.response.UserInfoDto;

@FeignClient(name = "user-service", path = "/api/users", configuration = FeignInterceptor.class)
public interface UserServiceClient {

	@GetMapping("/{userId}")
	UserInfoDto readUser(@PathVariable("userId") UUID userId);
}