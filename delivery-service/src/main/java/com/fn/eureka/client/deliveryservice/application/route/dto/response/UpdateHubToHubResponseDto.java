package com.fn.eureka.client.deliveryservice.application.route.dto.response;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

import com.fn.eureka.client.deliveryservice.domain.route.HubToHub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdateHubToHubResponseDto {

	// TODO: 나중에 허브 연결시 id받아오기
	// private UUID departureHubName;
	// private UUID arrivalHubName;

	private UUID hthid;
	private String departureHubAddress;
	private String arrivalHubAddress;
	private LocalTime hthQuantity;
	private BigDecimal hthDistance;

	public static UpdateHubToHubResponseDto fromHubToHub(HubToHub updateHubToHub) {

		return UpdateHubToHubResponseDto.builder()
			.hthid(updateHubToHub.getHthId())
			.departureHubAddress(updateHubToHub.getDepartureHubAddress())
			.arrivalHubAddress(updateHubToHub.getArrivalHubAddress())
			.hthQuantity(updateHubToHub.getHthQuantity())
			.hthDistance(updateHubToHub.getHthDistance())
			.build();
	}
}
