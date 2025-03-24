package com.fn.eureka.client.deliveryservice.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.deliveryservice.domain.model.deliveryRouteSequence.DeliveryRouteSequence;

public interface DeliveryRouteSequenceRepository extends JpaRepository<DeliveryRouteSequence, UUID> {
}
