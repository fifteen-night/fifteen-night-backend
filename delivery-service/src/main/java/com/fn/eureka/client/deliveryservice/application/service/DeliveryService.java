package com.fn.eureka.client.deliveryservice.application.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.fn.common.global.dto.CommonPageResponse;
import com.fn.eureka.client.deliveryservice.application.dto.delivery.request.CreateDeliveryRequestDto;
import com.fn.eureka.client.deliveryservice.application.dto.delivery.request.UpdateDeliveryRequestDto;
import com.fn.eureka.client.deliveryservice.application.dto.delivery.response.CreateDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.delivery.response.GetAllDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.delivery.response.GetDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.delivery.response.UpdateDeliveryResponseDto;

public interface DeliveryService {
	CreateDeliveryResponseDto createDelivery(CreateDeliveryRequestDto createDeliveryRequestDto);

	GetDeliveryResponseDto searchOneDelivery(UUID deliveryId);

	CommonPageResponse<GetAllDeliveryResponseDto> searchAllDelivery(Pageable pageable);

	void deleteDelivery(UUID deliveryId);

	UpdateDeliveryResponseDto updateDelivery(UUID deliveryId, UpdateDeliveryRequestDto updateDeliveryRequestDto);

}
