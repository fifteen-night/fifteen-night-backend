package com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fn.common.global.BaseEntity;
import com.fn.eureka.client.deliveryservice.domain.model.delivery.Delivery;
import com.fn.eureka.client.deliveryservice.domain.model.deliveryRouteSequence.DeliveryRouteSequence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery_route", schema = "delivery")
@NoArgsConstructor
@Getter
public class DeliveryRoute extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID deliveryRouteId;

	@OneToOne
	@JoinColumn(name = "deliveryId",  nullable = false)
	private Delivery delivery;

	// 여기에 ARRAY
	@OneToMany(mappedBy = "deliveryRoute" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
	private List<DeliveryRouteSequence> routeSequences = new ArrayList<>();

	@Column(nullable = false)
	private UUID departureHubId;

	@Column(nullable = false)
	private UUID destinationHubId;

	private BigDecimal estimatedDistance;

	@Column(columnDefinition = "TIME")
	private LocalTime estimatedTime;

	private BigDecimal actualDistance;

	@Column(columnDefinition = "TIME")
	private LocalTime actualTime;

	private DeliveryRouteStatus currentStatus;

	// 배송 담당자 ID
	private UUID managerId;
}
