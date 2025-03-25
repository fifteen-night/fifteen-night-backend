package com.fn.eureka.client.orderservice.application.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

	private String message;
	private UserData data;

	@Getter
	public static class UserData {
		private UUID userId;
		private String username;
		private String userNickname;
		private String userSlackId;
		private String userRole;
		private String email;
		private String userPhone;
	}
}