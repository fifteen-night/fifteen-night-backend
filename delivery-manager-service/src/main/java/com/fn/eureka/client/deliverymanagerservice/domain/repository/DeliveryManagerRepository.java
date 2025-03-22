package com.fn.eureka.client.deliverymanagerservice.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManager;
import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManagerType;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID> {

	// 허브별 가장 큰 dmTurn 구하기 (삭제되지 않은 담당자만)
	@Query("""
        SELECT MAX(dm.dmTurn) 
        FROM DeliveryManager dm
        WHERE dm.dmHubId = :hubId
          AND dm.dmType = :dmType
          AND dm.isDeleted = false
        """)
	Integer findMaxTurn(UUID hubId, DeliveryManagerType dmType);

	// 마스터(MASTER): 삭제된 데이터도 포함하여 전체 조회
	@Query("""
        SELECT d 
        FROM DeliveryManager d
        WHERE :keyword IS NULL 
           OR :keyword = ''
           OR LOWER(d.dmSlackId) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(d.dmType) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
	Page<DeliveryManager> findByKeyword(String keyword, Pageable pageable);

	@Query("""
    SELECT d
    FROM DeliveryManager d
    WHERE d.dmId = :dmId
      AND d.isDeleted = false
""")
	Optional<DeliveryManager> findActiveByDmId(UUID dmId);


	// 허브 관리자(HUB_MANAGER):
	@Query("""
        SELECT d 
        FROM DeliveryManager d
        WHERE d.dmHubId = :hubId
          AND d.isDeleted = false
          AND (
                :keyword IS NULL 
             OR :keyword = '' 
             OR LOWER(d.dmSlackId) LIKE LOWER(CONCAT('%', :keyword, '%')) 
             OR LOWER(d.dmType) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        """)
	Page<DeliveryManager> findByHubIdAndKeyword(UUID hubId, String keyword, Pageable pageable);

	// 배송 담당자(DELIVERY_MANAGER):
	@Query("""
        SELECT d
        FROM DeliveryManager d
        WHERE d.dmUserId = :userId
          AND d.isDeleted = false
          AND (
                :keyword IS NULL 
             OR :keyword = ''
             OR LOWER(d.dmSlackId) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(d.dmType) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        """)
	Page<DeliveryManager> findByKeywordAndUserId(UUID userId, String keyword, Pageable pageable);

	// 허브 관리자가 본인 hubId를 알아내기 위해, userId + isDeleted=false 로 DeliveryManager 조회
	@Query("""
        SELECT d 
        FROM DeliveryManager d
        WHERE d.dmUserId = :dmUserId
          AND d.isDeleted = false
        """)
	Optional<DeliveryManager> findActiveByDmUserId(UUID dmUserId);
}
