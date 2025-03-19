package com.fn.eureka.client.productservice.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fn.eureka.client.productservice.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
	Optional<Product> findByProductIdAndIsDeletedFalse(UUID productId);
}
