package com.fn.common.global.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.fn.common.global.audit.AuditorAwareImpl;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
	@Bean
	public AuditorAware<UUID> auditorAware() {
		return new AuditorAwareImpl();
	}
}
