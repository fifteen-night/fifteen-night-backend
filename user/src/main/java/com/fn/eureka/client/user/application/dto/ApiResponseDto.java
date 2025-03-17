package com.fn.eureka.client.user.application.dto;

import lombok.Getter;

@Getter
public class ApiResponseDto<T> {
	private static final String SUCCESS_MESSAGE = "요청이 성공적으로 처리되었습니다.";

	private int status;
	private String message;
	private T data;

	public ApiResponseDto(int status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	// 200
	public static <T> ApiResponseDto<T> success(T data) {
		return new ApiResponseDto<>(200, SUCCESS_MESSAGE, data);  // default status 200
	}

	// 20x
	public static <T> ApiResponseDto<T> success(int status, T data) {
		return new ApiResponseDto<>(status, SUCCESS_MESSAGE, data);  // default status 200
	}

	// 40x
	public static <T> ApiResponseDto<T> fail(int status, String message) {
		return new ApiResponseDto<>(status, message, null);  // default status 200
	}

}
