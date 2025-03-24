package com.fn.eureka.client.deliveryservice.application.dto.deliveryRoute.request;

import java.util.UUID;

import com.fn.eureka.client.deliveryservice.domain.model.delivery.Delivery;
import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRoute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateDeliveryRouteRequestDto {

	private UUID departureHubId;
	private UUID destinationHubId;

	public static DeliveryRoute toDeliveryRoute(Delivery delivery) {

		return DeliveryRoute.builder()
			.delivery(delivery)
			.destinationHubAddress(delivery.getDepartureHubId())
			.departureHubAddress(delivery.getDestinationHubId())
			.build();
	}
}
