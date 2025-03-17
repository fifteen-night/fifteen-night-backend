package com.fn.eureka.client.hubservice.hub.application.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchHubResponse {
	private List<ReadHubResponse> content;
	private PageInfo pageInfo;
}