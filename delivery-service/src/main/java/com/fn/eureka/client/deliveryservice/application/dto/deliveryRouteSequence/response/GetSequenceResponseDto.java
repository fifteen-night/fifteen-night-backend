package com.fn.eureka.client.deliveryservice.application.dto.deliveryRouteSequence.response;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

import com.fn.eureka.client.deliveryservice.domain.model.deliveryRouteSequence.DeliveryRouteSequence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GetSequenceResponseDto {

	private UUID sequenceId;
	private int sequenceNumber;
	private String departureHubAddress;
	private String arrivalHubAddress;
	private LocalTime quantity;
	private BigDecimal distance;
	private UUID hubDeliveryManagerId;

	public static GetSequenceResponseDto fromSequence(DeliveryRouteSequence deliveryRouteSequence) {

		return GetSequenceResponseDto.builder()
			.sequenceId(deliveryRouteSequence.getSequenceId())
			.sequenceNumber(deliveryRouteSequence.getSequenceNumber())
			.departureHubAddress(deliveryRouteSequence.getDepartureHubAddress())
			.arrivalHubAddress(deliveryRouteSequence.getArrivalHubAddress())
			.quantity(deliveryRouteSequence.getQuantity())
			.distance(deliveryRouteSequence.getDistance())
			.hubDeliveryManagerId(deliveryRouteSequence.getHubDeliveryManagerId())
			.build();
	}
}
