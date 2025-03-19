package com.fn.common.global.success;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SuccessCode {

	// 여기서 공통 성공 응답 생성

	HUBTOHUB_CREATE(HttpStatus.CREATED, "루트가 성공적으로 생성되었습니다.", "S_HUBTOHUB_CREATE"),
	HUBTOHUB_SEARCH_ONE(HttpStatus.OK, "단건 루트가 성공적으로 조회되었습니다.", "S_HUBTOHUB_SEARCH_ONE"),
	HUB_CREATE(HttpStatus.CREATED, "허브가 성공적으로 생성되었습니다.", "S_HUB_CREATE"),
	HUB_SEARCH(HttpStatus.OK, "허브가 성공적으로 조회되었습니다.", "S_HUB_SEARCH"),
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