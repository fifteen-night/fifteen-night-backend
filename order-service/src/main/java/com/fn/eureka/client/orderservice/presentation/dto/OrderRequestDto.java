package com.fn.eureka.client.orderservice.presentation.dto;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequestDto {
	private UUID orderSupplyCompanyId;
	private UUID orderReceiveCompanyId;
	private UUID orderProductId;
	private Integer orderProductQuantity;
	private Timestamp orderDeadline;
	private String orderRequirement;
}
