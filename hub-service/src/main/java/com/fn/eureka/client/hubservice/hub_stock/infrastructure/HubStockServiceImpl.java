package com.fn.eureka.client.hubservice.hub_stock.infrastructure;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.exception.NotFoundException;
import com.fn.eureka.client.hubservice.hub.application.HubService;
import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub_stock.application.HubStockService;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.mapper.HubStockMapper;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.request.CreateHubStockRequest;
import com.fn.eureka.client.hubservice.hub_stock.application.dto.response.CreateHubStockResponse;
import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;
import com.fn.eureka.client.hubservice.hub_stock.domain.repository.HubStockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubStockServiceImpl implements HubStockService {

	private final HubStockRepository hubStockRepository;
	private final HubService hubService;

	@Override
	@Transactional
	public CreateHubStockResponse createHubStock(UUID hubId, CreateHubStockRequest request) {
		Hub hub = hubService.findHubById(hubId);

		Optional<HubStock> optionalHubStock = hubStockRepository.findByHsHubHubIdAndHsProductId(hubId,
			request.getProductId());

		HubStock hubStock;

		if (optionalHubStock.isPresent()) {
			// 이미 존재 시 수량 증가
			hubStock = optionalHubStock.get();
			hubStock.updateQuantity(request.getQuantity());
		} else {
			// 없을 시 재고 생성
			hubStock = hubStockRepository.save(HubStockMapper.toEntity(request, hub));
		}

		return HubStockMapper.toDto(hubStock);
	}

	@Override
	public HubStock findHubStockById(UUID id) {
		return hubStockRepository.findById(id).orElseThrow(() -> new NotFoundException("없는 재고입니다."));
	}
}