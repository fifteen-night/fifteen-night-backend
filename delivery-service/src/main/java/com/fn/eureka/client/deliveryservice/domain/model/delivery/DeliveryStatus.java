package com.fn.eureka.client.deliveryservice.domain.model.delivery;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {

	PENDING("PENDING"),
	TRANSIT("TRANSIT"),
	ARRIVED("ARRIVED"),
	DELIVERING("DELIVERING");

	private final String value;

}
