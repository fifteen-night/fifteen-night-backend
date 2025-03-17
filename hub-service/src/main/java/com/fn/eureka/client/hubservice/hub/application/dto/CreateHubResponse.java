package com.fn.eureka.client.hubservice.hub.application.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateHubResponse {
	private UUID hubId;
}