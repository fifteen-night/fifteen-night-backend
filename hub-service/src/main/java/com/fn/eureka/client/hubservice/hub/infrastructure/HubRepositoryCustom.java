package com.fn.eureka.client.hubservice.hub.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;

public interface HubRepositoryCustom {
	Page<ReadHubResponse> searchHubs(Pageable pageable, String hubName);
}