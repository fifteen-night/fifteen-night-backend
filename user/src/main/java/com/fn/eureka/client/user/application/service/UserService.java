package com.fn.eureka.client.user.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.user.application.dto.user.request.UserUpdateRequestDto;
import com.fn.eureka.client.user.application.dto.user.response.UserGetResponseDto;
import com.fn.eureka.client.user.application.dto.user.response.UserUpdateResponseDto;
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
	public CommonResponse<UserGetResponseDto> getUser(UUID userId, RequestUserDetails userDetails) {
		validateAuthentication(userDetails);

		UUID requestUserId = UUID.fromString(userDetails.getUserId());
		boolean isMaster = hasMasterRole(userDetails);

		// 조회 대상 유저 정보 가져오기
		User targetUser = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException(UserException.USER_NOT_FOUND.getMessage()));

		// MASTER가 아니면 본인 정보만 조회 가능
		if (!isMaster && !requestUserId.equals(userId)) {
			throw new RuntimeException(UserException.ACCESS_DENIED.getMessage());
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
	public CommonResponse<Page<UserGetResponseDto>> getUsers(String keyword, Pageable pageable, RequestUserDetails userDetails) {
		validateAuthentication(userDetails);

		if (!hasMasterRole(userDetails)) {
			throw new RuntimeException(UserException.ACCESS_DENIED.getMessage());
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
	public CommonResponse<UserUpdateResponseDto> updateUser(UUID userId, UserUpdateRequestDto requestDto, RequestUserDetails userDetails) {
		validateAuthentication(userDetails);

		// 수정 권한 체크 마스터 관리자만 가능
		if (!hasMasterRole(userDetails)) {
			throw new RuntimeException(UserException.ACCESS_DENIED.getMessage());
		}

		// 사용자 조회
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException(UserException.USER_NOT_FOUND.getMessage()));

		// 업데이트 수행
		user.updateUser(requestDto);

		// 수정된 정보 DTO 변환
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

	// 인증 정보 검증
	private void validateAuthentication(RequestUserDetails userDetails) {
		if (userDetails == null || userDetails.getUserId() == null) {
			throw new RuntimeException(UserException.INVALID_AUTHENTICATION.getMessage());
		}
	}

	// 마스터 권한 확인
	private boolean hasMasterRole(RequestUserDetails userDetails) {
		return userDetails.getAuthorities().stream()
			.anyMatch(auth -> auth.getAuthority().equals("ROLE_MASTER"));
	}
}
