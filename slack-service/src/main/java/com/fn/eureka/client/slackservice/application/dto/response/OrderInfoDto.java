package com.fn.eureka.client.slackservice.application.dto.response;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.boot.autoconfigure.pulsar.PulsarAutoConfiguration;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderInfoDto {
	private String message;
	private OrderData data;
	@Getter
	public static class OrderData {
		private UUID orderId;
		private UUID orderSupplyCompanyId;
		private UUID orderReceiveCompanyId;
		private UUID orderDeliveryId;
		private UUID orderProductId;
		private Integer orderProductQuantity;
		private Timestamp orderDeadline;
		private String orderRequirement;
		private String orderCreatedAt;
	}
}
