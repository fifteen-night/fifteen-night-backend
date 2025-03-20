package com.fn.eureka.client.hubservice.hub.application.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.fn.eureka.client.hubservice.hub.domain.HubType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReadHubResponse {
	private UUID hubId;
	private String hubName;
	private String hubAddress;
	private HubType hubType;
	private UUID hubManagerId;
	private BigDecimal hubLatitude;
	private BigDecimal hubLongitude;
}