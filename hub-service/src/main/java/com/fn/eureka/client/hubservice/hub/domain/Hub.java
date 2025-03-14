package com.fn.eureka.client.hubservice.hub.domain;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Comment("허브")
@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_hub")
public class Hub {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(SqlTypes.UUID)
	@Comment("허브 ID")
	private UUID hubId;

	@Column(nullable = false)
	@Comment("허브 관리자 ID")
	private UUID hubManagerId;

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
}