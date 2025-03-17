package com.fn.eureka.client.deliveryservice.application.dto.request;

import java.util.UUID;

import com.fn.eureka.client.deliveryservice.domain.HubToHub;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateHubToHubRequestDto {

	// TODO: 나중에 허브 연결시 id받아와서 find
	// @NotBlank
	// private UUID hthDepartureHubId;
	//
	// @NotBlank
	// private UUID hthDestinationHubId;

	@NotBlank
	private String departureHubName;

	@NotBlank
	private String arrivalHubName;

	public static HubToHub toHubToHub(CreateHubToHubRequestDto createHubToHubRequestDto) {
		return HubToHub.builder()
			.departureHubName(createHubToHubRequestDto.getDepartureHubName())
			.arrivalHubName(createHubToHubRequestDto.getArrivalHubName())
			.build();
	}

}
