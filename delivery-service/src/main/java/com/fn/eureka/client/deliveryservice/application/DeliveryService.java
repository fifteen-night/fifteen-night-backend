package com.fn.eureka.client.deliveryservice.application;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.fn.common.global.dto.CommonPageResponse;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.request.CreateDeliveryRequestDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.CreateDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.GetAllDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.GetDeliveryResponseDto;

public interface DeliveryService {
	CreateDeliveryResponseDto createDelivery(CreateDeliveryRequestDto createDeliveryRequestDto);

	GetDeliveryResponseDto searchOneDelivery(UUID deliveryId);

}
