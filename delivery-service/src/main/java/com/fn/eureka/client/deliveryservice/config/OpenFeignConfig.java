package com.fn.eureka.client.deliveryservice.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.fn.eureka.client.deliveryservice")
public class OpenFeignConfig {
}
