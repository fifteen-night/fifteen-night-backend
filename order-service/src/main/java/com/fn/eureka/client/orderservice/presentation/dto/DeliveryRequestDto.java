package com.fn.eureka.client.orderservice.presentation.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryRequestDto {
	private UUID deliveryOrderId;
	private UUID deliveryDepartureHubId;
	private UUID deliveryDestinationHubId;
	private String deliveryAddress;
	private String deliveryReceiverCompanyManagerName;
	private UUID deliveryReceiverSlackId;

	@Builder
	public DeliveryRequestDto(UUID orderId, UUID supplyCompanyHubId, UUID receiveCompanyHubId, String receiveCompanyAddress, String deliveryReceiverCompanyManagerName, UUID receiverSlackId) {
		this.deliveryOrderId = orderId;
		this.deliveryDepartureHubId = supplyCompanyHubId;
		this.deliveryDestinationHubId = receiveCompanyHubId;
		this.deliveryAddress = receiveCompanyAddress;
		this.deliveryReceiverCompanyManagerName = deliveryReceiverCompanyManagerName;
		this.deliveryReceiverSlackId = receiverSlackId;
	}
}

