package com.fn.eureka.client.hubservice.hub.application.dto.response;

import java.util.UUID;

import com.fn.eureka.client.hubservice.hub.domain.HubType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadHubResponse {
	private UUID hubId;
	private String hubName;
	private String hubAddress;
	private HubType hubType;
	private UUID hubManagerId;
}