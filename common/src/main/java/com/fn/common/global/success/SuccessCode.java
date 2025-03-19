package com.fn.common.global.success;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SuccessCode {

	// 여기서 공통 성공 응답 생성
  // HubRoute 관련 성공응답
	HUBTOHUB_CREATE(HttpStatus.CREATED, "루트가 성공적으로 생성되었습니다.", "S_HUBTOHUB_CREATE"),
	HUBTOHUB_SEARCH_ALL(HttpStatus.OK, "모든 루트가 성공적으로 조회되었습니다.","S_HUBTOHUB_SEARCH_ALL" ),
	HUBTOHUB_SEARCH_ONE(HttpStatus.OK, "단건 루트가 성공적으로 조회되었습니다.", "S_HUBTOHUB_SEARCH_ONE"),
	HUBTOHUB_SOFT_DELETE(HttpStatus.NO_CONTENT, "해당 루트가 삭제되었습니다.", "S_HUBTOHUB_SOFT_DELETE" ),
  
  // Hub 관련 성공응답
	HUB_CREATE(HttpStatus.CREATED, "허브가 성공적으로 생성되었습니다.", "S_HUB_CREATE"),
	HUB_SEARCH(HttpStatus.OK, "허브가 성공적으로 조회되었습니다.", "S_HUB_SEARCH"),

	// Auth 관련 성공 응답
	AUTH_SIGNUP(HttpStatus.CREATED, "회원가입이 성공적으로 완료되었습니다.", "S_AUTH_SIGNUP"),
	AUTH_LOGIN(HttpStatus.OK, "로그인이 성공적으로 완료되었습니다.", "S_AUTH_LOGIN"),

	// User 관련 성공 응답
	USER_FOUND(HttpStatus.OK, "사용자 정보가 성공적으로 조회되었습니다.", "S_USER_FOUND"),
	USER_LIST_FOUND(HttpStatus.OK, "사용자 목록이 성공적으로 조회되었습니다.", "S_USER_LIST_FOUND"),
	USER_UPDATED(HttpStatus.OK, "사용자 정보가 성공적으로 수정되었습니다.", "S_USER_UPDATED"),
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