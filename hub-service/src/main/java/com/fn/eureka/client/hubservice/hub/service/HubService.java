package com.fn.eureka.client.hubservice.hub.service;

import org.springframework.stereotype.Service;

import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub.dto.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.dto.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.dto.Point;
import com.fn.eureka.client.hubservice.hub.dto.mapper.HubMapper;
import com.fn.eureka.client.hubservice.hub.repository.HubRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubService {

	private final HubRepository hubRepository;
	private final GeoService geoService;

	public CreateHubResponse createHub(CreateHubRequest request) {

		Point point = geoService.getPoint(request.getHubAddress());
		Hub hub = hubRepository.save(HubMapper.toEntity(request, point));

		return HubMapper.toDto(hub);
	}
}