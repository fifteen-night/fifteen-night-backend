package com.fn.eureka.client.userservice.application.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInRequestDto {
	private String userName;
	private String userPassword;
}