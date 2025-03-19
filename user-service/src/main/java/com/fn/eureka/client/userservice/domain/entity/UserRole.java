package com.fn.eureka.client.userservice.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
	MASTER("MASTER"),
	HUB_MANAGER("HUB_MANAGER"),
	DELIVERY_MANAGER("DELIVERY_MANAGER"),
	COMPANY_MANAGER("COMPANY_MANAGER");

	private final String value;
}
