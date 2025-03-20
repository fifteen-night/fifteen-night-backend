package com.fn.eureka.client.userservice.infrastructure.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fn.eureka.client.userservice.infrastructure.security.RequestUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String requestUri = request.getRequestURI();

		// 인증 예외 경로
		if (requestUri.startsWith("/api/auth/") ||
			requestUri.startsWith("/swagger-ui/") ||
			requestUri.startsWith("/v3/api-docs"))
		{
			filterChain.doFilter(request, response);
			return;
		}

		// API Gateway에서 전달된 인증 정보 추출
		String userId = request.getHeader("X-User-Id");
		String userName = request.getHeader("X-User-Name");
		String role = request.getHeader("X-User-Role");


		// ROLE_ 접두사 추가
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
