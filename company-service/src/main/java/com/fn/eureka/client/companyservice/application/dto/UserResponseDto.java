package com.fn.eureka.client.companyservice.application.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
	private String message;

	private UserData data;
	public static class UserData {
		private UUID userId;
		private String userName;
		private String userNickname;
		private String userEmail;
		private String userRole;
		private String userPhone;
		private String userSlackId;
	}
}
