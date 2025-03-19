package com.fn.common.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
// @EnableJpaAuditing // 각 서비스의 스프링 스캔으로 지정해줘서 충돌 방지
public class JpaConfig {

}
