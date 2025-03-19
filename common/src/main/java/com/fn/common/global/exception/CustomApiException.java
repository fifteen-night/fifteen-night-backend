package com.fn.common.global.exception;

import org.springframework.http.HttpStatus;

import com.fn.common.global.exception.type.BaseException;
import com.fn.common.global.exception.type.ExceptionType;

import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException {

	// 커스텀 api Exception
	// API에서 발생할 수 있는 다양한 예외를 일관되게 처리

	private final HttpStatus httpStatus;
	private ExceptionType exceptionType;


	public CustomApiException(ExceptionType exceptionType){
		super(exceptionType.getMessage());
		this.exceptionType = exceptionType;
		this.httpStatus = exceptionType.getHttpStatus();
	}

	public CustomApiException(String message, HttpStatus httpStatus) {
		super(message);
		this.exceptionType = BaseException.SERVER_ERROR;
		this.httpStatus = httpStatus;
	}
}
