package com.fn.eureka.client.deliveryservice.application.dto.delivery.response;

import java.util.UUID;

import com.fn.eureka.client.deliveryservice.application.dto.deliveryRoute.response.CreateDeliveryRouteResponseDto;
import com.fn.eureka.client.deliveryservice.domain.model.delivery.Delivery;
import com.fn.eureka.client.deliveryservice.domain.model.delivery.DeliveryStatus;
import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRoute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateDeliveryResponseDto {

	private UUID deliveryId;
	private UUID orderId;
	private UUID departureHubId;
	private UUID destinationHubId;
	private DeliveryStatus status;
	private String address;
	private String receiverName;
	private UUID receiverSlackId;
	private UUID cmdId;
	private CreateDeliveryRouteResponseDto deliveryRoute;


	public static CreateDeliveryResponseDto fromDelivery(Delivery savedDelivery, DeliveryRoute savedDeliveryRoute) {

		return CreateDeliveryResponseDto.builder()
			.deliveryId(savedDelivery.getDeliveryId())
			.orderId(savedDelivery.getOrderId())
			.departureHubId(savedDelivery.getDepartureHubId())
			.destinationHubId(savedDelivery.getDestinationHubId())
			.status(savedDelivery.getStatus())
			.address(savedDelivery.getAddress())
			.receiverName(savedDelivery.getReceiverName())
			.receiverSlackId(savedDelivery.getReceiverSlackId())
			.cmdId(savedDelivery.getCdmId())
			.deliveryRoute(savedDeliveryRoute != null
			? CreateDeliveryRouteResponseDto.fromDeliveryRoute(savedDeliveryRoute)
			: null)
			.build();
	}
}
