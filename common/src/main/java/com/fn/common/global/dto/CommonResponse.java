package com.fn.common.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse<T> {

	private String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	public CommonResponse(com.fn.common.global.success.SuccessCode successCode, T data){
		this.message = successCode.getMessage();
		this.data = data;
	}
}
