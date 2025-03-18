package com.fn.eureka.client.user.application.dto.user.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserGetResponseDto {
	private UUID userId;
	private String userName;
	private String userNickname;
	private String userEmail;
	private String userRole;
	private String userPhone;
	private String userSlackId;
}
