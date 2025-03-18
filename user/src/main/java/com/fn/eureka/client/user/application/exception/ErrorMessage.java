package com.fn.eureka.client.user.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
	DUPLICATED_EMAIL("이미 사용 중인 이메일입니다."),
	DUPLICATED_NICKNAME("이미 사용 중인 닉네임입니다."),
	WRONG_USERNAME_OR_PASSWORD("잘못된 사용자명 또는 비밀번호입니다."),
	USER_NOT_FOUND("존재하지 않는 사용자입니다."),
	ACCESS_DENIED("접근 권한이 없습니다."),
	INVALID_AUTHENTICATION("인증 정보가 유효하지 않습니다.");

	private final String message;
}

