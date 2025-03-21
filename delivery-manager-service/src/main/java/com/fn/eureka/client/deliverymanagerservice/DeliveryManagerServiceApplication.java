package com.fn.eureka.client.deliverymanagerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.fn.common" , "com.fn.eureka.client.deliverymanagerservice"})
@EnableFeignClients
@EnableDiscoveryClient
public class DeliveryManagerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryManagerServiceApplication.class, args);
	}

}
