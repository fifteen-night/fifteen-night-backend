package com.fn.eureka.client.deliveryservice.application.dto.delivery.response;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class QueryAllDeliveriesResponseDto {

	private List<UUID> deliveryIds;

	public static QueryAllDeliveriesResponseDto fromQuery(List<UUID> deliveryIds) {

		return QueryAllDeliveriesResponseDto.builder()
			.deliveryIds(deliveryIds)
			.build();
	}
}
