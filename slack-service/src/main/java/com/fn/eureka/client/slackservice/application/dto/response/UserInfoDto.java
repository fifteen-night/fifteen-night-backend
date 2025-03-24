package com.fn.eureka.client.slackservice.application.dto.response;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoDto {
	private UUID userId;
	private String userName;
	private String userNickname;
	private String userEmail;
	private String userRole;
	private String userPhone;
	private String userSlackId;
}
