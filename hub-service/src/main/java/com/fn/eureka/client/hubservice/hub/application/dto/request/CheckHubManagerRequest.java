package com.fn.eureka.client.hubservice.hub.application.dto.request;

import java.util.UUID;

import lombok.Getter;

@Getter
public class CheckHubManagerRequest {
	private UUID hubId;
	private UUID userId;
}