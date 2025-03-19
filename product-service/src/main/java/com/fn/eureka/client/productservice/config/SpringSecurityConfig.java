package com.fn.eureka.client.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

// TODO 추후 삭제 - 테스트 때마다 로그인하기 번거로워서 설정한 것
@Configuration
public class SpringSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable);
		http.sessionManagement((session) -> session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
