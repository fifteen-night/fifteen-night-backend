package com.fn.eureka.client.companyservice.infrastructure.exception;

import org.springframework.http.HttpStatus;

import com.fn.common.global.exception.type.ExceptionType;

import lombok.Getter;

@Getter
public enum CompanyException implements ExceptionType {
	COMPANY_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 존재하지 않습니다.", "E_COMPANY_UNAUTHORIZED"),
	COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "업체를 조회할 수 없습니다.", "E_COMPANY_NOT_FOUND");

	private final HttpStatus httpStatus;
	private final String message;
	private final String errorCode;

	CompanyException (HttpStatus httpStatus, String message, String errorCode) {
		this.httpStatus = httpStatus;
		this.message = message;
		this.errorCode = errorCode;
	}
}
