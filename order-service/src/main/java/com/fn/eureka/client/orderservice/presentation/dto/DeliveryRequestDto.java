package com.fn.eureka.client.orderservice.presentation.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryRequestDto {
	private UUID deliveryOrderId;
	private UUID deliveryDepartureHubId;
	private UUID deliveryDestinationHubId;
	private String deliveryAddress;
	private String deliveryReceiverCompanyName;
	private UUID deliveryReceiverSlackId;

	public DeliveryRequestDto(UUID orderId, UUID supplyCompanyHubId, UUID receiveCompanyHubId, String receiveCompanyAddress, String deliveryReceiverCompanyName, UUID receiverSlackId) {
		this.deliveryOrderId = orderId;
		this.deliveryDepartureHubId = supplyCompanyHubId;
		this.deliveryDestinationHubId = receiveCompanyHubId;
		this.deliveryAddress = receiveCompanyAddress;
		this.deliveryReceiverCompanyName = deliveryReceiverCompanyName;
		this.deliveryReceiverSlackId = receiverSlackId;
	}
}

