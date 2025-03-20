package com.fn.eureka.client.deliveryservice.domain.delivery;

import java.util.UUID;

import com.fn.common.global.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery", schema = "delivery")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Delivery extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID dId;

	@Column(nullable = false)
	private UUID OrderId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DeliveryStatus Status;

	@Column(nullable = false)
	private UUID departureHubId;

	@Column(nullable = false)
	private UUID destinationHubId;

	@Column(nullable = false, length = 100)
	private String address;

	@Column(nullable = false, length = 100)
	private String receiverName;

	@Column(nullable = false)
	private UUID receiverSlackId;

	// 업체 배송 담당자 id
	// @Column(nullable = false)
	private UUID cdmId;

	@Builder
	public Delivery(
		UUID orderId,
		DeliveryStatus status,
		UUID departureHubId,
		UUID destinationHubId,
		String address,
		String receiverName,
		UUID receiverSlackId,
		UUID cdmId) {

		this.OrderId = orderId;
		this.Status = status;
		this.departureHubId = departureHubId;
		this.destinationHubId = destinationHubId;
		this.address = address;
		this.receiverName = receiverName;
		this.receiverSlackId = receiverSlackId;
		this.cdmId = cdmId;
	}
}
