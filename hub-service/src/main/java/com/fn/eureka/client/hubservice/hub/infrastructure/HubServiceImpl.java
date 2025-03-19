package com.fn.eureka.client.hubservice.hub.infrastructure;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fn.common.global.exception.NotFoundException;
import com.fn.eureka.client.hubservice.hub.application.GeoService;
import com.fn.eureka.client.hubservice.hub.application.HubService;
import com.fn.eureka.client.hubservice.hub.application.dto.mapper.HubMapper;
import com.fn.eureka.client.hubservice.hub.application.dto.request.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.response.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.Point;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.SearchHubResponse;
import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub.domain.repository.HubRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {
	private final HubRepository hubRepository;
	private final GeoService geoService;

	@Override
	public CreateHubResponse createHub(CreateHubRequest request) {

		Point point = geoService.getPoint(request.getHubAddress());
		Hub hub = hubRepository.save(HubMapper.toEntity(request, point));

		return HubMapper.toDto(hub);
	}

	@Override
	public ReadHubResponse readHub(UUID hubId) {

		Hub hub = hubRepository.findById(hubId).orElseThrow(() -> new RuntimeException("없는 허브입니다."));

		return HubMapper.toDto(hub, hubId);
	}

	@Override
	public SearchHubResponse searchHub(Pageable pageable, String hubName) {

		Page<Hub> page = hubRepository.searchHubs(pageable, hubName);

		return HubMapper.toDto(page);
	}

	@Override
	public Hub findHubById(UUID id) {

		return hubRepository.findById(id).orElseThrow(() -> new NotFoundException("없는 허브입니다."));
	}
}