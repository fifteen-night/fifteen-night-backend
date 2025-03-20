package com.fn.eureka.client.slackservice.application.exception;

import org.springframework.http.HttpStatus;

import com.fn.common.global.exception.type.ExceptionType;

public enum SlackException implements ExceptionType {

	SLACK_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Slack 메시지를 찾을 수 없습니다.", "E_SLACK_MESSAGE_NOT_FOUND"),
	UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "마스터 권한이 필요합니다.", "E_UNAUTHORIZED_ACCESS");

	private final HttpStatus httpStatus;
	private final String message;
	private final String errorCode;

	SlackException(HttpStatus httpStatus, String message, String errorCode) {
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
