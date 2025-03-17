package com.fn.eureka.client.user.application.dto.auth.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignInRequestDto {
	private String userName;
	private String userPassword;
}
