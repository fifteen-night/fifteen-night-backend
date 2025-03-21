package com.fn.eureka.client.hubservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.fn.common", "com.fn.eureka.client.hubservice"})
public class HubServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubServiceApplication.class, args);
	}
}