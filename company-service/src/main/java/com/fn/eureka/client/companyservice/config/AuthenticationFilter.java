package com.fn.eureka.client.companyservice.config;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	// private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String requestUri = request.getRequestURI();
		log.info("현재 요청 URI: {}", requestUri);

		// 인증 예외 경로
		if (requestUri.startsWith("/api/") ||
			requestUri.startsWith("/swagger-ui/") ||
			requestUri.startsWith("/v3/api-docs"))
		{
			log.info("인증 예외 경로 - 필터 통과: {}", requestUri);
			filterChain.doFilter(request, response);
			return;
		}

		log.info("인증 필터 적용: {}", requestUri);

		// API Gateway에서 전달된 인증 정보 추출
		String userId = request.getHeader("X-User-Id");
		String userName = request.getHeader("X-User-Name");
		String role = request.getHeader("X-User-Role");

		// // ROLE_ 접두사 추가
		if (!role.startsWith("ROLE_")) {
			role = "ROLE_" + role;
		}

		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
		UserDetails userDetails = new RequestUserDetails(userId, userName, authorities);

		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}
}
