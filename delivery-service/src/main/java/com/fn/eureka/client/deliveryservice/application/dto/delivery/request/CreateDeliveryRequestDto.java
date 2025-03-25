package com.fn.eureka.client.deliveryservice.application.dto.delivery.request;

import java.util.UUID;

import com.fn.eureka.client.deliveryservice.domain.model.delivery.Delivery;
import com.fn.eureka.client.deliveryservice.domain.model.delivery.DeliveryStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
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
	private String receiverSlackId;

	public static Delivery toDelivery(CreateDeliveryRequestDto createDeliveryRequestDto, UUID deliveryHubId) {

		return Delivery.builder()
			.orderId(createDeliveryRequestDto.getOrderId())
			.departureHubId(createDeliveryRequestDto.getDepartureHubId())
			.destinationHubId(createDeliveryRequestDto.getDestinationHubId())
			.address(createDeliveryRequestDto.getAddress())
			.receiverName(createDeliveryRequestDto.getReceiverName())
			.receiverSlackId(createDeliveryRequestDto.getReceiverSlackId())
			.status(DeliveryStatus.PENDING)    // 배송이 생성되면 기본적으로 PENDING상태
			.cdmId(deliveryHubId)
			.build();
	}
}