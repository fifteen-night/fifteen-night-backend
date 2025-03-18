package com.fn.eureka.client.user.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.user.application.dto.auth.request.UserSignInRequestDto;
import com.fn.eureka.client.user.application.dto.auth.request.UserSignUpRequestDto;
import com.fn.eureka.client.user.application.dto.auth.response.UserSignInResponseDto;
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
	public ResponseEntity<CommonResponse<UserSignUpResponseDto>> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
		CommonResponse<UserSignUpResponseDto> response = authService.signUp(requestDto);

		return ResponseEntity.status(SuccessCode.AUTH_SIGNUP.getStatusCode()).body(response);
	}

	@PostMapping("/signIn")
	public ResponseEntity<CommonResponse<UserSignInResponseDto>> signIn(@RequestBody UserSignInRequestDto requestDto) {
		CommonResponse<UserSignInResponseDto> response = authService.signIn(requestDto);

		return ResponseEntity.status(SuccessCode.AUTH_LOGIN.getStatusCode()).body(response);
	}
}
