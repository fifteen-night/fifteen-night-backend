package com.fn.eureka.client.deliveryservice.domain.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonPageResponse;
import com.fn.common.global.exception.CustomApiException;
import com.fn.eureka.client.deliveryservice.application.DeliveryRepository;
import com.fn.eureka.client.deliveryservice.application.DeliveryService;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.request.CreateDeliveryRequestDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.CreateDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.GetAllDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.GetDeliveryResponseDto;
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

	@Override
	public GetDeliveryResponseDto searchOneDelivery(UUID deliveryId) {

		Delivery targetDelivery = findDeliveryById(deliveryId);

		return GetDeliveryResponseDto.fromDelivery(targetDelivery);
	}

	@Override
	public CommonPageResponse<GetAllDeliveryResponseDto> searchAllDelivery(Pageable pageable) {

		Page<Delivery> deliveries = deliveryRepository.findAllByIsDeletedIsFalse(pageable);

		if (deliveries.isEmpty()){
			throw new CustomApiException(DeliveryException.DELIVERY_NOT_FOUND);
		}

		Page<GetAllDeliveryResponseDto> getAllDeliveryResponseDtos = deliveries.map(GetAllDeliveryResponseDto::fromDelivery);

		return new CommonPageResponse<>(getAllDeliveryResponseDtos);
	}

	private Delivery findDeliveryById(UUID deliveryId) {

		Delivery targetDelivery = deliveryRepository.findByDeliveryIdAndIsDeletedIsFalse(deliveryId)
			.orElseThrow(() -> new CustomApiException(DeliveryException.DELIVERY_NOT_FOUND));

		return targetDelivery;
	}
}
