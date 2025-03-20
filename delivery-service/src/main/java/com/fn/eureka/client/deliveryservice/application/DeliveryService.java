package com.fn.eureka.client.deliveryservice.application;

import com.fn.eureka.client.deliveryservice.application.delivery.dto.request.CreateDeliveryRequestDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.CreateDeliveryResponseDto;

public interface DeliveryService {
	CreateDeliveryResponseDto createDelivery(CreateDeliveryRequestDto createDeliveryRequestDto);

}
