package com.fn.eureka.client.orderservice.exception;

import org.springframework.http.HttpStatus;

import com.fn.common.global.exception.type.ExceptionType;

import lombok.Getter;

@Getter
public enum OrderException implements ExceptionType {
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다.", "E_ORDER_NOT_FOUND"),
	HUB_INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "허브에 재고가 부족합니다.", "E_HUB_INSUFFICIENT_STOCK");

	private final HttpStatus httpStatus;
	private final String message;
	private final String errorCode;

	OrderException(HttpStatus httpStatus, String message, String errorCode) {
		this.httpStatus = httpStatus;
		this.message = message;
		this.errorCode = errorCode;
	}

}
