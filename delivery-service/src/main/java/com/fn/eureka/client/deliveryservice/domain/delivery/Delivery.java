package com.fn.eureka.client.deliveryservice.domain.delivery;

import java.util.Optional;
import java.util.UUID;

import com.fn.common.global.BaseEntity;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.request.UpdateDeliveryRequestDto;

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
	
	// 최대한 사람이 알아볼수있는 컬럼명을 짓자
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID deliveryId;

	@Column(nullable = false)
	private UUID orderId;

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

		this.orderId = orderId;
		this.Status = status;
		this.departureHubId = departureHubId;
		this.destinationHubId = destinationHubId;
		this.address = address;
		this.receiverName = receiverName;
		this.receiverSlackId = receiverSlackId;
		this.cdmId = cdmId;
	}

	public void update(UpdateDeliveryRequestDto updateDeliveryRequestDto) {

		Optional.ofNullable(updateDeliveryRequestDto.getOrderId()).ifPresent(value -> this.orderId = value);
		Optional.ofNullable(updateDeliveryRequestDto.getStatus()).ifPresent(value -> this.Status = value);
		Optional.ofNullable(updateDeliveryRequestDto.getDepartureHubId()).ifPresent(value -> this.departureHubId = value);
		Optional.ofNullable(updateDeliveryRequestDto.getDestinationHubId()).ifPresent(value -> this.destinationHubId = value);
		Optional.ofNullable(updateDeliveryRequestDto.getAddress()).ifPresent(value -> this.address = value);
		Optional.ofNullable(updateDeliveryRequestDto.getReceiverName()).ifPresent(value -> this.receiverName = value);
		Optional.ofNullable(updateDeliveryRequestDto.getReceiverSlackId()).ifPresent(value -> this.receiverSlackId = value);

	}

}
