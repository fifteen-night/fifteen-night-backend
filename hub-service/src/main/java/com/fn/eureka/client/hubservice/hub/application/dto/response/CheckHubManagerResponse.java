package com.fn.eureka.client.hubservice.hub.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckHubManagerResponse {
	private boolean isManager;
}