package com.fn.eureka.client.userservice.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.userservice.application.dto.user.request.UserUpdateRequestDto;
import com.fn.eureka.client.userservice.application.dto.user.response.UserGetResponseDto;
import com.fn.eureka.client.userservice.application.dto.user.response.UserUpdateResponseDto;
import com.fn.eureka.client.userservice.application.service.UserService;
import com.fn.eureka.client.userservice.infrastructure.security.RequestUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	@GetMapping("/{userId}")
	public ResponseEntity<CommonResponse<UserGetResponseDto>> getUser(
		@PathVariable UUID userId) {

		CommonResponse<UserGetResponseDto> response = userService.getUser(userId);

		return ResponseEntity.status(SuccessCode.USER_FOUND.getStatusCode()).body(response);
	}

	@GetMapping
	public ResponseEntity<CommonResponse<Page<UserGetResponseDto>>> getUsers(
		@RequestParam(required = false) String keyword,
		Pageable pageable) {

		CommonResponse<Page<UserGetResponseDto>> response = userService.getUsers(keyword, pageable);

		return ResponseEntity.status(SuccessCode.USER_LIST_FOUND.getStatusCode()).body(response);
	}

	@PatchMapping("/{userId}")
	public ResponseEntity<CommonResponse<UserUpdateResponseDto>> updateUser(
		@PathVariable UUID userId,
		@Valid @RequestBody UserUpdateRequestDto requestDto) {

		CommonResponse<UserUpdateResponseDto> response = userService.updateUser(userId, requestDto);

		return ResponseEntity.status(SuccessCode.USER_UPDATED.getStatusCode()).body(response);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<CommonResponse<Void>> deleteUser(
		@PathVariable UUID userId
	) {
		CommonResponse<Void> response = userService.deleteUser(userId);
		return ResponseEntity.status(SuccessCode.USER_DELETED.getStatusCode()).body(response);
	}

}
