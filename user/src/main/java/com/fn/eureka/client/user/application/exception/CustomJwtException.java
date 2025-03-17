package com.fn.eureka.client.user.application.exception;

public class CustomJwtException extends RuntimeException {
	public CustomJwtException(String message) {
		super(message);
	}
}
