package com.fn.eureka.client.hubservice.hub.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.hubservice.hub.domain.Hub;

public interface HubRepository extends JpaRepository<Hub, UUID> {
}