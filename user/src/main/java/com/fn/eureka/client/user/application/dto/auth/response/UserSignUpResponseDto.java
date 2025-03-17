package com.fn.eureka.client.user.application.dto.auth.response;

import java.util.UUID;

import com.fn.eureka.client.user.domain.entity.UserRole;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignUpResponseDto {
	private final UUID userId;
	private final String userName;
	private final String userNickname;
	private final String userEmail;
	private final UserRole userRole;
}
