package com.fn.eureka.client.deliverymanagerservice.application.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManagerType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryManagerSearchCondition {
	private UUID dmUserId;
	private UUID dmHubId;
	private DeliveryManagerType dmType;
	private String dmSlackId;

	private LocalDateTime createdAtFrom;
	private LocalDateTime createdAtTo;

	private String sortBy;  // createdAt, updatedAt
	private String sortDir; // asc, desc
}
