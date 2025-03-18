package com.fn.eureka.client.userservice.application.dto.user.request;

import com.fn.eureka.client.userservice.domain.entity.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

	@Size(min = 2, max = 50, message = "닉네임은 2~50자여야 합니다.")
	private String userNickname;

	private String userSlackId;

	@Size(min = 10, max = 15, message = "전화번호는 10~15자여야 합니다.")
	private String userPhone;

	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String userEmail;

	private UserRole userRole;
}
