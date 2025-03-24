package com.fn.eureka.client.deliveryservice.application.dto.delivery.response;

import java.util.UUID;

import com.fn.eureka.client.deliveryservice.application.dto.deliveryRoute.response.CreateDeliveryRouteResponseDto;
import com.fn.eureka.client.deliveryservice.domain.model.delivery.Delivery;
import com.fn.eureka.client.deliveryservice.domain.model.delivery.DeliveryStatus;

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
	private UUID cdmId;
	private CreateDeliveryRouteResponseDto deliveryRoute;

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
			.cdmId(targetDelivery.getCdmId())
			.deliveryRoute(targetDelivery != null
				? CreateDeliveryRouteResponseDto.fromDeliveryRoute(targetDelivery.getDeliveryRoute())
				: null)
			.build();
	}
}
