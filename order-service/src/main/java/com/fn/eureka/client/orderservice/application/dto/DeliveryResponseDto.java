package com.fn.eureka.client.orderservice.application.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryResponseDto {
	private UUID deliveryId;
	private String deliveryStatus;
	private UUID deliveryDepartureHubId;
	private UUID deliveryDestinationHubId;
	private String deliveryAddress;
	private UUID deliveryReceiverSlackId;
	private UUID deliveryCdmId;
}
