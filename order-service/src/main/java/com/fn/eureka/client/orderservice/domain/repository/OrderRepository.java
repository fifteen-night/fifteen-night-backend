package com.fn.eureka.client.orderservice.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fn.eureka.client.orderservice.domain.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

	Optional<Order> findByOrderIdAndIsDeletedFalse(UUID orderId);
}
