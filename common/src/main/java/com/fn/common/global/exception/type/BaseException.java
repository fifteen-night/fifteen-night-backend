package com.fn.common.global.exception.type;

import org.springframework.http.HttpStatus;

public enum BaseException implements ExceptionType{

	// 기본적인 예외 타입 Enum 클래스
	SERVER_ERROR(HttpStatus.BAD_REQUEST, "서버 오류가 발생하였습니다.", "E_NOT_DEFINED"),
	UNAUTHORIZED_REQ(HttpStatus.FORBIDDEN, "권한이 없는 요청입니다.", "E_UNAUTH");

	private final HttpStatus status;
	private final String message;
	private final String errorCode;

	BaseException(HttpStatus status, String message, String errorCode) {
		this.status = status;
		this.message = message;
		this.errorCode = errorCode;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return this.status;
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
