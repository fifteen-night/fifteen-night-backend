package com.fn.eureka.client.company.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyType {
	SUPPLIER("SUPPLIER"),
	RECEIVER("RECEIVER");

	private final String value;
}
