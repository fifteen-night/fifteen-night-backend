package com.fn.eureka.client.hubservice.hub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class GeoClientConfig {

	@Value("${naver.api.client-id}")
	private String clientId;
	@Value("${naver.api.client-secret}")
	private String clientSecret;

	@Bean
	public RequestInterceptor requestInterceptor() {
		return requestTemplate -> {
			requestTemplate.header("X-NCP-APIGW-API-KEY-ID", clientId);
			requestTemplate.header("X-NCP-APIGW-API-KEY", clientSecret);
		};
	}
}