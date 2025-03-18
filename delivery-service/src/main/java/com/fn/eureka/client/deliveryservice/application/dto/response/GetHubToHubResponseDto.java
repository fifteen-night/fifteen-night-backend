package com.fn.eureka.client.deliveryservice.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

import com.fn.eureka.client.deliveryservice.domain.HubToHub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetHubToHubResponseDto {

	private UUID hthId;
	private String departureHubAddress;
	private String arrivalHubAddress;
	private LocalTime hthQuantity;
	private BigDecimal hthDistance;

	public static GetHubToHubResponseDto fromHubToHub(HubToHub targetHubToHub) {

		return GetHubToHubResponseDto.builder()
			.hthId(targetHubToHub.getHthId())
			.departureHubAddress(targetHubToHub.getDepartureHubAddress())
			.arrivalHubAddress(targetHubToHub.getArrivalHubAddress())
			.hthQuantity(targetHubToHub.getHthQuantity())
			.hthDistance(targetHubToHub.getHthDistance())
			.build();
	}
}
