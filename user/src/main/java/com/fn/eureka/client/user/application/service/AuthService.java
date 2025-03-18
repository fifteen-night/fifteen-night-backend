package com.fn.eureka.client.user.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.eureka.client.user.application.dto.auth.request.UserSignInRequestDto;
import com.fn.eureka.client.user.application.dto.auth.request.UserSignUpRequestDto;
import com.fn.eureka.client.user.application.dto.auth.response.UserSignInResponseDto;
import com.fn.eureka.client.user.application.dto.auth.response.UserSignUpResponseDto;
import com.fn.eureka.client.user.application.exception.AuthException;
import com.fn.eureka.client.user.domain.entity.User;
import com.fn.eureka.client.user.domain.repository.UserRepository;
import com.fn.eureka.client.user.libs.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	public UserSignUpResponseDto signUp(UserSignUpRequestDto requestDto) {
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

		// 단순 DTO 반환
		return UserSignUpResponseDto.builder()
			.userId(user.getUserId())
			.userName(user.getUserName())
			.userNickname(user.getUserNickname())
			.userEmail(user.getUserEmail())
			.userRole(user.getUserRole())
			.build();
	}

	@Transactional(readOnly = true)
	public UserSignInResponseDto signIn(UserSignInRequestDto requestDto) {
		// 유저네임으로 유저 조회 후, 비밀번호 검증까지 한 번에 처리
		User user = userRepository.findByUserName(requestDto.getUserName())
			.filter(u -> passwordEncoder.matches(requestDto.getUserPassword(), u.getUserPassword()))
			.orElseThrow(() -> new RuntimeException(AuthException.WRONG_USERNAME_OR_PASSWORD.getMessage()));

		// Access/Refresh Token 발급
		String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getUserName(), user.getUserRole());
		String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

		// 로그인 성공 응답 DTO 반환
		return UserSignInResponseDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

}