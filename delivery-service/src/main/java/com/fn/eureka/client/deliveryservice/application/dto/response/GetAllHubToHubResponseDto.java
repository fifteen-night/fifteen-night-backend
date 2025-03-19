package com.fn.eureka.client.deliveryservice.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

import com.fn.eureka.client.deliveryservice.domain.HubToHub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GetAllHubToHubResponseDto {

	private UUID hthId;
	private String departureHubAddress;
	private String arrivalHubAddress;
	private LocalTime hthQuantity;
	private BigDecimal hthDistance;

	public static GetAllHubToHubResponseDto fromHubToHub(HubToHub targetHubToHub) {

		return GetAllHubToHubResponseDto.builder()
			.hthId(targetHubToHub.getHthId())
			.departureHubAddress(targetHubToHub.getDepartureHubAddress())
			.arrivalHubAddress(targetHubToHub.getArrivalHubAddress())
			.hthQuantity(targetHubToHub.getHthQuantity())
			.hthDistance(targetHubToHub.getHthDistance())
			.build();
	}
}
