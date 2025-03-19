package com.fn.eureka.client.hubservice.hub.infrastructure;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.exception.NotFoundException;
import com.fn.eureka.client.hubservice.hub.application.GeoService;
import com.fn.eureka.client.hubservice.hub.application.HubService;
import com.fn.eureka.client.hubservice.hub.application.dto.mapper.HubMapper;
import com.fn.eureka.client.hubservice.hub.application.dto.request.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.request.UpdateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.response.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.Point;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;
import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub.domain.repository.HubRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {
	private final HubRepository hubRepository;
	private final GeoService geoService;

	@Override
	@Transactional
	public CreateHubResponse createHub(CreateHubRequest request) {

		Point point = geoService.getPoint(request.getHubAddress());
		Hub hub = hubRepository.save(HubMapper.toEntity(request, point));

		return HubMapper.toDto(hub);
	}

	@Override
	@Transactional(readOnly = true)
	public ReadHubResponse readHub(UUID hubId) {

		Hub hub = hubRepository.findById(hubId).orElseThrow(() -> new NotFoundException("없는 허브입니다."));

		return HubMapper.toDto(hub, hubId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ReadHubResponse> searchHub(Pageable pageable, String hubName) {

		return hubRepository.searchHubs(pageable, hubName);
	}

	@Override
	@Transactional
	public void updateHub(UUID hubId, UpdateHubRequest request) {

		Hub hub = hubRepository.findById(hubId).orElseThrow(() -> new NotFoundException("없는 허브입니다."));

		if (request.getHubName() != null) {
			hub.updateHubName(request.getHubName());
		}

		if (request.getHubType() != null) {
			hub.updateHubType(request.getHubType());
		}

		if (request.getHubManagerId() != null) {
			hub.updateHubManagerId(request.getHubManagerId());
		}
	}

	@Override
	@Transactional
	public void deleteHub(UUID hubId) {
		Hub hub = hubRepository.findById(hubId).orElseThrow(() -> new NotFoundException("없는 허브입니다."));

		hub.markAsDeleted();
	}

	@Override
	public Hub findHubById(UUID id) {

		return hubRepository.findById(id).orElseThrow(() -> new NotFoundException("없는 허브입니다."));
	}

	@Override
	public Hub findHubById(UUID id) {

		return hubRepository.findById(id).orElseThrow(() -> new NotFoundException("없는 허브입니다."));
	}

	@Override
	public Hub findHubById(UUID id) {

		return hubRepository.findById(id).orElseThrow(() -> new NotFoundException("없는 허브입니다."));
	}
}