package com.fn.eureka.client.hubservice.hub.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateHubResponse {
	private UUID hubId;
}