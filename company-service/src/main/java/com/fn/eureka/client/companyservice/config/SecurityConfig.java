package com.fn.eureka.client.companyservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
// @EnableWebSecurity
public class SecurityConfig {

	@Bean
	public AuthenticationFilter getAuthenticationFilter() {
		return new AuthenticationFilter();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/auth/**", "/api/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
				.anyRequest().authenticated()
			)
			.addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}