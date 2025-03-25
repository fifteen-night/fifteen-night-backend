package com.fn.eureka.client.deliveryservice.presentation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubClientResponseDto {

	private String message;
	private HubData data;

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class HubData {
		private UUID hubId;
		private String hubName;
		private String hubAddress;
		private String hubType;
		private UUID hubManagerId;
		private BigDecimal hubLatitude;
		private BigDecimal hubLongitude;
	}
}