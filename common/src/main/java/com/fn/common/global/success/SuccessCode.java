package com.fn.common.global.success;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SuccessCode {
	
	// 여기서 공통 성공 응답 생성

	// Route
	;

	private final HttpStatus statusCode;
	private final String message;
	private final String code;

	SuccessCode(HttpStatus statusCode, String message, String code) {
		this.statusCode = statusCode;
		this.message = message;
		this.code = code;
	}
}
