package com.fn.eureka.client.userservice.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.exception.CustomApiException;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.userservice.application.dto.auth.request.UserSignInRequestDto;
import com.fn.eureka.client.userservice.application.dto.auth.request.UserSignUpRequestDto;
import com.fn.eureka.client.userservice.application.dto.auth.response.UserSignInResponseDto;
import com.fn.eureka.client.userservice.application.dto.auth.response.UserSignUpResponseDto;
import com.fn.eureka.client.userservice.application.exception.AuthException;
import com.fn.eureka.client.userservice.domain.entity.User;
import com.fn.eureka.client.userservice.domain.repository.UserRepository;
import com.fn.eureka.client.userservice.libs.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	public CommonResponse<UserSignUpResponseDto> signUp(UserSignUpRequestDto requestDto) {
		// 이메일 중복 확인
		if (userRepository.findByUserEmail(requestDto.getUserEmail()).isPresent()) {
			throw new CustomApiException(AuthException.DUPLICATED_EMAIL);
		}

		// 닉네임 중복 확인
		if (userRepository.findByUserNickname(requestDto.getUserNickname()).isPresent()) {
			throw new CustomApiException(AuthException.DUPLICATED_NICKNAME);
		}

		// 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(requestDto.getUserPassword());

		// User 엔티티 생성 및 저장
		User user = User.of(
			requestDto.getUserName(),
			encodedPassword,
			requestDto.getUserNickname(),
			requestDto.getUserSlackId(),
			requestDto.getUserRole(),
			requestDto.getUserPhone(),
			requestDto.getUserEmail()
		);
		userRepository.save(user);

		// 회원가입 성공 응답 DTO
		UserSignUpResponseDto responseDto = UserSignUpResponseDto.builder()
			.userId(user.getUserId())
			.userName(user.getUserName())
			.userNickname(user.getUserNickname())
			.userEmail(user.getUserEmail())
			.userRole(user.getUserRole())
			.build();

		return new CommonResponse<>(SuccessCode.AUTH_SIGNUP, responseDto);
	}


	@Transactional(readOnly = true)
	public CommonResponse<UserSignInResponseDto> signIn(UserSignInRequestDto requestDto) {
		// 사용자명과 비밀번호가 일치하는지 확인
		User user = userRepository.findByUserName(requestDto.getUserName())
			.filter(u -> passwordEncoder.matches(requestDto.getUserPassword(), u.getUserPassword()))
			.orElseThrow(() -> new CustomApiException(AuthException.WRONG_USERNAME_OR_PASSWORD));

		// JWT Access/Refresh 토큰 생성
		String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getUserName(), user.getUserRole());
		String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

		// 로그인 성공 응답 DTO
		UserSignInResponseDto responseDto = UserSignInResponseDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();

		return new CommonResponse<>(SuccessCode.AUTH_LOGIN, responseDto);
	}
}
