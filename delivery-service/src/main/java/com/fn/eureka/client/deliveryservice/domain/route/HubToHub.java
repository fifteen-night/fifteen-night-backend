package com.fn.eureka.client.deliveryservice.domain.route;

import static com.fn.eureka.client.deliveryservice.domain.util.TimeUtils.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import com.fn.common.global.BaseEntity;
import com.fn.eureka.client.deliveryservice.application.route.dto.request.UpdateHubToHubRequestDto;
import com.fn.eureka.client.deliveryservice.presentation.dto.response.NaverMapDirResponseDto;

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
@Table(name = "p_hub_to_hub" , schema = "delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubToHub extends BaseEntity {

	//TODO : Common BaseEntity Impl 할것

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID hthId;

	//private UUID hthDepartureHubId;

	//private UUID hthDestinationHubId;

	// 임시
	private String departureHubAddress;

	private String arrivalHubAddress;

	@Column(columnDefinition = "TIME")
	private LocalTime hthQuantity;

	@Column(precision = 9,  scale = 2)
	private BigDecimal hthDistance;

	@Builder
	public HubToHub(String departureHubAddress, String arrivalHubAddress, LocalTime hthQuantity, BigDecimal hthDistance) {
		this.departureHubAddress = departureHubAddress;
		this.arrivalHubAddress = arrivalHubAddress;
		this.hthQuantity = hthQuantity;
		this.hthDistance = hthDistance;
	}

	public void updateAddress(UpdateHubToHubRequestDto updateHubToHubRequestDto) {
		Optional.ofNullable(updateHubToHubRequestDto.getDepartureHubAddress()).ifPresent(value -> departureHubAddress = value);
		Optional.ofNullable(updateHubToHubRequestDto.getArrivalHubAddress()).ifPresent(value -> arrivalHubAddress = value);
	}

	public void update(NaverMapDirResponseDto naverMapDirResponseDto) {

		this.hthQuantity = convertTime(
			naverMapDirResponseDto
				.getRoute()
				.getTraoptimal()
				.get(0)
				.getSummary()
				.getDuration()
		);

		this.hthDistance = BigDecimal.valueOf(
			naverMapDirResponseDto
				.getRoute()
				.getTraoptimal()
				.get(0)
				.getSummary()
				.getDistance()
		);

	}
}
