package com.fn.eureka.client.deliveryservice.exception;

import org.springframework.http.HttpStatus;

import com.fn.common.global.exception.type.ExceptionType;

public enum HubToHubException implements ExceptionType {
	NOT_FOUND_COORDS(HttpStatus.NOT_FOUND, "좌표를 찾을 수 없습니다.", "E_NOT_FOUND_COORDS"),
	NOT_FOUND_DIRECTION(HttpStatus.NOT_FOUND, "허브간 루트를 생성할 수 없습니다.", "E_NOT_FOUND_DIRECTION" ),
	NOT_FOUND_HUBTOHUB(HttpStatus.NOT_FOUND, "허브간 루트를 조회할 수 없습니다.", "E_NOT_FOUND_HUBTOHUB" ),
	ALREADY_EXISTS_ROUTE(HttpStatus.CONFLICT, "이미 존재하는 루트입니다." , "E_ALREADY_EXISTS_ROUTE"),;

	private final HttpStatus httpStatus;
	private final String message;
	private final String errorCode;

	HubToHubException(HttpStatus httpStatus, String message, String errorCode) {
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

