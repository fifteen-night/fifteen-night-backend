package com.fn.common.global.success;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SuccessCode {

	// 여기서 공통 성공 응답 생성

	// Product 관련 성공응답
	PRODUCT_CREATE(HttpStatus.CREATED, "상품이 성공적으로 생성되었습니다.", "S_PRODUCT_CREATE"),
	PRODUCT_SEARCH_ONE(HttpStatus.OK, "단건 상품이 성공적으로 조회되었습니다.", "S_PRODUCT_SEARCH_ONE"),
	PRODUCT_SEARCH_ALL(HttpStatus.OK, "모든 상품이 성공적으로 조회되었습니다.","S_PRODUCT_SEARCH_ALL"),
	PRODUCT_UPDATE(HttpStatus.OK, "상품이 성공적으로 수정되었습니다.", "S_PRODUCT_UPDATE"),
	PRODUCT_DELETE(HttpStatus.NO_CONTENT, "해당 상품이 삭제되었습니다.", "S_PRODUCT_DELETE"),

	// Order 관련 성공응답
	ORDER_CREATE(HttpStatus.CREATED, "주문이 성공적으로 생성되었습니다.", "S_ORDER_CREATE"),
	ORDER_SEARCH_ONE(HttpStatus.OK, "단건 주문이 성공적으로 조회되었습니다.", "S_ORDER_SEARCH_ONE"),
	ORDER_SEARCH_ALL(HttpStatus.OK, "모든 주문이 성공적으로 조회되었습니다.","S_ORDER_SEARCH_ALL"),
	ORDER_UPDATE(HttpStatus.OK, "주문이 성공적으로 수정되었습니다.", "S_ORDER_UPDATE"),
	ORDER_DELETE(HttpStatus.NO_CONTENT, "해당 주문이 삭제되었습니다.", "S_ORDER_DELETE"),

  // HubToHub 관련 성공응답
	HUBTOHUB_CREATE(HttpStatus.CREATED, "루트가 성공적으로 생성되었습니다.", "S_HUBTOHUB_CREATE"),
	HUBTOHUB_SEARCH_ALL(HttpStatus.OK, "모든 루트가 성공적으로 조회되었습니다.","S_HUBTOHUB_SEARCH_ALL" ),
	HUBTOHUB_SEARCH_ONE(HttpStatus.OK, "단건 루트가 성공적으로 조회되었습니다.", "S_HUBTOHUB_SEARCH_ONE"),
	HUBTOHUB_SOFT_DELETE(HttpStatus.NO_CONTENT, "해당 루트가 삭제되었습니다.", "S_HUBTOHUB_SOFT_DELETE" ),
	HUBTOHUB_UPDATE(HttpStatus.OK, "루트가 성공적으로 수정되었습니다.","S_HUBTOHUB_UPDATE" ),

	// Delivery 관련 성공응답
	DELIVERY_CREATE(HttpStatus.CREATED, "배송이 성공적으로 생성되었습니다.", "S_DELIVERY_CREATE" ),
	DELIVERY_SEARCH_ONE(HttpStatus.OK, "단건 배송이 성공적으로 조회되었습니다.","S_DELIVERY_SEARCH_ONE" ),
	DELIVERY_SEARCH_ALL(HttpStatus.OK, "모든 배송이 성공적으로 조회되었습니다.","S_DELIVERY_SEARCH_ALL" ),
	DELIVERY_DELETE(HttpStatus.NO_CONTENT, "해당 배송이 삭제되었습니다.", "S_DELIVERY_DELETE" ),
	DELIVERY_UPDATE(HttpStatus.OK, "배송이 성공적으로 수정되었습니다.", "S_DELIVERY_UPDATE" ),

	// Hub 관련 성공응답
	HUB_CREATE(HttpStatus.CREATED, "허브가 성공적으로 생성되었습니다.", "S_HUB_CREATE"),
	HUB_SEARCH(HttpStatus.OK, "허브가 성공적으로 조회되었습니다.", "S_HUB_SEARCH"),
	HUB_MANAGER_CHECK(HttpStatus.OK, "허브 매니저가 확인되었습니다.", "S_HUB_MANAGER_CHECK"),

	// HubStock 관련 성공 응답
	HUB_STOCK_CREATE(HttpStatus.CREATED, "허브 재고가 성공적으로 생성되었습니다.", "S_HUB_STOCK_CREATE"),
	HUB_STOCK_SEARCH(HttpStatus.OK, "허브 재고가 성공적으로 조회되었습니다.", "S_HUB_STOCK_SEARCH"),

	// Auth 관련 성공 응답
	AUTH_SIGNUP(HttpStatus.CREATED, "회원가입이 성공적으로 완료되었습니다.", "S_AUTH_SIGNUP"),
	AUTH_LOGIN(HttpStatus.OK, "로그인이 성공적으로 완료되었습니다.", "S_AUTH_LOGIN"),

	// User 관련 성공 응답
	USER_FOUND(HttpStatus.OK, "사용자 정보가 성공적으로 조회되었습니다.", "S_USER_FOUND"),
	USER_LIST_FOUND(HttpStatus.OK, "사용자 목록이 성공적으로 조회되었습니다.", "S_USER_LIST_FOUND"),
	USER_UPDATED(HttpStatus.OK, "사용자 정보가 성공적으로 수정되었습니다.", "S_USER_UPDATED"),
	USER_DELETED(HttpStatus.OK, "사용자 정보가 성공적으로 삭제되었습니다.", "S_USER_DELETED"),

	// Slack 관련 성공 응답
	SLACK_MESSAGE_SENT(HttpStatus.CREATED, "Slack 메시지가 성공적으로 전송되었습니다.", "S_SLACK_MESSAGE_SENT"),
	SLACK_MESSAGE_FOUND(HttpStatus.OK, "Slack 메시지가 성공적으로 조회되었습니다.", "S_SLACK_MESSAGE_FOUND"),
	SLACK_MESSAGE_LIST_FOUND(HttpStatus.OK, "Slack 메시지 목록이 성공적으로 조회되었습니다.", "S_SLACK_MESSAGE_LIST_FOUND"),
	SLACK_MESSAGE_UPDATED(HttpStatus.OK, "Slack 메시지가 성공적으로 수정되었습니다.", "S_SLACK_MESSAGE_UPDATED"),
	SLACK_MESSAGE_DELETED(HttpStatus.NO_CONTENT, "Slack 메시지가 성공적으로 삭제되었습니다.", "S_SLACK_MESSAGE_DELETED"),

	// DeliveryManager 관련 성공 응답
	DELIVERY_MANAGER_CREATED(HttpStatus.CREATED, "배송 관리자가 성공적으로 생성되었습니다.", "S_DELIVERY_MANAGER_CREATED"),
	DELIVERY_MANAGER_FOUND(HttpStatus.OK, "배송 관리자가 성공적으로 조회되었습니다.", "S_DELIVERY_MANAGER_FOUND"),
	DELIVERY_MANAGER_LIST_FOUND(HttpStatus.OK, "배송 관리자 목록이 성공적으로 조회되었습니다.", "S_DELIVERY_MANAGER_LIST_FOUND")
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