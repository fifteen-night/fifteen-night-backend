package com.fn.eureka.client.hubservice.hub_stock.exception;

import org.springframework.http.HttpStatus;

import com.fn.common.global.exception.type.ExceptionType;

public enum HubStockException implements ExceptionType {
	HUB_STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브 재고를 찾을 수 없습니다.", "E_HUB_STOCK_NOT_FOUND");

	private final HttpStatus httpStatus;
	private final String message;
	private final String errorCode;

	HubStockException(HttpStatus httpStatus, String message, String errorCode) {
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