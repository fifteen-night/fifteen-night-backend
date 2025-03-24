package com.fn.eureka.client.deliverymanagerservice.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManager;
import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManagerType;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID>, DeliveryManagerRepositoryCustom {

	// 허브 담당자 목록 → getHubManagers()
	@Query("SELECT dm FROM DeliveryManager dm " +
		"WHERE dm.dmType = 'HUB' AND dm.isDeleted = false " +
		"ORDER BY dm.dmTurn ASC")
	List<DeliveryManager> getHubManagers();

	// 특정 허브의 업체 담당자 목록 → getCompanyManagersByHub()
	@Query("SELECT dm FROM DeliveryManager dm " +
		"WHERE dm.dmType = 'COMPANY' " +
		"  AND dm.dmHubId = :hubId " +
		"  AND dm.isDeleted = false " +
		"ORDER BY dm.dmTurn ASC")
	List<DeliveryManager> getCompanyManagersByHub(@Param("hubId") UUID hubId);

	// 허브별 가장 큰 dmTurn 구하기 (삭제되지 않은 담당자만)
	@Query("""
        SELECT MAX(dm.dmTurn) 
        FROM DeliveryManager dm
        WHERE dm.dmHubId = :hubId
          AND dm.dmType = :dmType
          AND dm.isDeleted = false
        """)
	Integer findMaxTurn(UUID hubId, DeliveryManagerType dmType);

	@Query("""
    SELECT d
    FROM DeliveryManager d
    WHERE d.dmId = :dmId
      AND d.isDeleted = false
""")
	Optional<DeliveryManager> findActiveByDmId(UUID dmId);

	// 허브 관리자가 본인 hubId를 알아내기 위해, userId + isDeleted=false 로 DeliveryManager 조회
	@Query("""
        SELECT d 
        FROM DeliveryManager d
        WHERE d.dmUserId = :dmUserId
          AND d.isDeleted = false
        """)
	Optional<DeliveryManager> findActiveByDmUserId(UUID dmUserId);
}
