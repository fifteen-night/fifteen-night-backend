package com.fn.eureka.client.hubservice.hub_stock.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;
import com.fn.eureka.client.hubservice.hub_stock.infrastructure.HubStockRepositoryCustom;

public interface HubStockRepository extends JpaRepository<HubStock, UUID>, HubStockRepositoryCustom {
	Optional<HubStock> findByHsHubHubIdAndHsProductIdAndIsDeletedIsFalse(UUID hubId, UUID productId);

	Optional<HubStock> findByHsHubHubIdAndHsIdAndIsDeletedIsFalse(UUID hubId, UUID stockId);

	Optional<HubStock> findByHsIdAndIsDeletedIsFalse(UUID id);
}