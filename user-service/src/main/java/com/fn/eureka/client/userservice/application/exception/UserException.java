package com.fn.eureka.client.userservice.application.exception;

import org.springframework.http.HttpStatus;
import com.fn.common.global.exception.type.ExceptionType;

public enum UserException implements ExceptionType {
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.", "E_USER_NOT_FOUND"),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", "E_ACCESS_DENIED"),
	INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다.", "E_INVALID_AUTHENTICATION");

	private final HttpStatus httpStatus;
	private final String message;
	private final String errorCode;

	UserException(HttpStatus httpStatus, String message, String errorCode) {
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