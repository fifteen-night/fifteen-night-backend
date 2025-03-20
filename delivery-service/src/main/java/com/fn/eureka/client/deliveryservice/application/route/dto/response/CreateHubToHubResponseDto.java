package com.fn.eureka.client.deliveryservice.application.route.dto.response;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

import com.fn.eureka.client.deliveryservice.domain.route.HubToHub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateHubToHubResponseDto {

	// TODO: 나중에 허브 연결시 id받아오기
	// private UUID departureHubName;
	// private UUID arrivalHubName;

	private UUID hthid;
	private String departureHubAddress;
	private String arrivalHubAddress;
	private LocalTime hthQuantity;
	private BigDecimal hthDistance;

	public static CreateHubToHubResponseDto fromHubToHub(HubToHub saveHubToHub) {

		return CreateHubToHubResponseDto.builder()
			.hthid(saveHubToHub.getHthId())
			.departureHubAddress(saveHubToHub.getDepartureHubAddress())
			.arrivalHubAddress(saveHubToHub.getArrivalHubAddress())
			.hthQuantity(saveHubToHub.getHthQuantity())
			.hthDistance(saveHubToHub.getHthDistance())
			.build();

	}
}
