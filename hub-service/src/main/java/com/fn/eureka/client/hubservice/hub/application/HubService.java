package com.fn.eureka.client.hubservice.hub.application;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.fn.eureka.client.hubservice.hub.application.dto.request.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.response.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.SearchHubResponse;
import com.fn.eureka.client.hubservice.hub.domain.Hub;

public interface HubService {
	CreateHubResponse createHub(CreateHubRequest request);

	ReadHubResponse readHub(UUID hubId);

	SearchHubResponse searchHub(Pageable pageable, String hubName);

	Hub findHubById(UUID id);
}