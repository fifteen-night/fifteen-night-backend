package com.fn.eureka.client.orderservice.presentation.dto;

import java.sql.Timestamp;
import java.util.UUID;

import com.fn.eureka.client.orderservice.domain.Order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
	private UUID orderId;
	private UUID orderSupplyCompanyId;
	private UUID orderReceiveCompanyId;
	private UUID orderDeliveryId;
	private UUID orderProductId;
	private Integer orderProductQuantity;
	private Timestamp orderDeadline;
	private String orderRequirement;

	public OrderResponseDto(Order order) {
		this.orderId = order.getOrderId();
		this.orderSupplyCompanyId = order.getOrderSupplyCompanyId();
		this.orderReceiveCompanyId = order.getOrderReceiveCompanyId();
		this.orderDeliveryId = order.getOrderDeliveryId();
		this.orderProductId = order.getOrderProductId();
		this.orderProductQuantity = order.getOrderProductQuantity();
		this.orderDeadline = order.getOrderDeadline();
		this.orderRequirement = order.getOrderRequirement();
	}
}
