package com.fn.eureka.client.hubservice.hub.application.dto;

import java.util.UUID;

import com.fn.eureka.client.hubservice.hub.domain.HubType;

import lombok.Getter;

@Getter
public class CreateHubRequest {
	private String hubName;
	private String hubAddress;
	private HubType hubType;
	private UUID hubManagerId;
}