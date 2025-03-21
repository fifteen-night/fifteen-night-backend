package com.fn.eureka.client.deliveryservice.application.delivery.dto.request;

import java.util.UUID;

import com.fn.eureka.client.deliveryservice.domain.delivery.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdateDeliveryRequestDto {

	private UUID orderId;
	private UUID departureHubId;
	private UUID destinationHubId;
	private String address;
	private String receiverName;
	private UUID receiverSlackId;
	private DeliveryStatus status;
}
