package com.fn.eureka.client.deliverymanagerservice.application.dto.response;

import java.util.UUID;

import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManagerType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryManagerCreateResponseDto {
	private final UUID id;
	private final UUID dmUserId;
	private final UUID dmHubId;
	private final String dmSlackId;
	private final DeliveryManagerType dmType;
	private final Integer dmTurn;
}
