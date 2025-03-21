package com.fn.eureka.client.deliverymanagerservice.application.dto.request;

import java.util.UUID;

import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManagerType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryManagerCreateRequestDto {
	private UUID dmUserId;
	private UUID dmHubId;
	private String dmSlackId;
	private DeliveryManagerType dmType;
}
