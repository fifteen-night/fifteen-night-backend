package com.fn.eureka.client.hubservice.hub.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub.infrastructure.HubRepositoryCustom;

public interface HubRepository extends JpaRepository<Hub, UUID>, HubRepositoryCustom {
}