package com.fn.eureka.client.userservice.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.exception.CustomApiException;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.userservice.application.dto.user.request.UserUpdateRequestDto;
import com.fn.eureka.client.userservice.application.dto.user.response.UserGetResponseDto;
import com.fn.eureka.client.userservice.application.dto.user.response.UserUpdateResponseDto;
import com.fn.eureka.client.userservice.application.exception.UserException;
import com.fn.eureka.client.userservice.domain.entity.User;
import com.fn.eureka.client.userservice.domain.repository.UserRepository;
import com.fn.eureka.client.userservice.infrastructure.security.RequestUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public CommonResponse<UserGetResponseDto> getUser(UUID userId) {
		RequestUserDetails userDetails = getAuthenticatedUser();
		validateAuthentication(userDetails);

		UUID requestUserId = UUID.fromString(userDetails.getUserId());
		boolean isMaster = hasMasterRole(userDetails);

		// 조회 대상 유저 정보 가져오기
		User targetUser = userRepository.findById(userId)
			.orElseThrow(() -> new CustomApiException(UserException.USER_NOT_FOUND));

		// MASTER가 아니면 본인 정보만 조회 가능
		if (!isMaster && !requestUserId.equals(userId)) {
			throw new CustomApiException(UserException.ACCESS_DENIED);
		}

		// 조회 성공 시 DTO 변환
		UserGetResponseDto responseDto = UserGetResponseDto.builder()
			.userId(targetUser.getUserId())
			.userName(targetUser.getUserName())
			.userNickname(targetUser.getUserNickname())
			.userEmail(targetUser.getUserEmail())
			.userRole(targetUser.getUserRole().name())
			.userPhone(targetUser.getUserPhone())
			.userSlackId(targetUser.getUserSlackId())
			.build();

		return new CommonResponse<>(SuccessCode.USER_FOUND, responseDto);
	}

	@Transactional(readOnly = true)
	public CommonResponse<Page<UserGetResponseDto>> getUsers(String keyword, Pageable pageable) {
		RequestUserDetails userDetails = getAuthenticatedUser();
		validateAuthentication(userDetails);

		if (!hasMasterRole(userDetails)) {
			throw new CustomApiException(UserException.ACCESS_DENIED);
		}

		Page<User> users = userRepository.findByKeyword(keyword, pageable);

		Page<UserGetResponseDto> responseDtoPage = users.map(user -> UserGetResponseDto.builder()
			.userId(user.getUserId())
			.userName(user.getUserName())
			.userNickname(user.getUserNickname())
			.userEmail(user.getUserEmail())
			.userRole(user.getUserRole().name())
			.userPhone(user.getUserPhone())
			.userSlackId(user.getUserSlackId())
			.build());

		return new CommonResponse<>(SuccessCode.USER_LIST_FOUND, responseDtoPage);
	}

	@Transactional
	public CommonResponse<UserUpdateResponseDto> updateUser(UUID userId, UserUpdateRequestDto requestDto) {
		RequestUserDetails userDetails = getAuthenticatedUser();
		validateAuthentication(userDetails);

		if (!hasMasterRole(userDetails)) {
			throw new CustomApiException(UserException.ACCESS_DENIED);
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomApiException(UserException.USER_NOT_FOUND));

		user.updateUser(requestDto);
		user.setUpdatedAt(LocalDateTime.now());

		UserUpdateResponseDto responseDto = UserUpdateResponseDto.builder()
			.userId(user.getUserId())
			.userName(user.getUserName())
			.userNickname(user.getUserNickname())
			.userEmail(user.getUserEmail())
			.userRole(user.getUserRole().name())
			.userPhone(user.getUserPhone())
			.userSlackId(user.getUserSlackId())
			.build();

		return new CommonResponse<>(SuccessCode.USER_UPDATED, responseDto);
	}


	@Transactional
	public CommonResponse<Void> deleteUser(UUID userId) {
		RequestUserDetails userDetails = getAuthenticatedUser();
		validateAuthentication(userDetails);

		// 권한 체크
		if (!hasMasterRole(userDetails)) {
			throw new CustomApiException(UserException.ACCESS_DENIED);
		}

		// 삭제 대상 User 엔티티 조회
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomApiException(UserException.USER_NOT_FOUND));

		// 소프트 삭제 수행 - @PreUpdate를 활용하여 deletedAt, deletedBy 자동 설정
		user.markAsDeleted();

		// 응답
		return new CommonResponse<>(SuccessCode.USER_DELETED, null);
	}

	// 인증 정보 검증
	private void validateAuthentication(RequestUserDetails userDetails) {
		if (userDetails == null || userDetails.getUserId() == null) {
			throw new CustomApiException(UserException.INVALID_AUTHENTICATION);
		}
	}

	// 마스터 권한 확인
	private boolean hasMasterRole(RequestUserDetails userDetails) {
		return userDetails.getAuthorities().stream()
			.anyMatch(auth -> auth.getAuthority().equals("ROLE_MASTER"));
	}

	private RequestUserDetails getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new CustomApiException(UserException.INVALID_AUTHENTICATION);
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof RequestUserDetails userDetails) {
			return userDetails;
		}

		throw new CustomApiException(UserException.INVALID_AUTHENTICATION);
	}

}
