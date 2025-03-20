package com.fn.eureka.client.slackservice.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.slackservice.domain.entity.Slack;

public interface SlackRepository extends JpaRepository<Slack, UUID> {
}
