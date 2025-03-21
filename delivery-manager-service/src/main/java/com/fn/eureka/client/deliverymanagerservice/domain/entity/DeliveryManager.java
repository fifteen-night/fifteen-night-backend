package com.fn.eureka.client.deliverymanagerservice.domain.entity;

import java.util.UUID;

import com.fn.common.global.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_delivery_manager")
public class DeliveryManager extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private UUID dmId; // 배송 담당자 ID (PK)

	@Column(unique = true)
	private UUID dmUserId; // 유저 ID (p_user)

	@Column
	private UUID dmHubId; // 소속 허브 ID (p_hub)

	@Column
	private String dmSlackId; // 슬랙 ID (p_slack)

	@Enumerated(EnumType.STRING)
	@Column
	private DeliveryManagerType dmType; // 배송 담당자 타입 (HUB, COMPANY)

	@Column
	private int dmTurn; // 배송 순번

	// 생성자 (정적 팩토리 메서드 사용)
	public static DeliveryManager of(UUID dmUserId, UUID dmHubId, String dmSlackId, DeliveryManagerType dmType, int dmTurn) {
		return new DeliveryManager(dmUserId, dmHubId, dmSlackId, dmType, dmTurn);
	}

	private DeliveryManager(UUID dmUserId, UUID dmHubId, String dmSlackId, DeliveryManagerType dmType, int dmTurn) {
		this.dmUserId = dmUserId;
		this.dmHubId = dmHubId;
		this.dmSlackId = dmSlackId;
		this.dmType = dmType;
		this.dmTurn = dmTurn;
	}
}
