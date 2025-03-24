package com.fn.eureka.client.deliveryservice.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fn.eureka.client.deliveryservice.domain.model.delivery.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

	Optional<Delivery> findByDeliveryIdAndIsDeletedIsFalse(UUID deliveryId);

	Page<Delivery> findAllByIsDeletedIsFalse(Pageable pageable);

	boolean existsByOrderIdAndIsDeletedIsFalse(UUID orderId);

	@Query("SELECT d.deliveryId FROM Delivery d WHERE d.cdmId = :deliveryManagerId AND d.isDeleted = false")
	List<UUID> findAllDeliveryIdsByCdmId(@Param("deliveryManagerId") UUID deliveryManagerId);

}
