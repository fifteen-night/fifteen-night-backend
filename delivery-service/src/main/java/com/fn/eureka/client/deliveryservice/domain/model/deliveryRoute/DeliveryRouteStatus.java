package com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryRouteStatus {

	WAITING("WAITING"),
	MOVING("MOVING"),
	ARRIVED("ARRIVED"),
	DELIVERING("DELIVERING");

	private final String value;
}
