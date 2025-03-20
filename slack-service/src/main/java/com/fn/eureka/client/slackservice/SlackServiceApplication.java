package com.fn.eureka.client.slackservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.fn.common" , "com.fn.eureka.client.slackservice"})
public class SlackServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackServiceApplication.class, args);
	}

}
