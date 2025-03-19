package com.fn.eureka.client.userservice.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonResponse;
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
			throw new RuntimeException(AuthException.DUPLICATED_EMAIL.getMessage());
		}
		// 닉네임 중복 확인
		if (userRepository.findByUserNickname(requestDto.getUserNickname()).isPresent()) {
			throw new RuntimeException(AuthException.DUPLICATED_NICKNAME.getMessage());
		}

		String encodedPassword = passwordEncoder.encode(requestDto.getUserPassword());

		User user = User.of(
			requestDto.getUserName(),
			encodedPassword,  // 암호화된 비밀번호 저장
			requestDto.getUserNickname(),
			requestDto.getUserSlackId(),
			requestDto.getUserRole(),
			requestDto.getUserPhone(),
			requestDto.getUserEmail()
		);

		userRepository.save(user);

		// DTO 반환
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
		// 유저네임으로 유저 조회 후, 비밀번호 검증까지 한 번에 처리
		User user = userRepository.findByUserName(requestDto.getUserName())
			.filter(u -> passwordEncoder.matches(requestDto.getUserPassword(), u.getUserPassword()))
			.orElseThrow(() -> new RuntimeException(AuthException.WRONG_USERNAME_OR_PASSWORD.getMessage()));

		// Access/Refresh Token 발급
		String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getUserName(), user.getUserRole());
		String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

		// DTO 반환
		UserSignInResponseDto responseDto = UserSignInResponseDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();

		return new CommonResponse<>(SuccessCode.AUTH_LOGIN, responseDto);
	}
}
