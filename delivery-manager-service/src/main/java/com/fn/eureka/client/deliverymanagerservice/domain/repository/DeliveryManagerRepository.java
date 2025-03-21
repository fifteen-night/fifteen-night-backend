package com.fn.eureka.client.deliverymanagerservice.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManager;
import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManagerType;

import feign.Param;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID> {
	// 허브별 가장 큰 dmTurn 구하기 (삭제되지 않은 담당자만)
	@Query("SELECT MAX(dm.dmTurn) FROM DeliveryManager dm " +
		"WHERE dm.dmHubId = :hubId " +
		"AND dm.dmType = :dmType " +
		"AND dm.isDeleted = false")
	Integer findMaxTurn(@Param("hubId") UUID hubId,
		@Param("dmType") DeliveryManagerType dmType);

}
