package com.fn.eureka.client.userservice.application.exception;

import org.springframework.http.HttpStatus;
import com.fn.common.global.exception.type.ExceptionType;

public enum JwtException implements ExceptionType {
	INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다.", "E_INVALID_JWT_SIGNATURE"),
	EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다.", "E_EXPIRED_JWT_TOKEN"),
	UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다.", "E_UNSUPPORTED_JWT_TOKEN"),
	JWT_CLAIM_IS_EMPTY(HttpStatus.UNAUTHORIZED, "JWT 토큰이 비어있습니다.", "E_JWT_CLAIM_IS_EMPTY");

	private final HttpStatus httpStatus;
	private final String message;
	private final String errorCode;

	JwtException(HttpStatus httpStatus, String message, String errorCode) {
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
