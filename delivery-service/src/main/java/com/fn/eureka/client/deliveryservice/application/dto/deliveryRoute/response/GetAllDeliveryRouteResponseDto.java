package com.fn.eureka.client.deliveryservice.application.dto.deliveryRoute.response;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fn.eureka.client.deliveryservice.application.dto.deliveryRouteSequence.response.GetAllSequenceResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.deliveryRouteSequence.response.GetSequenceResponseDto;
import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRoute;
import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRouteStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GetAllDeliveryRouteResponseDto {

	private UUID deliveryRouteId;
	private List<GetAllSequenceResponseDto> sequence;
	private LocalTime totalTime;
	private BigDecimal totalDistance;
	private DeliveryRouteStatus status;

	public static GetAllDeliveryRouteResponseDto fromDeliveryRoute(DeliveryRoute deliveryRoute) {

		return GetAllDeliveryRouteResponseDto.builder()
			.deliveryRouteId(deliveryRoute.getDeliveryRouteId())
			.sequence(deliveryRoute.getRouteSequences().stream()
				.map(GetAllSequenceResponseDto::fromSequence)
				.collect(Collectors.toList()))
			.totalTime(deliveryRoute.getEstimatedTime())
			.totalDistance(deliveryRoute.getEstimatedDistance())
			.status(deliveryRoute.getCurrentStatus())
			.build();
	}
}
