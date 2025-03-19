package com.fn.eureka.client.deliveryservice.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.deliveryservice.domain.HubToHub;

public interface HubToHubRepository extends JpaRepository<HubToHub, UUID> {

	Optional<HubToHub> findByHthIdAndIsDeletedIsFalse(UUID hubToHubId);

	boolean existsByDepartureHubAddressAndArrivalHubAddressAndIsDeletedIsFalse(String departureHubAddress, String arrivalHubAddress);

	Page<HubToHub> findAllByIsDeletedIsFalse(Pageable pageable);

	List<HubToHub> findAllByIsDeletedIsFalse();
}
