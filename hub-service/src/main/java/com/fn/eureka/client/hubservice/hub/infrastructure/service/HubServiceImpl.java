package com.fn.eureka.client.hubservice.hub.infrastructure.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.exception.CustomApiException;
import com.fn.eureka.client.hubservice.hub.application.GeoClientService;
import com.fn.eureka.client.hubservice.hub.application.HubService;
import com.fn.eureka.client.hubservice.hub.application.ProductClientService;
import com.fn.eureka.client.hubservice.hub.application.UserClientService;
import com.fn.eureka.client.hubservice.hub.application.dto.mapper.HubMapper;
import com.fn.eureka.client.hubservice.hub.application.dto.request.CheckHubManagerRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.request.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.request.UpdateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.response.CheckHubManagerResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.Point;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;
import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub.domain.repository.HubRepository;
import com.fn.eureka.client.hubservice.hub.exception.HubException;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.mapper.HubStockMapper;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.UpdateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;
import com.fn.eureka.client.hubservice.hub_stock.exception.HubStockException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {
	private final HubRepository hubRepository;
	private final GeoClientService geoClientService;
	private final UserClientService userClientService;
	private final ProductClientService productClientService;

	// 허브 관련 시작
	@Override
	@Transactional
	public CreateHubResponse createHub(CreateHubRequest request) {
		Point point = geoClientService.getPoint(request.getHubAddress());

		if (!userClientService.checkUserIfManager(request.getHubManagerId())) {
			throw new CustomApiException(HubException.USER_NOT_QUALIFIED);
		}

		if (hubRepository.existsByHubAddress(request.getHubAddress())) {
			throw new CustomApiException(HubException.HUB_ALREADY_EXISTS);
		}

		Hub hub = hubRepository.save(HubMapper.toEntity(request, point));

		return HubMapper.toDto(hub);
	}

	@Override
	@Transactional(readOnly = true)
	public ReadHubResponse readHub(UUID hubId) {
		Hub hub = findHubById(hubId);

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
		Hub hub = findHubById(hubId);

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
		Hub hub = findHubById(hubId);

		hub.markAsDeleted();
	}

	@Override
	public Hub findHubById(UUID id) {
		return hubRepository.findById(id).orElseThrow(() -> new CustomApiException(HubException.HUB_NOT_FOUND));
	}

	@Override
	public CheckHubManagerResponse checkHubManager(CheckHubManagerRequest request) {
		Hub hub = findHubById(request.getHubId());

		return new CheckHubManagerResponse(hub.getHubManagerId().equals(request.getUserId()));
	}
	// 허브 관련 끝

	// 허브 재고 관련 시작
	@Override
	@Transactional
	public CreateHubStockResponse createHubStock(UUID hubId, CreateHubStockRequest request) {
		Hub hub = findHubById(hubId);

		if (!productClientService.checkProductIfPresent(request.getProductId())) {
			throw new CustomApiException(HubException.PRODUCT_NOT_FOUND);
		}

		Optional<HubStock> optionalHubStock = hubRepository.findHubStockByHubIdAndProductId(hubId,
			request.getProductId());
		HubStock hubStock;

		if (optionalHubStock.isPresent()) {
			// 이미 존재 시 수량 증가
			hubStock = optionalHubStock.get();
			hubStock.updateQuantity(request.getQuantity());
		} else {
			// 없을 시 재고 생성
			hubStock = HubStockMapper.toEntity(request, hub);
			hub.addHubStock(hubStock);
		}

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

		if (hubStock.getHsQuantity() - request.getQuantity() < 0) {
			throw new CustomApiException(HubException.HUB_STOCK_LESS_QUANTITY);
		}

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
		return hubRepository.findHubStockByHubIdAndProductId(hubId, productId)
			.orElseThrow(() -> new CustomApiException(HubStockException.HUB_STOCK_NOT_FOUND));
	}
}