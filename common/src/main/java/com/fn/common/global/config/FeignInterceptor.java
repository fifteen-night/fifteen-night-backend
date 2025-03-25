package com.fn.common.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FeignInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		ServletRequestAttributes attributes =
			(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			String userId = request.getHeader("X-User-Id");
			String userName = request.getHeader("X-User-Name");
			String role = request.getHeader("X-User-Role");

			requestTemplate.header("X-User-Id", userId);
			requestTemplate.header("X-User-Name", userName);
			requestTemplate.header("X-User-Role", role);
		}
	}
}