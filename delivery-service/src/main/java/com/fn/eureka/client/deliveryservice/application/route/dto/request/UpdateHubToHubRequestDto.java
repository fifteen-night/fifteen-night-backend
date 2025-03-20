package com.fn.eureka.client.deliveryservice.application.route.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdateHubToHubRequestDto {

	private String departureHubAddress;
	private String arrivalHubAddress;
}
