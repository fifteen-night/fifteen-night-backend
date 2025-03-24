package com.fn.eureka.client.deliveryservice.domain.model.deliveryRouteSequence;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

import com.fn.common.global.BaseEntity;
import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRoute;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery_route_sequence", schema = "delivery")
@Getter
@NoArgsConstructor
public class DeliveryRouteSequence extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID sequenceId;

	private int sequenceNumber;

	private String departureHubAddress;

	private String arrivalHubAddress;

	@Column(columnDefinition = "TIME")
	private LocalTime quantity;

	@Column(precision = 9,  scale = 2)
	private BigDecimal distance;

	private UUID hubDeliveryManagerId;

	@ManyToOne
	@JoinColumn(name = "deliveryRouteId", nullable = false)
	private DeliveryRoute deliveryRoute;

	@Builder
	public DeliveryRouteSequence(
		int sequenceNumber,
		String departureHubAddress,
		String arrivalHubAddress,
		LocalTime quantity,
		BigDecimal distance,
		DeliveryRoute deliveryRoute,
		UUID hubDeliveryManagerId
	){
		this.sequenceNumber = sequenceNumber;
		this.departureHubAddress = departureHubAddress;
		this.arrivalHubAddress = arrivalHubAddress;
		this.quantity = quantity;
		this.distance = distance;
		this.deliveryRoute = deliveryRoute;
		this.hubDeliveryManagerId = hubDeliveryManagerId;
	}

}
