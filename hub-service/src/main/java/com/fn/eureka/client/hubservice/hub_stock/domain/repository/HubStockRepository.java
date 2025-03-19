package com.fn.eureka.client.hubservice.hub_stock.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;

public interface HubStockRepository extends JpaRepository<HubStock, UUID> {
	Optional<HubStock> findByHsHubHubIdAndHsProductId(UUID hubId, UUID productId);
}