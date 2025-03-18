package com.fn.eureka.client.hubservice.hub.application.dto.mapper;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.fn.eureka.client.hubservice.hub.application.dto.request.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.response.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.PageInfo;
import com.fn.eureka.client.hubservice.hub.application.dto.response.Point;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.SearchHubResponse;
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

	public static ReadHubResponse toDto(Hub hub, UUID uuid) {
		return ReadHubResponse.builder()
			.hubId(uuid)
			.hubAddress(hub.getHubAddress())
			.hubManagerId(hub.getHubManagerId())
			.hubName(hub.getHubName())
			.hubType(hub.getHubType())
			.build();
	}

	public static SearchHubResponse toDto(Page<Hub> page) {
		List<ReadHubResponse> content = page.stream()
			.map(e -> toDto(e, e.getHubId()))
			.toList();

		return SearchHubResponse.builder()
			.content(content)
			.pageInfo(PageInfo.builder()
				.page(page.getNumber())
				.size(page.getSize())
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.build())
			.build();
	}
}