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
public class GetAllDeliveryResponseDto {

	private UUID deliveryId;
	private UUID orderId;
	private UUID departureHubId;
	private UUID destinationHubId;
	private DeliveryStatus status;
	private String address;
	private String receiverName;
	private UUID receiverSlackId;
	private UUID cmdId;

	public static GetAllDeliveryResponseDto fromDelivery(Delivery delivery) {

		return GetAllDeliveryResponseDto.builder()
			.deliveryId(delivery.getDeliveryId())
			.orderId(delivery.getOrderId())
			.departureHubId(delivery.getDepartureHubId())
			.destinationHubId(delivery.getDestinationHubId())
			.status(delivery.getStatus())
			.address(delivery.getAddress())
			.receiverName(delivery.getReceiverName())
			.receiverSlackId(delivery.getReceiverSlackId())
			.cmdId(delivery.getCdmId())
			.build();
	}
}
