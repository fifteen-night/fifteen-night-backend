package com.fn.eureka.client.hubservice.hub.exception;

import org.springframework.http.HttpStatus;

import com.fn.common.global.exception.type.ExceptionType;

public enum HubException implements ExceptionType {
	HUB_ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주소를 찾을 수 없습니다.", "E_HUB_ADDRESS_NOT_FOUND"),
	HUB_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 허브입니다.", "E_HUB_ALREADY_EXISTS"),
	HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브를 찾을 수 없습니다.", "E_HUB_NOT_FOUND"),
	HUB_STOCK_LESS_QUANTITY(HttpStatus.BAD_REQUEST, "요청한 재고 수가 현재 재고 수보다 많습니다.", "E_HUB_STOCK_LESS_QUANTITY"),
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없어 추가할 수 없습니다.", "E_PRODUCT_NOT_FOUND"),
	USER_NOT_QUALIFIED(HttpStatus.BAD_REQUEST, "해당 사용자는 허브 매니저가 아닙니다.", "E_USER_NOT_QUALIFIED"),
	;

	private final HttpStatus httpStatus;
	private final String message;
	private final String errorCode;

	HubException(HttpStatus httpStatus, String message, String errorCode) {
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