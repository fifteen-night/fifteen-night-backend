package com.fn.eureka.client.deliveryservice.application.delivery.dto.response;

import java.util.UUID;

import com.fn.eureka.client.deliveryservice.domain.delivery.Delivery;
import com.fn.eureka.client.deliveryservice.domain.delivery.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GetDeliveryResponseDto {

	private UUID deliveryId;
	private UUID orderId;
	private UUID departureHubId;
	private UUID destinationHubId;
	private DeliveryStatus status;
	private String address;
	private String receiverName;
	private UUID receiverSlackId;
	private UUID cmdId;

	public static GetDeliveryResponseDto fromDelivery(Delivery targetDelivery) {

		return GetDeliveryResponseDto.builder()
			.deliveryId(targetDelivery.getDeliveryId())
			.orderId(targetDelivery.getOrderId())
			.departureHubId(targetDelivery.getDepartureHubId())
			.destinationHubId(targetDelivery.getDestinationHubId())
			.status(targetDelivery.getStatus())
			.address(targetDelivery.getAddress())
			.receiverName(targetDelivery.getReceiverName())
			.receiverSlackId(targetDelivery.getReceiverSlackId())
			.cmdId(targetDelivery.getCdmId())
			.build();
	}
}
