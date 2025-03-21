package com.fn.eureka.client.deliveryservice.config;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fn.eureka.client.deliveryservice.domain.service.HubToHubServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class HubRouteScheduler {

	private final HubToHubServiceImpl hubToHubServiceImpl;

	@Scheduled(cron = "0 30 * * * *")
	public void updateHubRoutes() {

		log.info("주기적인 허브 루트 갱신");

		hubToHubServiceImpl.updateAllRoutes();

		log.info("허브 업데이트 종료");

	}
}
