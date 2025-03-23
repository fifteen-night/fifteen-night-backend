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
import com.fn.eureka.client.hubservice.hub.application.dto.response.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.Point;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadProductResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.UpdateHubResponse;
import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub.domain.repository.HubRepository;
import com.fn.eureka.client.hubservice.hub.exception.HubException;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.mapper.HubStockMapper;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.UpdateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.ReadHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.UpdateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;
import com.fn.eureka.client.hubservice.hub_stock.domain.repository.HubStockRepository;
import com.fn.eureka.client.hubservice.hub_stock.exception.HubStockException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {
	private final HubRepository hubRepository;
	private final HubStockRepository hubStockRepository;
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

		return HubMapper.toCreateDto(hub);
	}

	@Override
	@Transactional(readOnly = true)
	public ReadHubResponse readHub(UUID hubId) {
		Hub hub = findHubById(hubId);

		return HubMapper.toReadDto(hub);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ReadHubResponse> searchHub(Pageable pageable, String hubName) {
		return hubRepository.searchHubs(pageable, hubName);
	}

	@Override
	@Transactional
	public UpdateHubResponse updateHub(UUID hubId, UpdateHubRequest request) {
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

		return HubMapper.toUpdateDto(hub);
	}

	@Override
	@Transactional
	public void deleteHub(UUID hubId) {
		Hub hub = findHubById(hubId);

		hub.markAsDeleted();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkHubManager(CheckHubManagerRequest request) {
		Hub hub = findHubById(request.getHubId());

		return hub.getHubManagerId().equals(request.getUserId());
	}

	@Override
	@Transactional(readOnly = true)
	public UUID readHubIdByHubManagerId(UUID hubManagerId) {
		Hub hub = hubRepository.findByHubManagerIdAndIsDeletedIsFalse(hubManagerId)
			.orElseThrow(() -> new CustomApiException(HubException.HUB_NOT_FOUND));

		return hub.getHubId();
	}

	private Hub findHubById(UUID hubId) {
		return hubRepository.findByHubIdAndIsDeletedIsFalse(hubId)
			.orElseThrow(() -> new CustomApiException(HubException.HUB_NOT_FOUND));
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

		Optional<HubStock> optionalHubStock = hubStockRepository.findByHsHubHubIdAndHsProductIdAndIsDeletedIsFalse(
			hubId, request.getProductId());
		HubStock hubStock;

		if (optionalHubStock.isPresent()) {
			// 이미 존재 시 수량 증가
			hubStock = optionalHubStock.get();
			hubStock.updateQuantity(request.getQuantity());
		} else {
			// 없을 시 재고 생성
			hubStock = HubStockMapper.toEntity(request, hub);
		}

		return HubStockMapper.toCreateDto(hubStockRepository.save(hubStock));
	}

	@Override
	@Transactional(readOnly = true)
	public ReadHubStockResponse readHubStock(UUID hubId, UUID productId) {
		HubStock hubStock = findHubStockByHubIdAndProductId(hubId, productId);
		ReadProductResponse response = productClientService.readProduct(productId);

		return HubStockMapper.toReadDto(hubStock, response);
	}

	@Override
	public Page<ReadHubStockResponse> searchHubStock(UUID hubId, Pageable pageable, UUID productId, int quantity,
		LocalDateTime startDateTime, LocalDateTime endDateTime) {
		return hubStockRepository.searchHubStock(hubId, pageable, productId, quantity, startDateTime, endDateTime);
	}

	@Override
	@Transactional
	public UpdateHubStockResponse updateHubStock(UUID hubId, UUID productId, UpdateHubStockRequest request) {
		HubStock hubStock = findHubStockByHubIdAndProductId(hubId, productId);

		if (hubStock.getHsQuantity() + request.getQuantity() < 0) {
			throw new CustomApiException(HubException.HUB_STOCK_LESS_QUANTITY);
		}

		hubStock.updateQuantity(request.getQuantity());

		return HubStockMapper.toUpdateDto(hubStock);
	}

	@Override
	@Transactional
	public void deleteHubStock(UUID hubId, UUID productId) {
		HubStock hubStock = findHubStockByHubIdAndProductId(hubId, productId);

		hubStock.markAsDeleted();
	}

	private HubStock findHubStockByHubIdAndProductId(UUID hubId, UUID productId) {
		return hubStockRepository.findByHsHubHubIdAndHsProductIdAndIsDeletedIsFalse(hubId, productId)
			.orElseThrow(() -> new CustomApiException(HubStockException.HUB_STOCK_NOT_FOUND));
	}
	// 허브 재고 관련 끝
}