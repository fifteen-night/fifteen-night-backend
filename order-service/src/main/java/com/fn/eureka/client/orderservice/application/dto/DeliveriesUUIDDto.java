package com.fn.eureka.client.orderservice.application.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveriesUUIDDto {
	private String message;
	private DeliveryData data;
	@Getter
	public static class DeliveryData {
		private List<UUID> deliveryIds;
	}
}
