package com.fn.eureka.client.user.libs.util;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fn.eureka.client.user.application.exception.JwtException;
import com.fn.eureka.client.user.domain.entity.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
	// Token 식별자
	public static final String BEARER_PREFIX = "Bearer ";
	// 토큰 만료시간
	@Value("${service.jwt.access-expiration}")
	private long ACCESS_TOKEN_TIME;

	@Value("${service.jwt.refresh-expiration}")
	private long REFRESH_TOKEN_TIME;

	@Value("${service.jwt.secret-key}")
	private String secretKey;
	private Key key;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public String createAccessToken(UUID userId, String userName, UserRole userRole) {
		Date date = new Date();

		return BEARER_PREFIX +
			Jwts.builder()
				.claim("userId", userId.toString()) // 사용자 식별자값(userId)
				.claim("userName", userName)
				.claim("role", userRole)
				.setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
				.setIssuedAt(date) // 발급일
				.signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘
				.compact();
	}

	public String createRefreshToken(UUID userId) {
		Date date = new Date();

		return Jwts.builder()
			.claim("userId", userId.toString())
			.setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간
			.setIssuedAt(date) // 발급일
			.signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘
			.compact();
	}

	public Claims parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public void validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			throw new RuntimeException(JwtException.INVALID_JWT_SIGNATURE.getMessage());
		} catch (ExpiredJwtException e) {
			throw new RuntimeException(JwtException.EXPIRED_JWT_TOKEN.getMessage());
		} catch (UnsupportedJwtException e) {
			throw new RuntimeException(JwtException.UNSUPPORTED_JWT_TOKEN.getMessage());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(JwtException.JWT_CLAIM_IS_EMPTY.getMessage());
		}
	}
}
