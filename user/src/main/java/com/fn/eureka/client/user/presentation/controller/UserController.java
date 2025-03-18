package com.fn.eureka.client.user.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fn.eureka.client.user.application.dto.ApiResponseDto;
import com.fn.eureka.client.user.application.dto.user.response.UserGetResponseDto;
import com.fn.eureka.client.user.application.service.UserService;
import com.fn.eureka.client.user.infrastructure.security.RequestUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponseDto<UserGetResponseDto>> getUser(@PathVariable UUID userId, @AuthenticationPrincipal RequestUserDetails userDetails) {
		UserGetResponseDto responseDto = userService.getUser(userId, userDetails);

		return ResponseEntity.ok(ApiResponseDto.success(responseDto));
	}

	@GetMapping
	public ResponseEntity<ApiResponseDto<Page<UserGetResponseDto>>> getUsers(
		@RequestParam(required = false) String keyword, Pageable pageable, @AuthenticationPrincipal RequestUserDetails userDetails) {
		Page<UserGetResponseDto> responseDto = userService.getUsers(keyword, pageable, userDetails);

		return ResponseEntity.ok(ApiResponseDto.success(responseDto));
	}

}
