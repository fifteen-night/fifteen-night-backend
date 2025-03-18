package com.fn.common.global.exception.type;

import org.springframework.http.HttpStatus;

public interface ExceptionType {
	HttpStatus getHttpStatus();
	String getMessage();
	String getErrorCode();
}
