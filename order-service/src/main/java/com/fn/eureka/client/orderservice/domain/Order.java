package com.fn.eureka.client.orderservice.domain;

import java.sql.Timestamp;
import java.util.UUID;

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
public class Order {
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

}
