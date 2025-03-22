package com.fn.eureka.client.orderservice.domain.model;

import java.sql.Timestamp;
import java.util.UUID;

import com.fn.common.global.BaseEntity;
import com.fn.eureka.client.orderservice.presentation.request.OrderRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="p_order")
@NoArgsConstructor
public class Order extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID orderId;

	@Column(nullable = false)
	private UUID orderSupplyCompanyId;

	@Column(nullable = false)
	private UUID orderReceiveCompanyId;

	@Column
	private UUID orderDeliveryId;

	@Column(nullable = false)
	private UUID orderProductId;

	@Column(nullable = false)
	private Integer orderProductQuantity;

	@Column(nullable = false)
	private Timestamp orderDeadline;

	@Column
	private String orderRequirement;

	public Order(OrderRequestDto requestDto) {
		this.orderSupplyCompanyId = requestDto.getOrderSupplyCompanyId();
		this.orderReceiveCompanyId = requestDto.getOrderReceiveCompanyId();
		this.orderProductId = requestDto.getOrderProductId();
		this.orderProductQuantity = requestDto.getOrderProductQuantity();
		this.orderDeadline = requestDto.getOrderDeadline();
		this.orderRequirement = requestDto.getOrderRequirement();
	}

	public void saveOrderDeliveryId(UUID deliveryId) {
		this.orderDeliveryId = deliveryId;
	}

	public void modifyOrderInfo(String key, Object value) {
		switch (key) {
			case "orderProductQuantity" -> this.orderProductQuantity = (Integer) value;
			case "orderDeadline" -> this.orderDeadline = (Timestamp) value;
			case "orderRequirement" -> this.orderRequirement = (String) value;
			default -> throw new IllegalArgumentException("잘못된 필드명 : " + key);
		}
	}
}
