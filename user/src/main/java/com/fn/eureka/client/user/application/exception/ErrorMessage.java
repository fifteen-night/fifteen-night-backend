package com.fn.eureka.client.user.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
	DUPLICATED_EMAIL("이미 가입된 이메일입니다."),
	DUPLICATED_NICKNAME("이미 사용 중인 닉네임입니다."),
	WRONG_USERNAME_OR_PASSWORD("아이디 또는 비밀번호가 틀렸습니다.");

	private final String message;
}

