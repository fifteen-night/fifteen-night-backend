package com.fn.eureka.client.deliverymanagerservice.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.exception.CustomApiException;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerCreateRequestDto;
import com.fn.eureka.client.deliverymanagerservice.application.dto.response.DeliveryManagerCreateResponseDto;
import com.fn.eureka.client.deliverymanagerservice.application.exception.DeliveryManagerException;
import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManager;
import com.fn.eureka.client.deliverymanagerservice.domain.repository.DeliveryManagerRepository;
import com.fn.eureka.client.deliverymanagerservice.infrastructure.client.UserFeignClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

	private final DeliveryManagerRepository deliveryManagerRepository;
	private final UserFeignClient userFeignClient;

	@Transactional
	public CommonResponse<DeliveryManagerCreateResponseDto> createDeliveryManager(DeliveryManagerCreateRequestDto requestDto) {
		// 1. 사용자 존재 여부 확인
		boolean userExists = userFeignClient.checkUserExists(requestDto.getDmUserId());
		if (!userExists) {
			throw new CustomApiException(DeliveryManagerException.USER_NOT_FOUND);
		}

		// TODO 허브 FeignClient 통신
		// 2. 허브 체크 (임시로 true)
		boolean hubExists = true;
		if (!hubExists) {
			throw new CustomApiException(DeliveryManagerException.HUB_NOT_FOUND);
		}

		// 3. 현재 허브 & 담당자타입(dmType)에 속한 DM 중 최대 turn 찾기
		Integer maxTurn = deliveryManagerRepository.findMaxTurn(requestDto.getDmHubId(), requestDto.getDmType());
		if (maxTurn == null) {
			maxTurn = -1; // 아직 담당자가 없으면 -1로 간주
		}
		int newTurn = maxTurn + 1; // 새 담당자는 "가장 마지막 순번"

		// 4. DeliveryManager 엔티티 생성
		DeliveryManager deliveryManager = DeliveryManager.of(
			requestDto.getDmUserId(),
			requestDto.getDmHubId(),
			requestDto.getDmSlackId(),
			requestDto.getDmType(),
			newTurn
		);

		// 5. DB 저장
		deliveryManagerRepository.save(deliveryManager);

		// 6. 응답 DTO 생성
		DeliveryManagerCreateResponseDto responseDto = DeliveryManagerCreateResponseDto.builder()
			.id(deliveryManager.getDmId())
			.dmUserId(deliveryManager.getDmUserId())
			.dmHubId(deliveryManager.getDmHubId())
			.dmSlackId(deliveryManager.getDmSlackId())
			.dmType(deliveryManager.getDmType())
			.dmTurn(deliveryManager.getDmTurn())
			.build();

		return new CommonResponse<>(SuccessCode.DELIVERY_MANAGER_CREATED, responseDto);
	}

}
