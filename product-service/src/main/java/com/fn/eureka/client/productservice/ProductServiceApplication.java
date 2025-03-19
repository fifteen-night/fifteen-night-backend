package com.fn.eureka.client.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
	// (scanBasePackages = {"com.fn.common"})
// @EnableDiscoveryClient
@EnableFeignClients
	// (basePackages = {"com.fn.eureka.client.productservice.infrastructure"})
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

}
