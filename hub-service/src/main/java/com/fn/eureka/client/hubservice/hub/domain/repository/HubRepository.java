package com.fn.eureka.client.hubservice.hub.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub.infrastructure.repository.HubRepositoryCustom;

public interface HubRepository extends JpaRepository<Hub, UUID>, HubRepositoryCustom {
	Optional<Hub> findByHubIdAndIsDeletedIsFalse(UUID hubId);

	boolean existsByHubAddressAndIsDeletedIsFalse(String address);

	boolean existsByHubIdAndIsDeletedIsFalse(UUID hubId);

	Optional<Hub> findByHubManagerIdAndIsDeletedIsFalse(UUID hubManagerId);
}