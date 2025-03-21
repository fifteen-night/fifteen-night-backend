package com.fn.eureka.client.hubservice.hub.application.dto.request;

import java.util.UUID;

import com.fn.eureka.client.hubservice.hub.domain.HubType;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateHubRequest {
	@Size(max = 100, message = "최대 100글자 입니다.")
	private String hubName;
	private HubType hubType;
	private UUID hubManagerId;
}