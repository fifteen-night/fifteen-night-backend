package com.fn.eureka.client.user.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.eureka.client.user.application.dto.user.response.UserGetResponseDto;
import com.fn.eureka.client.user.application.exception.UserException;
import com.fn.eureka.client.user.domain.entity.User;
import com.fn.eureka.client.user.domain.repository.UserRepository;
import com.fn.eureka.client.user.infrastructure.security.RequestUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public UserGetResponseDto getUser(UUID userId, RequestUserDetails userDetails) {
		if (userDetails == null || userDetails.getUserId() == null) {
			throw new RuntimeException(UserException.INVALID_AUTHENTICATION.getMessage());
		}

		// userDetails 로부터 UUID 변환
		UUID requestUserId = UUID.fromString(userDetails.getUserId());

		// 권한 체크 (MASTER 권한 여부 확인)
		boolean isMaster = userDetails.getAuthorities().stream()
			.anyMatch(auth -> auth.getAuthority().equals("ROLE_MASTER"));

		// 조회 대상 유저 정보 가져오기
		User targetUser = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException(UserException.USER_NOT_FOUND.getMessage()));

		// MASTER가 아니면 본인 정보만 조회 가능
		if (!isMaster && !requestUserId.equals(userId)) {
			throw new RuntimeException(UserException.ACCESS_DENIED.getMessage());
		}

		// 조회 성공 시 DTO로 변환
		return UserGetResponseDto.builder()
			.userId(targetUser.getUserId())
			.userName(targetUser.getUserName())
			.userNickname(targetUser.getUserNickname())
			.userEmail(targetUser.getUserEmail())
			.userRole(targetUser.getUserRole().name())
			.userPhone(targetUser.getUserPhone())
			.userSlackId(targetUser.getUserSlackId())
			.build();
	}

	@Transactional(readOnly = true)
	public Page<UserGetResponseDto> getUsers(String keyword, Pageable pageable, RequestUserDetails userDetails) {
		if (userDetails == null || userDetails.getUserId() == null) {
			throw new RuntimeException(UserException.INVALID_AUTHENTICATION.getMessage());
		}

		boolean isMaster = userDetails.getAuthorities().stream()
			.anyMatch(auth -> auth.getAuthority().equals("ROLE_MASTER"));

		if (!isMaster) {
			throw new RuntimeException(UserException.ACCESS_DENIED.getMessage());
		}

		Page<User> users = userRepository.findByKeyword(keyword, pageable);

		return users.map(user -> UserGetResponseDto.builder()
			.userId(user.getUserId())
			.userName(user.getUserName())
			.userNickname(user.getUserNickname())
			.userEmail(user.getUserEmail())
			.userRole(user.getUserRole().name())
			.userPhone(user.getUserPhone())
			.userSlackId(user.getUserSlackId())
			.build());
	}
}
