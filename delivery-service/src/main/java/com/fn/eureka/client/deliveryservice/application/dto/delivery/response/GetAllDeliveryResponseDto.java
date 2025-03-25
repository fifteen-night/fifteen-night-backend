package com.fn.eureka.client.deliveryservice.application.dto.delivery.response;

import java.util.UUID;

import com.fn.eureka.client.deliveryservice.application.dto.deliveryRoute.response.GetAllDeliveryRouteResponseDto;
import com.fn.eureka.client.deliveryservice.domain.model.delivery.Delivery;
import com.fn.eureka.client.deliveryservice.domain.model.delivery.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GetAllDeliveryResponseDto {

	private UUID deliveryId;
	private UUID orderId;
	private UUID departureHubId;
	private UUID destinationHubId;
	private DeliveryStatus status;
	private String address;
	private String receiverName;
	private String receiverSlackId;
	private UUID cdmId;
	private GetAllDeliveryRouteResponseDto deliveryRoute;

	public static GetAllDeliveryResponseDto fromDelivery(Delivery delivery) {

		return GetAllDeliveryResponseDto.builder()
			.deliveryId(delivery.getDeliveryId())
			.orderId(delivery.getOrderId())
			.departureHubId(delivery.getDepartureHubId())
			.destinationHubId(delivery.getDestinationHubId())
			.status(delivery.getStatus())
			.address(delivery.getAddress())
			.receiverName(delivery.getReceiverName())
			.receiverSlackId(delivery.getReceiverSlackId())
			.cdmId(delivery.getCdmId())
			.deliveryRoute(GetAllDeliveryRouteResponseDto.fromDeliveryRoute(delivery.getDeliveryRoute()))
			.build();
	}
}