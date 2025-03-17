package com.fn.eureka.client.hubservice.hub.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fn.eureka.client.hubservice.hub.domain.Hub;
import com.fn.eureka.client.hubservice.hub.domain.repository.HubRepository;

@Repository
public interface JpaHubRepository extends HubRepository, JpaRepository<Hub, UUID> {
}