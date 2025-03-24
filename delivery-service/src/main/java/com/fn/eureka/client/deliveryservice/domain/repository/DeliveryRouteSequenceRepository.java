package com.fn.eureka.client.deliveryservice.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRoute;
import com.fn.eureka.client.deliveryservice.domain.model.deliveryRouteSequence.DeliveryRouteSequence;

public interface DeliveryRouteSequenceRepository extends JpaRepository<DeliveryRouteSequence, UUID> {
	List<DeliveryRouteSequence> findBySequenceIdAndIsDeletedIsFalse(UUID deliveryRouteId);

	Optional<DeliveryRouteSequence> findByDeliveryRouteAndSequenceNumber(DeliveryRoute deliveryRoute, int i);
}
