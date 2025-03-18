package com.fn.eureka.client.companyservice.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyType {
	SUPPLIER("SUPPLIER"),
	RECEIVER("RECEIVER");

	private final String value;
}
