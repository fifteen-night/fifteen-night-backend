package com.fn.eureka.client.hubservice.hub.application;

import com.fn.eureka.client.hubservice.hub.application.dto.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.CreateHubResponse;

public interface HubService {
	CreateHubResponse createHub(CreateHubRequest request);
}