package com.fn.eureka.client.deliveryservice.application.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.fn.eureka.client.deliveryservice.domain.HubToHub;
import com.fn.eureka.client.deliveryservice.presentation.dto.response.NaverMapDirResponseDto;

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

	private String departureHubName;
	private String arrivalHubName;
	private String hthQuantity;
	private BigDecimal hthDistance;

	public static CreateHubToHubResponseDto fromHubToHub(HubToHub hub, NaverMapDirResponseDto naverMapDirResponseDto) {

		return CreateHubToHubResponseDto.builder()
			.departureHubName(hub.getDepartureHubName())
			.arrivalHubName(hub.getArrivalHubName())
			.hthQuantity(
				String.valueOf(naverMapDirResponseDto.getRoute().getTraoptimal().get(0).getSummary().getDuration()))
			.hthDistance(
				BigDecimal.valueOf(naverMapDirResponseDto.getRoute().getTraoptimal().get(0).getSummary().getDistance()))
			.build();

	}
}
