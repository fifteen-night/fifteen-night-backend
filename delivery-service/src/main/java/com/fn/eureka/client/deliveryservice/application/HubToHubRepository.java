package com.fn.eureka.client.deliveryservice.application;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.deliveryservice.domain.HubToHub;

public interface HubToHubRepository extends JpaRepository<HubToHub, UUID> {
}
