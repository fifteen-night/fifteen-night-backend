package com.fn.eureka.client.deliverymanagerservice.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryManagerType {
	HUB_MANAGER("HUB"),
	COMPANY_MANAGER("COMPANY");

	private final String value;
}
