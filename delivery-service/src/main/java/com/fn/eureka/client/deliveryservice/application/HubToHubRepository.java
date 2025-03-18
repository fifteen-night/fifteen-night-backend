package com.fn.eureka.client.deliveryservice.application;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.deliveryservice.domain.HubToHub;

public interface HubToHubRepository extends JpaRepository<HubToHub, UUID> {

	Optional<HubToHub> findByHthIdAndDeletedAtIsNull(UUID hubToHubId);
}
