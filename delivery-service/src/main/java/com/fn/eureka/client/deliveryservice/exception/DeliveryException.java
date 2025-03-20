package com.fn.eureka.client.deliveryservice.exception;

import org.springframework.http.HttpStatus;

import com.fn.common.global.exception.type.ExceptionType;

public enum DeliveryException implements ExceptionType {
	DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배송을 찾을 수 없습니다.", "E_DELIVERY_NOT_FOUND"),
	ALREADY_EXISTS_DELIVERY(HttpStatus.CONFLICT, "이미 존재하는 배송입니다.", "E_ALREADY_EXISTS_ROUTE" ),;

	private final HttpStatus httpStatus;
	private final String message;
	private final String errorCode;

	DeliveryException(HttpStatus httpStatus, String message, String errorCode) {
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
