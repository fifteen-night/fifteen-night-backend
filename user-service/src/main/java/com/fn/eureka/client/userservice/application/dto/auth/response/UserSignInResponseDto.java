package com.fn.eureka.client.userservice.application.dto.auth.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignInResponseDto {
	private String accessToken;
	private String refreshToken;
}
