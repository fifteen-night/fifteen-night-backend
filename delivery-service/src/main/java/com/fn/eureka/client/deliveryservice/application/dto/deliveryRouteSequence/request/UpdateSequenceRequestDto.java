package com.fn.eureka.client.deliveryservice.application.dto.deliveryRouteSequence.request;

import java.math.BigDecimal;
import java.time.LocalTime;

import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRoute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdateSequenceRequestDto {

	private DeliveryRoute deliveryRoute;
	private int sequenceNumber;
	private String departureHubAddress;
	private String arrivalHubAddress;
	private LocalTime quantity;
	private BigDecimal distance;

	public static UpdateSequenceRequestDto toSequence(UpdateSequenceRequestDto updateSequence) {

		return UpdateSequenceRequestDto.builder()
			.deliveryRoute(updateSequence.getDeliveryRoute())
			.sequenceNumber(updateSequence.getSequenceNumber())
			.departureHubAddress(updateSequence.getDepartureHubAddress())
			.arrivalHubAddress(updateSequence.getArrivalHubAddress())
			.quantity(updateSequence.getQuantity())
			.distance(updateSequence.getDistance())
			.build();
	}
}
