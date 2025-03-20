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
public class CreateDeliveryResponseDto {

	private UUID deliveryId;
	private UUID orderId;
	private UUID departureHubId;
	private UUID destinationHubId;
	private DeliveryStatus status;
	private String address;
	private String receiverName;
	private UUID receiverSlackId;
	private UUID cmdId;


	public static CreateDeliveryResponseDto fromDelivery(Delivery savedDelivery) {

		return CreateDeliveryResponseDto.builder()
			.deliveryId(savedDelivery.getDeliveryId())
			.orderId(savedDelivery.getOrderId())
			.departureHubId(savedDelivery.getDepartureHubId())
			.destinationHubId(savedDelivery.getDestinationHubId())
			.status(savedDelivery.getStatus())
			.address(savedDelivery.getAddress())
			.receiverName(savedDelivery.getReceiverName())
			.receiverSlackId(savedDelivery.getReceiverSlackId())
			.cmdId(savedDelivery.getCdmId())
			.build();
	}
}
