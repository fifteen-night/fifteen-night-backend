package com.fn.eureka.client.deliveryservice.application;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.deliveryservice.domain.delivery.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
}
