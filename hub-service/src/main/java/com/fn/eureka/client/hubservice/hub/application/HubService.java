package com.fn.eureka.client.hubservice.hub.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fn.eureka.client.hubservice.hub.application.dto.request.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.request.UpdateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.response.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;

public interface HubService {
	CreateHubResponse createHub(CreateHubRequest request);

	ReadHubResponse readHub(UUID hubId);

	Page<ReadHubResponse> searchHub(Pageable pageable, String hubName);

	void updateHub(UUID hubId, UpdateHubRequest request);

	void deleteHub(UUID hubId);
}