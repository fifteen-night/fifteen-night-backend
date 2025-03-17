package com.fn.eureka.client.user.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionMessage {
	INVALID_JWT_SIGNATURE("잘못된 JWT 서명입니다."),
	EXPIRED_JWT_TOKEN("만료된 JWT 토큰입니다."),
	UNSUPPORTED_JWT_TOKEN("지원되지 않는 JWT 토큰입니다."),
	JWT_CLAIM_IS_EMPTY("JWT 토큰이 비어있습니다.");

	private final String message;
}
