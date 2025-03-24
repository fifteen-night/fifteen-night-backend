package com.fn.eureka.client.userservice.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.userservice.application.dto.user.request.UserUpdateRequestDto;
import com.fn.eureka.client.userservice.application.dto.user.response.UserGetResponseDto;
import com.fn.eureka.client.userservice.application.dto.user.response.UserUpdateResponseDto;

public interface UserService {

	CommonResponse<UserGetResponseDto> getUser(UUID userId);

	CommonResponse<Page<UserGetResponseDto>> getUsers(String keyword, Pageable pageable);

	CommonResponse<UserUpdateResponseDto> updateUser(UUID userId, UserUpdateRequestDto requestDto);

	CommonResponse<Void> deleteUser(UUID userId);

	boolean checkUserExists(UUID userId);
}
