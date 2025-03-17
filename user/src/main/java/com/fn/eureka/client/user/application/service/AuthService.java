package com.fn.eureka.client.user.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.eureka.client.user.application.dto.auth.request.UserSignUpRequestDto;
import com.fn.eureka.client.user.application.dto.auth.response.UserSignUpResponseDto;
import com.fn.eureka.client.user.application.exception.ErrorMessage;
import com.fn.eureka.client.user.domain.entity.User;
import com.fn.eureka.client.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserSignUpResponseDto signUp(UserSignUpRequestDto requestDto) {
		// 이메일 중복 확인
		if (userRepository.findByUserEmail(requestDto.getUserEmail()).isPresent()) {
			throw new IllegalArgumentException(ErrorMessage.DUPLICATED_EMAIL.getMessage());
		}

		// 유저이름 중복 확인
		if (userRepository.findByUserNickname(requestDto.getUserNickname()).isPresent()) {
			throw new IllegalArgumentException(ErrorMessage.DUPLICATED_NICKNAME.getMessage());
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
}