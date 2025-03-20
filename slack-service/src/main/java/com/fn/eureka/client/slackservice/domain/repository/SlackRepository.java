package com.fn.eureka.client.slackservice.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fn.eureka.client.slackservice.domain.entity.Slack;

public interface SlackRepository extends JpaRepository<Slack, UUID> {
	@Query("SELECT s FROM Slack s WHERE " +
		"(:keyword IS NULL OR LOWER(s.slackMessage) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	Page<Slack> findByKeyword(String keyword, Pageable pageable);
}
