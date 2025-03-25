package com.fn.eureka.client.deliveryservice.application.dto.delivery.response;

import java.util.UUID;

import com.fn.eureka.client.deliveryservice.application.dto.deliveryRoute.response.CreateDeliveryRouteResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.deliveryRoute.response.UpdateDeliveryRouteResponseDto;
import com.fn.eureka.client.deliveryservice.domain.model.delivery.Delivery;
import com.fn.eureka.client.deliveryservice.domain.model.delivery.DeliveryStatus;
import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRoute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdateDeliveryResponseDto {

	private UUID deliveryId;
	private UUID orderId;
	private UUID departureHubId;
	private UUID destinationHubId;
	private DeliveryStatus status;
	private String address;
	private String receiverName;
	private UUID receiverSlackId;
	private UUID cdmId;
	private UpdateDeliveryRouteResponseDto deliveryRoute;


	public static UpdateDeliveryResponseDto fromDelivery(Delivery updateDelivery, DeliveryRoute updateDeliveryRoute) {

		return UpdateDeliveryResponseDto.builder()
			.deliveryId(updateDelivery.getDeliveryId())
			.orderId(updateDelivery.getOrderId())
			.departureHubId(updateDelivery.getDepartureHubId())
			.destinationHubId(updateDelivery.getDestinationHubId())
			.status(updateDelivery.getStatus())
			.address(updateDelivery.getAddress())
			.receiverName(updateDelivery.getReceiverName())
			.receiverSlackId(updateDelivery.getReceiverSlackId())
			.cdmId(updateDelivery.getCdmId())
			.deliveryRoute(updateDeliveryRoute != null
				? UpdateDeliveryRouteResponseDto.fromDeliveryRoute(updateDeliveryRoute)
				: null)
			.build();
	}
}
