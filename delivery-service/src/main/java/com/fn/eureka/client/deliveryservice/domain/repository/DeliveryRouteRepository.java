package com.fn.eureka.client.deliveryservice.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRoute;

public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID> {
}
