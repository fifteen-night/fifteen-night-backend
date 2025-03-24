package com.fn.eureka.client.deliveryservice.domain.repository;

import java.util.UUID;

import com.fn.eureka.client.deliveryservice.application.dto.delivery.response.CreateDeliveryResponseDto;

public interface DeliveryRepositoryCustom {

	CreateDeliveryResponseDto findDeliveryWithRoute(UUID deliveryId);
}
