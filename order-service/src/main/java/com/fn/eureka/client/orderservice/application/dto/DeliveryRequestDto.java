package com.fn.eureka.client.orderservice.application.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryRequestDto {
	private UUID orderId;
	private UUID departureHubId;
	private UUID destinationHubId;
	private String address;
	private String receiverName;
	private String receiverSlackId;

	@Builder
	public DeliveryRequestDto(UUID orderId, UUID supplyCompanyHubId, UUID receiveCompanyHubId,
		String receiveCompanyAddress, String receiverName, String receiverSlackId) {
		this.orderId = orderId;
		this.departureHubId = supplyCompanyHubId;
		this.destinationHubId = receiveCompanyHubId;
		this.address = receiveCompanyAddress;
		this.receiverName = receiverName;
		this.receiverSlackId = receiverSlackId;
	}
}