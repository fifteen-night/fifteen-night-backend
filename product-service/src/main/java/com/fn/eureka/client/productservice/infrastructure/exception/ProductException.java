package com.fn.eureka.client.productservice.infrastructure.exception;

import org.springframework.http.HttpStatus;

import com.fn.common.global.exception.type.ExceptionType;

import lombok.Getter;

@Getter
public enum ProductException implements ExceptionType {
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 조회할 수 없습니다.", "E_PRODUCT_NOT_FOUND"),
	PRODUCT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 존재하지 않습니다.", "E_PRODUCT_UNAUTHORIZED");

	private final HttpStatus httpStatus;
	private final String message;
	private final String errorCode;

	ProductException(HttpStatus httpStatus, String message, String errorCode) {
		this.httpStatus = httpStatus;
		this.message = message;
		this.errorCode = errorCode;
	}
}
