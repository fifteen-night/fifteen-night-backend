package com.fn.eureka.client.user.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fn.eureka.client.user.application.dto.ApiResponseDto;
import com.fn.eureka.client.user.application.dto.auth.request.UserSignUpRequestDto;
import com.fn.eureka.client.user.application.dto.auth.response.UserSignUpResponseDto;
import com.fn.eureka.client.user.application.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signUp")
	public ResponseEntity<ApiResponseDto<UserSignUpResponseDto>> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
		UserSignUpResponseDto responseDto = authService.signUp(requestDto);

		return ResponseEntity.ok(ApiResponseDto.success(responseDto));
	}
}

