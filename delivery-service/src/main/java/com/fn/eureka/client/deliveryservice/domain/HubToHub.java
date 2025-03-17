package com.fn.eureka.client.deliveryservice.domain;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_hub_to_hub")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubToHub {

	//TODO : Common BaseEntity Impl 할것

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID hthId;

	//private UUID hthDepartureHubId;

	//private UUID hthDestinationHubId;

	// 임시
	private String departureHubName;

	private String arrivalHubName;

	@Column(columnDefinition = "TIME")
	private String hthQuantity;

	@Column(precision = 7,  scale = 2)
	private BigDecimal hthDistance;

	@Builder
	public HubToHub(String departureHubName, String arrivalHubName , String hthQuantity, BigDecimal hthDistance) {
		this.departureHubName = departureHubName;
		this.arrivalHubName = arrivalHubName;
		this.hthQuantity = hthQuantity;
		this.hthDistance = hthDistance;
	}

}
