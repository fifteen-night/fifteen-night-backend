package com.fn.eureka.client.deliverymanagerservice.application.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.exception.CustomApiException;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerCreateRequestDto;
import com.fn.eureka.client.deliverymanagerservice.application.dto.response.DeliveryManagerGetResponseDto;
import com.fn.eureka.client.deliverymanagerservice.application.exception.DeliveryManagerException;
import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManager;
import com.fn.eureka.client.deliverymanagerservice.domain.repository.DeliveryManagerRepository;
import com.fn.eureka.client.deliverymanagerservice.infrastructure.client.UserFeignClient;
import com.fn.eureka.client.deliverymanagerservice.infrastructure.security.RequestUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

	private final DeliveryManagerRepository deliveryManagerRepository;
	private final UserFeignClient userFeignClient;

	@Transactional
	public CommonResponse<DeliveryManagerGetResponseDto> createDeliveryManager(DeliveryManagerCreateRequestDto requestDto) {
		RequestUserDetails userDetails = getAuthenticatedUser();

		// 1) 권한 체크
		// 마스터 or 허브 관리자만 생성 가능
		if (!(hasMasterRole(userDetails) || hasHubManagerRole(userDetails))) {
			// 배송 담당자 / 업체 담당자 등이면 여기서 거부
			throw new CustomApiException(DeliveryManagerException.ACCESS_DENIED);
		}

		// 2) 허브 관리자라면, "담당 허브"만 생성 가능
		if (hasHubManagerRole(userDetails)) {
			validateHubAccess(requestDto.getDmHubId(), userDetails.getUserId());
		}

		// 3) 사용자 존재 여부 확인
		validateUserExists(requestDto.getDmUserId());

		// 4) 허브 존재 여부 확인
		validateHubExists(requestDto.getDmHubId());

		// 5) 순번(라운드로빈 Turn) 계산
		int newTurn = Optional.ofNullable(
			deliveryManagerRepository.findMaxTurn(requestDto.getDmHubId(), requestDto.getDmType())
		).orElse(-1) + 1;

		// 6) 엔티티 생성
		DeliveryManager manager = DeliveryManager.of(
			requestDto.getDmUserId(),
			requestDto.getDmHubId(),
			requestDto.getDmSlackId(),
			requestDto.getDmType(),
			newTurn
		);

		// 7) DB 저장
		deliveryManagerRepository.save(manager);

		// 8) 응답 DTO
		DeliveryManagerGetResponseDto responseDto = toDto(manager);
		return new CommonResponse<>(SuccessCode.DELIVERY_MANAGER_CREATED, responseDto);
	}

	@Transactional(readOnly = true)
	public CommonResponse<DeliveryManagerGetResponseDto> getDeliveryManager(UUID deliveryManagerId) {
		RequestUserDetails userDetails = getAuthenticatedUser();

		// 1) 현재 사용자 권한 검사 (MASTER, HUB_MANAGER, DELIVERY_MANAGER 중 하나여야 조회 가능)
		validateReadRole(userDetails);

		// 2) 전달받은 ID로 삭제되지 않은 배송담당자 조회
		DeliveryManager manager = deliveryManagerRepository.findActiveByDmId(deliveryManagerId)
			.orElseThrow(() -> new CustomApiException(DeliveryManagerException.MANAGER_NOT_FOUND));

		// 3) 허브 관리자일 경우, 본인이 담당하는 허브의 배송담당자만 조회 가능
		if (hasHubManagerRole(userDetails)) {
			validateHubAccess(manager.getDmHubId(), userDetails.getUserId());
		}

		// 4) 배송 담당자 본인일 경우, 자신의 정보만 조회 가능
		if (hasDeliveryManagerRole(userDetails)) {
			if (!manager.getDmUserId().toString().equals(userDetails.getUserId())) {
				throw new CustomApiException(DeliveryManagerException.ACCESS_DENIED);
			}
		}

		// 5) 응답 반환
		return new CommonResponse<>(SuccessCode.DELIVERY_MANAGER_FOUND, toDto(manager));
	}

	@Transactional(readOnly = true)
	public CommonResponse<Page<DeliveryManagerGetResponseDto>> getDeliveryManagers(
		String keyword, Pageable pageable) {

		RequestUserDetails userDetails = getAuthenticatedUser();
		Page<DeliveryManager> page;

		// 1) 마스터 권한인 경우: 전체 데이터 + 삭제 여부 관계없이 키워드 기반 조회
		if (hasMasterRole(userDetails)) {
			page = deliveryManagerRepository.findByKeyword(keyword, pageable);

			// 2) 허브 관리자 권한인 경우: 본인이 담당하는 허브의 데이터만 조회 (삭제되지 않은 항목만)
		} else if (hasHubManagerRole(userDetails)) {
			UUID userId = UUID.fromString(userDetails.getUserId());

			// 본인이 어떤 허브의 관리자 역할인지 확인 (해당 허브 ID 가져오기)
			DeliveryManager hubManager = deliveryManagerRepository.findActiveByDmUserId(userId)
				.orElseThrow(() -> new CustomApiException(DeliveryManagerException.MANAGER_NOT_FOUND));

			// 해당 허브에서 키워드로 필터링된 결과 조회
			page = deliveryManagerRepository.findByHubIdAndKeyword(hubManager.getDmHubId(), keyword, pageable);

			// 3) 배송 담당자 권한인 경우: 본인에 대한 데이터만 조회 (삭제되지 않은 항목만)
		} else if (hasDeliveryManagerRole(userDetails)) {
			page = deliveryManagerRepository.findByKeywordAndUserId(
				UUID.fromString(userDetails.getUserId()), keyword, pageable);

			// 4) 그 외 권한 없음
		} else {
			throw new CustomApiException(DeliveryManagerException.ACCESS_DENIED);
		}

		// 5) 조회 결과를 DTO로 변환 후 응답
		return new CommonResponse<>(SuccessCode.DELIVERY_MANAGER_LIST_FOUND, page.map(this::toDto));
	}


	// 인증된 사용자 정보 반환 (없거나 잘못된 경우 예외 발생)
	private RequestUserDetails getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			throw new CustomApiException(DeliveryManagerException.INVALID_AUTHENTICATION);
		}
		Object principal = auth.getPrincipal();
		if (principal instanceof RequestUserDetails userDetails) {
			if (userDetails.getUserId() == null) {
				throw new CustomApiException(DeliveryManagerException.INVALID_AUTHENTICATION);
			}
			return userDetails;
		}
		throw new CustomApiException(DeliveryManagerException.INVALID_AUTHENTICATION);
	}

	// 조회 가능한 권한(MASTER, HUB_MANAGER, DELIVERY_MANAGER)인지 확인
	private void validateReadRole(RequestUserDetails userDetails) {
		if (!hasMasterRole(userDetails) && !hasHubManagerRole(userDetails) && !hasDeliveryManagerRole(userDetails)) {
			throw new CustomApiException(DeliveryManagerException.ACCESS_DENIED);
		}
	}

	// 사용자 존재 여부 확인 (UserFeignClient 호출, 없으면 예외 발생)
	private void validateUserExists(UUID userId) {
		if (!userFeignClient.checkUserExists(userId)) {
			throw new CustomApiException(DeliveryManagerException.USER_NOT_FOUND);
		}
	}

	// 허브 존재 여부 확인 (현재는 항상 true, TODO: HubFeignClient 연동 필요)
	private boolean validateHubExists(UUID hubId) {
		// TODO: 허브 서비스 연동 필요
		return true;
	}

	// 허브 관리자 권한자일 경우, 본인 허브에 대해서만 접근 가능한지 확인
	private void validateHubAccess(UUID hubId, String requestUserId) {
		UUID userId = UUID.fromString(requestUserId);
		DeliveryManager hubManager = deliveryManagerRepository.findActiveByDmUserId(userId)
			.orElseThrow(() -> new CustomApiException(DeliveryManagerException.MANAGER_NOT_FOUND));

		if (!hubManager.getDmHubId().equals(hubId)) {
			throw new CustomApiException(DeliveryManagerException.ACCESS_DENIED);
		}
	}

	// 엔티티를 응답 DTO로 변환
	private DeliveryManagerGetResponseDto toDto(DeliveryManager dm) {
		return DeliveryManagerGetResponseDto.builder()
			.id(dm.getDmId())
			.dmUserId(dm.getDmUserId())
			.dmHubId(dm.getDmHubId())
			.dmSlackId(dm.getDmSlackId())
			.dmType(dm.getDmType())
			.dmTurn(dm.getDmTurn())
			.build();
	}

	// MASTER 권한 보유 여부 확인
	private boolean hasMasterRole(RequestUserDetails userDetails) {
		return userDetails.getAuthorities().stream()
			.anyMatch(auth -> auth.getAuthority().equals("ROLE_MASTER"));
	}

	// HUB_MANAGER 권한 보유 여부 확인
	private boolean hasHubManagerRole(RequestUserDetails userDetails) {
		return userDetails.getAuthorities().stream()
			.anyMatch(auth -> auth.getAuthority().equals("ROLE_HUB_MANAGER"));
	}

	// DELIVERY_MANAGER 권한 보유 여부 확인
	private boolean hasDeliveryManagerRole(RequestUserDetails userDetails) {
		return userDetails.getAuthorities().stream()
			.anyMatch(auth -> auth.getAuthority().equals("ROLE_DELIVERY_MANAGER"));
	}

}
