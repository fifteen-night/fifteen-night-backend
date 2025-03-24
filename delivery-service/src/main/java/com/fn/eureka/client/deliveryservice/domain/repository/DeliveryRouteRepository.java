package com.fn.eureka.client.deliveryservice.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRoute;

public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID> {

	Optional<DeliveryRoute> findByDeliveryRouteIdAndIsDeletedIsFalse(UUID deliveryId);
}
