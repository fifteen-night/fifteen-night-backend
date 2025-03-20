package com.fn.eureka.client.deliveryservice.application.route.dto.request;

import com.fn.eureka.client.deliveryservice.domain.route.HubToHub;

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
	private String departureHubAddress;

	@NotBlank
	private String arrivalHubAddress;

	public static HubToHub toHubToHub(CreateHubToHubRequestDto createHubToHubRequestDto) {
		return HubToHub.builder()
			.departureHubAddress(createHubToHubRequestDto.getDepartureHubAddress())
			.arrivalHubAddress(createHubToHubRequestDto.getArrivalHubAddress())
			.build();
	}

}
