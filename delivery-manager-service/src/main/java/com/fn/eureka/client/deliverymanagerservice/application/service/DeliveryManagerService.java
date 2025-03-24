package com.fn.eureka.client.deliverymanagerservice.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerCreateRequestDto;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerSearchCondition;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerUpdateRequestDto;
import com.fn.eureka.client.deliverymanagerservice.application.dto.response.DeliveryManagerGetResponseDto;
import com.fn.eureka.client.deliverymanagerservice.application.dto.response.DeliveryManagerUpdateResponseDto;

public interface DeliveryManagerService {
	CommonResponse<DeliveryManagerGetResponseDto> createDeliveryManager(DeliveryManagerCreateRequestDto requestDto);

	CommonResponse<DeliveryManagerGetResponseDto> getDeliveryManager(UUID dmId);

	CommonResponse<Page<DeliveryManagerGetResponseDto>> getDeliveryManagers(DeliveryManagerSearchCondition condition, Pageable pageable);

	CommonResponse<DeliveryManagerUpdateResponseDto> updateDeliveryManager(UUID dmId, DeliveryManagerUpdateRequestDto requestDto);

	CommonResponse<Void> deleteDeliveryManager(UUID dmId);

	UUID assignHubDeliveryManager();

	UUID assignCompanyDeliveryManager(UUID hubId);
}