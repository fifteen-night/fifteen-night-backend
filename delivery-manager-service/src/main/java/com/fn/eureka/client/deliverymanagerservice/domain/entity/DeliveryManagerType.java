package com.fn.eureka.client.deliverymanagerservice.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryManagerType {
	HUB("HUB"),
	COMPANY("COMPANY");

	private final String value;
}
