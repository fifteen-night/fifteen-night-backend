package com.fn.eureka.client.deliveryservice.application.dto.deliveryRouteSequence.request;

import java.math.BigDecimal;
import java.time.LocalTime;

import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRoute;
import com.fn.eureka.client.deliveryservice.domain.model.deliveryRouteSequence.DeliveryRouteSequence;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateSequenceRequestDto {

	@NotNull
	private DeliveryRoute deliveryRoute;

	@NotNull
	private int sequenceNumber;

	@NotNull
	private String departureHubAddress;

	@NotNull
	private String arrivalHubAddress;

	@NotNull
	private LocalTime quantity;

	@NotNull
	private BigDecimal distance;

	public static DeliveryRouteSequence toSequence(CreateSequenceRequestDto sequence) {

		return DeliveryRouteSequence.builder()
			.deliveryRoute(sequence.getDeliveryRoute())
			.sequenceNumber(sequence.getSequenceNumber())
			.departureHubAddress(sequence.getDepartureHubAddress())
			.arrivalHubAddress(sequence.getArrivalHubAddress())
			.quantity(sequence.getQuantity())
			.distance(sequence.getDistance())
			.build();
	}
}
