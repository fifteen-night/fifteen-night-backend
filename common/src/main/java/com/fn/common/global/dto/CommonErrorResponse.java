package com.fn.common.global.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fn.common.global.exception.type.ExceptionType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CommonErrorResponse {
	private String message;
	private String errorCode;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<ErrorField> errorFields;

	public CommonErrorResponse(ExceptionType exceptionType, String message, HttpStatus badRequest) {
		this.message = exceptionType.getMessage();
		this.errorCode = exceptionType.getErrorCode();
	}

	public CommonErrorResponse(String message, ExceptionType exceptionType) {
		this.message = message;
		this.errorCode = exceptionType.getErrorCode();
	}

	public CommonErrorResponse(ExceptionType exceptionType, List<ErrorField> errorFields) {
		this.message = exceptionType.getMessage();
		this.errorCode = exceptionType.getErrorCode();
		this.errorFields = errorFields;
	}

	public CommonErrorResponse(String errorCode, String message, HttpStatus httpStatus) {
		this.message = message;
		this.errorCode = errorCode;
		this.errorFields = new ArrayList<ErrorField>();
	}

	@AllArgsConstructor
	@Getter
	public static class ErrorField {
		private String field;
		private String message;
	}
}