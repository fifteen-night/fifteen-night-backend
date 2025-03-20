package com.fn.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fn.gateway.util.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
	private final JwtUtil jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// 1) 경로 및 메서드 확인
		String path = exchange.getRequest().getURI().getPath();
		String method = exchange.getRequest().getMethod().name();

		// 2) 인증 예외 경로 -> 필터 스킵
		if (isAllowedPath(path, method)) {
			return chain.filter(exchange);
		}

		// 3) 헤더에서 JWT 추출
		String token = jwtUtil.extractToken(exchange);

		// 4) JWT 검증 실패 시 401 반환
		if (token == null || !jwtUtil.validateToken(token)) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		log.info("추출된 JWT 토큰: {}", token);


		// 5) Claims(유저 정보) 추출
		Claims claims = jwtUtil.parseClaims(token);
		String userId = claims.get("userId", String.class);
		String userName = claims.get("userName", String.class);
		String role = claims.get("role", String.class);

		log.info("Claims 정보 - userId: {}, userName: {}, role: {}", userId, userName, role);


		// 6) 내부 서비스로 전달할 사용자 정보 헤더에 추가 (exchange 객체 갱신)
		exchange = exchange.mutate()
			.request(exchange.getRequest().mutate()
				.header("X-User-Id", userId)
				.header("X-User-Name", userName)
				.header("X-User-Role", role)
				.build())
			.build();

		log.info("요청에 추가된 헤더 - X-User-Id: {}, X-User-Name: {}, X-User-Role: {}",
			userId, userName, role);

		return chain.filter(exchange);

	}

	// 인증 없이 통과시킬 경로
	private boolean isAllowedPath(String path, String method) {
		return path.startsWith("/api/auth/") ||  // 로그인/회원가입 API
			path.startsWith("/swagger-ui") || // Swagger UI
			path.startsWith("/v3/api-docs"); // Swagger API Docs
	}
}
