package com.fn.eureka.client.userservice.application.service;

import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.userservice.application.dto.auth.request.UserSignInRequestDto;
import com.fn.eureka.client.userservice.application.dto.auth.request.UserSignUpRequestDto;
import com.fn.eureka.client.userservice.application.dto.auth.response.UserSignInResponseDto;
import com.fn.eureka.client.userservice.application.dto.auth.response.UserSignUpResponseDto;

public interface AuthService {

	CommonResponse<UserSignUpResponseDto> signUp(UserSignUpRequestDto requestDto);

	CommonResponse<UserSignInResponseDto> signIn(UserSignInRequestDto requestDto);
}
