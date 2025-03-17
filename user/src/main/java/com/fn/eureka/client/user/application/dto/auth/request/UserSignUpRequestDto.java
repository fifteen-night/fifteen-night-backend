package com.fn.eureka.client.user.application.dto.auth.request;

import com.fn.eureka.client.user.domain.entity.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignUpRequestDto {
	@NotBlank(message = "유저 아이디는 필수 입력값입니다.")
	@Size(min = 4, max = 10, message = "유저 아이디는 4~10자여야 합니다.")
	@Pattern(regexp = "^[a-z0-9]+$", message = "유저 아이디는 소문자와 숫자로만 구성되어야 합니다.")
	private String userName;

	@NotBlank(message = "비밀번호는 필수 입력값입니다.")
	@Size(min = 8, max = 15, message = "비밀번호는 8~15자여야 합니다.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
		message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다.")
	private String userPassword;

	@NotBlank(message = "닉네임은 필수 입력값입니다.")
	private String userNickname;

	@NotBlank(message = "Slack ID는 필수 입력값입니다.")
	private String userSlackId;

	private UserRole userRole; // ENUM 타입 유지

	@NotBlank(message = "전화번호는 필수 입력값입니다.")
	private String userPhone;

	@Email(message = "올바른 이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일은 필수 입력값입니다.")
	private String userEmail;
}
