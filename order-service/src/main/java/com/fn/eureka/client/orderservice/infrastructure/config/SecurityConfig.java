package com.fn.eureka.client.orderservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fn.common.global.audit.SecurityContextHelper;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	// private final AuthenticationFilter authenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
			// .authorizeHttpRequests(auth -> auth
			// 	.requestMatchers("/api/auth/**", "/api/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
			// 	.anyRequest().authenticated()
			// )
			// .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
			// .addFilterBefore((request, response, chain) -> {
			// 	SecurityContextHelper.storeAuthentication();
			// 	chain.doFilter(request, response);
			// 	SecurityContextHelper.clear();
			// }, AuthenticationFilter.class);

		return http.build();
	}
}
