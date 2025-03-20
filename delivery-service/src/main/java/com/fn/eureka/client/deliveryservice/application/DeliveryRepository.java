package com.fn.eureka.client.deliveryservice.application;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.deliveryservice.domain.delivery.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

	Optional<Delivery> findByDeliveryIdAndIsDeletedIsFalse(UUID deliveryId);

	Page<Delivery> findAllByIsDeletedIsFalse(Pageable pageable);
}
