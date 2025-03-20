package com.fn.eureka.client.hubservice.hub.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fn.common.global.BaseEntity;
import com.fn.eureka.client.hubservice.hub_stock.domain.HubStock;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Comment("허브")
@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_hub")
public class Hub extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(SqlTypes.UUID)
	@Comment("허브 ID")
	private UUID hubId;

	@Column(nullable = false)
	@Comment("허브 관리자 ID")
	private UUID hubManagerId;

	@OneToMany(mappedBy = "hsHub", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<HubStock> hubStocks = new ArrayList<>();

	@Column(nullable = false, length = 100)
	@Comment("허브 이름")
	private String hubName;

	@Column(nullable = false, length = 100)
	@Comment("허브 주소")
	private String hubAddress;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Comment("허브 타입")
	private HubType hubType;

	@Column(nullable = false, precision = 9, scale = 6)
	@Comment("위도")
	private BigDecimal hubLatitude;

	@Column(nullable = false, precision = 9, scale = 6)
	@Comment("경도")
	private BigDecimal hubLongitude;

	@Builder
	public Hub(UUID hubManagerId, String hubName, String hubAddress, HubType hubType, BigDecimal hubLatitude,
		BigDecimal hubLongitude) {
		this.hubManagerId = hubManagerId;
		this.hubName = hubName;
		this.hubAddress = hubAddress;
		this.hubType = hubType;
		this.hubLatitude = hubLatitude;
		this.hubLongitude = hubLongitude;
	}

	public void updateHubName(String hubName) {
		this.hubName = hubName;
	}

	public void updateHubType(HubType hubType) {
		this.hubType = hubType;
	}

	public void updateHubManagerId(UUID hubManagerId) {
		this.hubManagerId = hubManagerId;
	}

	public void addHubStock(HubStock hubStock) {
		this.hubStocks.add(hubStock);
	}
}