package com.fn.eureka.client.deliverymanagerservice.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManager;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID> {
}
