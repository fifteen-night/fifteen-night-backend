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
import com.fn.eureka.client.deliveryservice.application.delivery.dto.request.UpdateDeliveryRequestDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.CreateDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.GetAllDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.GetDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.delivery.dto.response.UpdateDeliveryResponseDto;
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

		if(deliveryRepository.existsByOrderIdAndIsDeletedIsFalse(delivery.getOrderId())){
			throw new CustomApiException(DeliveryException.ALREADY_EXISTS_DELIVERY);
		}

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

	@Override
	@Transactional
	public void deleteDelivery(UUID deliveryId) {

		Delivery targetDelivery = findDeliveryById(deliveryId);

		targetDelivery.markAsDeleted();
	}

	@Override
	@Transactional
	public UpdateDeliveryResponseDto updateDelivery(UUID deliveryId,
		UpdateDeliveryRequestDto updateDeliveryRequestDto) {

		Delivery targetDelivery = findDeliveryById(deliveryId);

		targetDelivery.update(updateDeliveryRequestDto);

		return UpdateDeliveryResponseDto.fromDelivery(targetDelivery);
	}

	private Delivery findDeliveryById(UUID deliveryId) {

		Delivery targetDelivery = deliveryRepository.findByDeliveryIdAndIsDeletedIsFalse(deliveryId)
			.orElseThrow(() -> new CustomApiException(DeliveryException.DELIVERY_NOT_FOUND));

		return targetDelivery;
	}
}
