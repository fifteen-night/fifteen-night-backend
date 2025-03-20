package com.fn.eureka.client.deliveryservice.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.exception.CustomApiException;
import com.fn.eureka.client.deliveryservice.application.DeliveryRepository;
import com.fn.eureka.client.deliveryservice.application.DeliveryService;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.request.CreateDeliveryRequestDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.CreateDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.domain.delivery.Delivery;
import com.fn.eureka.client.deliveryservice.exception.DeliveryException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

	private final DeliveryRepository deliveryRepository;

	@Override
	@Transactional
	public CreateDeliveryResponseDto createDelivery(CreateDeliveryRequestDto createDeliveryRequestDto) {

		Delivery delivery = CreateDeliveryRequestDto.toDelivery(createDeliveryRequestDto);

		// TODO: 중복 검증 예외 추가

		Delivery savedDelivery = deliveryRepository.save(delivery);

		return CreateDeliveryResponseDto.fromDelivery(savedDelivery);
	}
}
