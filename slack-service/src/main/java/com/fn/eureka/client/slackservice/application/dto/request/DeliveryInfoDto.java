package com.fn.eureka.client.slackservice.application.dto.request;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryInfoDto {
	private String message;
	private DeliveryData data;

	@Getter
	@NoArgsConstructor
	public static class DeliveryData {
		private UUID deliveryId;
		private UUID orderId;
		private String status;
		private String address;
		private String receiverName;
		private String receiverSlackId;
		private UUID cdmId;
		private DeliveryRoute deliveryRoute;
	}

	@Getter
	@NoArgsConstructor
	public static class DeliveryRoute {
		private String deliveryRouteId;
		private List<Sequence> sequence;
		private String totalTime;
		private double totalDistance;
		private String status;
	}

	@Getter
	@NoArgsConstructor
	public static class Sequence {
		private UUID sequenceId;
		private int sequenceNumber;
		private String departureHubAddress;
		private String arrivalHubAddress;
		private String quantity;
		private double distance;
		private UUID hubDeliveryManagerId;
	}
}