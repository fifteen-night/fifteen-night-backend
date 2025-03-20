package com.fn.eureka.client.deliveryservice.application.delivery.dto.request;

import java.util.UUID;

import com.fn.eureka.client.deliveryservice.domain.delivery.Delivery;
import com.fn.eureka.client.deliveryservice.domain.delivery.DeliveryStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateDeliveryRequestDto {

	@NotNull
	private UUID orderId;

	@NotNull
	private UUID departureHubId;

	@NotNull
	private UUID destinationHubId;

	@NotBlank
	private String address;

	@NotBlank
	private String receiverName;

	@NotNull
	private UUID receiverSlackId;

	public static Delivery toDelivery(CreateDeliveryRequestDto createDeliveryRequestDto) {

		return Delivery.builder()
			.orderId(createDeliveryRequestDto.getOrderId())
			.departureHubId(createDeliveryRequestDto.getDepartureHubId())
			.destinationHubId(createDeliveryRequestDto.getDestinationHubId())
			.address(createDeliveryRequestDto.getAddress())
			.receiverName(createDeliveryRequestDto.getReceiverName())
			.receiverSlackId(createDeliveryRequestDto.getReceiverSlackId())
			.status(DeliveryStatus.PENDING)	// 배송이 생성되면 기본적으로 PENDING상태
			.build();
	}
}
