package com.fn.eureka.client.hubservice.hub.infrastructure;

import org.springframework.stereotype.Service;

import com.fn.eureka.client.hubservice.hub.application.GeoService;
import com.fn.eureka.client.hubservice.hub.application.HubService;
import com.fn.eureka.client.hubservice.hub.application.dto.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.Point;
import com.fn.eureka.client.hubservice.hub.application.dto.mapper.HubMapper;
import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub.domain.repository.HubRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {
	private final HubRepository hubRepository;
	private final GeoService geoService;

	public CreateHubResponse createHub(CreateHubRequest request) {

		Point point = geoService.getPoint(request.getHubAddress());
		Hub hub = hubRepository.save(HubMapper.toEntity(request, point));

		return HubMapper.toDto(hub);
	}
}