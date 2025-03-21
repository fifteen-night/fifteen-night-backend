package com.fn.eureka.client.deliverymanagerservice.application.exception;

import org.springframework.http.HttpStatus;

import com.fn.common.global.exception.type.ExceptionType;

public enum DeliveryManagerException implements ExceptionType {
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다.", "E_DM_USER_NOT_FOUND"),
	HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브가 존재하지 않습니다.", "E_DM_HUB_NOT_FOUND"),
	NO_HUB_MANAGER(HttpStatus.NOT_FOUND, "허브 배송 담당자가 없습니다.", "E_DM_NO_HUB_MANAGER");

	private final HttpStatus httpStatus;
	private final String message;
	private final String errorCode;

	DeliveryManagerException(HttpStatus httpStatus, String message, String errorCode) {
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