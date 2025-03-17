package com.fn.eureka.client.hubservice.hub.application.dto.mapper;

import com.fn.eureka.client.hubservice.hub.application.dto.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.Point;
import com.fn.eureka.client.hubservice.hub.domain.Hub;

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