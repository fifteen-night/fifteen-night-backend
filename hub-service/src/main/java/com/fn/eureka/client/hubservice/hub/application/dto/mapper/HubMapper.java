package com.fn.eureka.client.hubservice.hub.application.dto.mapper;

import com.fn.eureka.client.hubservice.hub.application.dto.request.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.response.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.Point;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.UpdateHubResponse;
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

	public static CreateHubResponse toCreateDto(Hub hub) {
		return new CreateHubResponse(hub.getHubId());
	}

	public static ReadHubResponse toReadDto(Hub hub) {
		return ReadHubResponse.builder()
			.hubId(hub.getHubId())
			.hubAddress(hub.getHubAddress())
			.hubManagerId(hub.getHubManagerId())
			.hubName(hub.getHubName())
			.hubType(hub.getHubType())
			.hubLatitude(hub.getHubLatitude())
			.hubLongitude(hub.getHubLongitude())
			.build();
	}

	public static UpdateHubResponse toUpdateDto(Hub hub) {
		return UpdateHubResponse.builder()
			.hubId(hub.getHubId())
			.hubAddress(hub.getHubAddress())
			.hubManagerId(hub.getHubManagerId())
			.hubName(hub.getHubName())
			.hubType(hub.getHubType())
			.hubLatitude(hub.getHubLatitude())
			.hubLongitude(hub.getHubLongitude())
			.build();
	}
}