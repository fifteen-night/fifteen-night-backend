package com.fn.eureka.client.slackservice.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.common.global.config.FeignInterceptor;
import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.slackservice.application.dto.response.DeliveryManagerInfoDto;

@FeignClient(name = "delivery-manager-service", path = "/api/delivery-managers", configuration = FeignInterceptor.class)
public interface DeliveryManagerServiceClient {
	@GetMapping("/{dmId}")
	DeliveryManagerInfoDto getDeliveryManager(@PathVariable UUID dmId);
}
