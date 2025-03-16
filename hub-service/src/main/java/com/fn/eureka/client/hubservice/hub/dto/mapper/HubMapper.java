package com.fn.eureka.client.hubservice.hub.dto.mapper;

import java.math.BigDecimal;

import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub.dto.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.dto.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.dto.Point;

public class HubMapper {

	public static Hub toEntity(CreateHubRequest request, Point point) {
		return Hub.builder()
			.hubManagerId(request.getHubManagerId())
			.hubName(request.getHubName())
			.hubAddress(request.getHubAddress())
			.hubType(request.getHubType())
			.hubLatitude(point.getLatitude())
			.hubLongitude(point.getLongitude())
			.build();
	}

	public static CreateHubResponse toDto(Hub hub) {
		return new CreateHubResponse(hub.getHubId());
	}
}