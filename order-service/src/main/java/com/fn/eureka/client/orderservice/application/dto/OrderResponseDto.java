package com.fn.eureka.client.orderservice.application.dto;

import java.sql.Timestamp;
import java.util.UUID;

import com.fn.eureka.client.orderservice.domain.model.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
	private UUID orderId;
	private UUID orderSupplyCompanyId;
	private UUID orderReceiveCompanyId;
	private UUID orderDeliveryId;
	private UUID orderProductId;
	private Integer orderProductQuantity;
	private Timestamp orderDeadline;
	private String orderRequirement;

	public static OrderResponseDto from(Order order) {
		return OrderResponseDto.builder()
			.orderId(order.getOrderId())
			.orderSupplyCompanyId(order.getOrderSupplyCompanyId())
			.orderReceiveCompanyId(order.getOrderReceiveCompanyId())
			.orderDeliveryId(order.getOrderDeliveryId())
			.orderProductId(order.getOrderProductId())
			.orderProductQuantity(order.getOrderProductQuantity())
			.orderDeadline(order.getOrderDeadline())
			.orderRequirement(order.getOrderRequirement())
			.build();
	}
}
