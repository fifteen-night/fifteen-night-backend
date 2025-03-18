package com.fn.eureka.client.userservice.application.exception;

import org.springframework.http.HttpStatus;
import com.fn.common.global.exception.type.ExceptionType;

public enum AuthException implements ExceptionType {
	DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다.", "E_DUPLICATED_EMAIL"),
	DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다.", "E_DUPLICATED_NICKNAME"),
	WRONG_USERNAME_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 사용자명 또는 비밀번호입니다.", "E_WRONG_USERNAME_OR_PASSWORD");


	private final HttpStatus httpStatus;
	private final String message;
	private final String errorCode;

	AuthException(HttpStatus httpStatus, String message, String errorCode) {
		this.httpStatus = httpStatus;
		this.message = message;
		this.errorCode = errorCode;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String getErrorCode() {
		return this.errorCode;
	}
}