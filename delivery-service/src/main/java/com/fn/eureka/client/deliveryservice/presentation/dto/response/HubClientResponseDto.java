package com.fn.eureka.client.deliveryservice.presentation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class HubClientResponseDto {

	private UUID hubId;
	private String hubName;
	private String hubAddress;
	private String hubType;
	private UUID hubManagerId;
	private BigDecimal hubLatitude;
	private BigDecimal hubLongitude;
}
