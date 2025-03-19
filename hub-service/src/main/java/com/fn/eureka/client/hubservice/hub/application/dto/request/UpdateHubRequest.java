package com.fn.eureka.client.hubservice.hub.application.dto.request;

import java.util.UUID;

import com.fn.eureka.client.hubservice.hub.domain.HubType;

import lombok.Getter;

@Getter
public class UpdateHubRequest {
	private String hubName;
	private HubType hubType;
	private UUID hubManagerId;
}