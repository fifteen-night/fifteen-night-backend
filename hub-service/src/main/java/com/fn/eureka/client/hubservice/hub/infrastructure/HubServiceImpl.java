package com.fn.eureka.client.hubservice.hub.infrastructure;

import java.time.LocalDateTime;
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
import com.fn.eureka.client.hubservice.hub_stock.application.dto.mapper.HubStockMapper;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.UpdateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {
	private final HubRepository hubRepository;
	private final GeoService geoService;

	// 허브 관련 시작
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
	// 허브 관련 끝

	// 허브 재고 관련 시작
	@Override
	@Transactional
	public CreateHubStockResponse createHubStock(UUID hubId, CreateHubStockRequest request) {
		Hub hub = findHubById(hubId);
		HubStock hubStock = HubStockMapper.toEntity(request, hub);

		hub.addHubStock(hubStock);
		hubRepository.save(hub);

		return HubStockMapper.toDto(hubStock);
	}

	@Override
	@Transactional(readOnly = true)
	public ReadHubStockResponse readHubStock(UUID hubId, UUID productId) {
		return hubRepository.readHubStock(hubId, productId);
	}

	@Override
	public Page<ReadHubStockResponse> searchHubStock(UUID hubId, Pageable pageable, UUID productId, int quantity,
		LocalDateTime startDateTime, LocalDateTime endDateTime) {
		return hubRepository.searchHubStock(hubId, pageable, productId, quantity, startDateTime, endDateTime);
	}

	@Override
	@Transactional
	public void updateHubStock(UUID hubId, UUID productId, UpdateHubStockRequest request) {
		HubStock hubStock = findHubStockByHubIdAndProductId(hubId, productId);

		hubStock.updateQuantity(request.getQuantity());
	}

	@Override
	@Transactional
	public void deleteHubStock(UUID hubId, UUID productId) {
		HubStock hubStock = findHubStockByHubIdAndProductId(hubId, productId);

		hubStock.markAsDeleted();
	}

	@Override
	public HubStock findHubStockByHubIdAndProductId(UUID hubId, UUID productId) {
		Hub hub = findHubById(hubId);

		return hub.getHubStocks().stream()
			.filter(e -> !e.isDeleted() && e.getHsProductId().equals(productId))
			.findFirst()
			.orElseThrow(() -> new NotFoundException("없는 재고입니다."));
	}
}