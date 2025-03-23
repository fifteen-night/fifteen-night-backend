package com.fn.eureka.client.deliverymanagerservice.application.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.exception.CustomApiException;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerCreateRequestDto;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerSearchCondition;
import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerUpdateRequestDto;
import com.fn.eureka.client.deliverymanagerservice.application.dto.response.DeliveryManagerGetResponseDto;
import com.fn.eureka.client.deliverymanagerservice.application.exception.DeliveryManagerException;
import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManager;
import com.fn.eureka.client.deliverymanagerservice.domain.repository.DeliveryManagerRepository;
import com.fn.eureka.client.deliverymanagerservice.infrastructure.client.UserClient;
import com.fn.eureka.client.deliverymanagerservice.infrastructure.security.RequestUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryManagerServiceImpl implements DeliveryManagerService {

	private final DeliveryManagerRepository deliveryManagerRepository;
	private final UserClient userClient;

	@Transactional
	public CommonResponse<DeliveryManagerGetResponseDto> createDeliveryManager(DeliveryManagerCreateRequestDto requestDto) {
		RequestUserDetails userDetails = getAuthenticatedUser();

		// 1) 권한 체크 - 마스터 또는 허브 관리자만 생성 가능
		if (!(hasMasterRole(userDetails) || hasHubManagerRole(userDetails))) {
			throw new CustomApiException(DeliveryManagerException.ACCESS_DENIED);
		}

		// 2) 허브 관리자의 경우, 자신의 허브만 생성 가능
		if (hasHubManagerRole(userDetails)) {
			validateHubAccess(requestDto.getDmHubId(), userDetails.getUserId());
		}

		// 3) 사용자 존재 여부 확인
		validateUserExists(requestDto.getDmUserId());

		// 4) 허브 존재 여부 확인
		validateHubExists(requestDto.getDmHubId());

		// 5) 순번 계산 (Turn: 동일 허브 및 타입 내 최대값 + 1)
		int newTurn = Optional.ofNullable(
			deliveryManagerRepository.findMaxTurn(requestDto.getDmHubId(), requestDto.getDmType())
		).orElse(-1) + 1;

		// 6) 배송 관리자 엔티티 생성
		DeliveryManager manager = DeliveryManager.of(
			requestDto.getDmUserId(),
			requestDto.getDmHubId(),
			requestDto.getDmSlackId(),
			requestDto.getDmType(),
			newTurn
		);

		// 7) DB 저장
		deliveryManagerRepository.save(manager);

		// 8) DTO 응답 생성 및 반환
		DeliveryManagerGetResponseDto responseDto = DeliveryManagerGetResponseDto.builder()
			.id(manager.getDmId())
			.dmUserId(manager.getDmUserId())
			.dmHubId(manager.getDmHubId())
			.dmSlackId(manager.getDmSlackId())
			.dmType(manager.getDmType())
			.dmTurn(manager.getDmTurn())
			.build();

		return new CommonResponse<>(SuccessCode.DELIVERY_MANAGER_CREATED, responseDto);
	}

	@Transactional(readOnly = true)
	public CommonResponse<DeliveryManagerGetResponseDto> getDeliveryManager(UUID dmId) {
		RequestUserDetails userDetails = getAuthenticatedUser();

		// 1) 조회 권한 확인
		validateReadRole(userDetails);

		// 2) 삭제되지 않은 배송 관리자 정보 조회
		DeliveryManager manager = deliveryManagerRepository.findActiveByDmId(dmId)
			.orElseThrow(() -> new CustomApiException(DeliveryManagerException.MANAGER_NOT_FOUND));

		// 3) 허브 관리자는 본인 허브만 접근 가능
		if (hasHubManagerRole(userDetails)) {
			validateHubAccess(manager.getDmHubId(), userDetails.getUserId());
		}

		// 4) 배송 담당자는 본인 정보만 조회 가능
		if (hasDeliveryManagerRole(userDetails)) {
			if (!manager.getDmUserId().toString().equals(userDetails.getUserId())) {
				throw new CustomApiException(DeliveryManagerException.ACCESS_DENIED);
			}
		}

		// 5) DTO 생성 및 응답 반환
		DeliveryManagerGetResponseDto responseDto = DeliveryManagerGetResponseDto.builder()
			.id(manager.getDmId())
			.dmUserId(manager.getDmUserId())
			.dmHubId(manager.getDmHubId())
			.dmSlackId(manager.getDmSlackId())
			.dmType(manager.getDmType())
			.dmTurn(manager.getDmTurn())
			.build();

		return new CommonResponse<>(SuccessCode.DELIVERY_MANAGER_FOUND, responseDto);
	}

	@Transactional(readOnly = true)
	public CommonResponse<Page<DeliveryManagerGetResponseDto>> getDeliveryManagers(
		DeliveryManagerSearchCondition condition, Pageable pageable) {
		RequestUserDetails userDetails = getAuthenticatedUser();

		// 1) 조회 권한 확인 (MASTER, HUB_MANAGER, DELIVERY_MANAGER 중 하나여야 함)
		validateReadRole(userDetails);

		// 2) 배송 담당자는 본인 정보만 조회 가능하므로 userId 조건 강제 세팅
		if (hasDeliveryManagerRole(userDetails)) {
			condition.setDmUserId(UUID.fromString(userDetails.getUserId()));
		}

		// 3) 허브 관리자는 본인 허브 소속만 검색 가능하므로 hubId 조건 강제 세팅
		if (hasHubManagerRole(userDetails)) {
			UUID userId = UUID.fromString(userDetails.getUserId());
			DeliveryManager hubManager = deliveryManagerRepository.findActiveByDmUserId(userId)
				.orElseThrow(() -> new CustomApiException(DeliveryManagerException.MANAGER_NOT_FOUND));
			condition.setDmHubId(hubManager.getDmHubId());
		}

		// 4) 페이지 사이즈 유효성 검사 (10, 30, 50만 허용)
		int size = pageable.getPageSize();
		if (size != 10 && size != 30 && size != 50) {
			pageable = PageRequest.of(pageable.getPageNumber(), 10, pageable.getSort());
		}

		// 5) QueryDSL을 이용한 조건 검색 실행
		Page<DeliveryManager> result = deliveryManagerRepository.search(condition, pageable);

		// 6) Entity → DTO 매핑
		Page<DeliveryManagerGetResponseDto> responsePage = result.map(manager -> DeliveryManagerGetResponseDto.builder()
			.id(manager.getDmId())
			.dmUserId(manager.getDmUserId())
			.dmHubId(manager.getDmHubId())
			.dmSlackId(manager.getDmSlackId())
			.dmType(manager.getDmType())
			.dmTurn(manager.getDmTurn())
			.build());

		// 7) 응답 반환
		return new CommonResponse<>(SuccessCode.DELIVERY_MANAGER_LIST_FOUND, responsePage);
	}


	@Transactional
	public CommonResponse<DeliveryManagerGetResponseDto> updateDeliveryManager(UUID dmId, DeliveryManagerUpdateRequestDto requestDto) {
		RequestUserDetails userDetails = getAuthenticatedUser();

		// 1) 수정 권한 확인 - 마스터 또는 허브 관리자만 수정 가능
		if (!(hasMasterRole(userDetails) || hasHubManagerRole(userDetails))) {
			throw new CustomApiException(DeliveryManagerException.ACCESS_DENIED);
		}

		// 2) 삭제되지 않은 배송 관리자 조회
		DeliveryManager manager = deliveryManagerRepository.findActiveByDmId(dmId)
			.orElseThrow(() -> new CustomApiException(DeliveryManagerException.MANAGER_NOT_FOUND));

		// 3) 허브 관리자는 본인 허브만 수정 가능
		if (hasHubManagerRole(userDetails)) {
			validateHubAccess(manager.getDmHubId(), userDetails.getUserId());
		}

		// 4) 정보 수정
		manager.updateDeliveryManager(requestDto);

		// 5) DTO 반환
		DeliveryManagerGetResponseDto responseDto = DeliveryManagerGetResponseDto.builder()
			.id(manager.getDmId())
			.dmUserId(manager.getDmUserId())
			.dmHubId(manager.getDmHubId())
			.dmSlackId(manager.getDmSlackId())
			.dmType(manager.getDmType())
			.dmTurn(manager.getDmTurn())
			.build();

		return new CommonResponse<>(SuccessCode.DELIVERY_MANAGER_UPDATED, responseDto);
	}

	@Transactional
	public CommonResponse<Void> deleteDeliveryManager(UUID dmId) {
		RequestUserDetails userDetails = getAuthenticatedUser();

		// 1) 삭제 대상 조회 (삭제되지 않은 배송 관리자만)
		DeliveryManager manager = deliveryManagerRepository.findActiveByDmId(dmId)
			.orElseThrow(() -> new CustomApiException(DeliveryManagerException.MANAGER_NOT_FOUND));

		// 2) 권한 확인
		if (hasMasterRole(userDetails)) {
			// 마스터는 전체 삭제 가능
			manager.markAsDeleted();
		} else if (hasHubManagerRole(userDetails)) {
			// 허브 관리자는 본인 허브 소속만 삭제 가능
			validateHubAccess(manager.getDmHubId(), userDetails.getUserId());
			manager.markAsDeleted();
		} else {
			throw new CustomApiException(DeliveryManagerException.ACCESS_DENIED);
		}

		return new CommonResponse<>(SuccessCode.DELIVERY_MANAGER_DELETED, null);
	}


	// 현재 인증된 사용자 정보 조회
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

	// 조회 가능한 권한인지 검사 (MASTER, HUB_MANAGER, DELIVERY_MANAGER)
	private void validateReadRole(RequestUserDetails userDetails) {
		if (!hasMasterRole(userDetails) && !hasHubManagerRole(userDetails) && !hasDeliveryManagerRole(userDetails)) {
			throw new CustomApiException(DeliveryManagerException.ACCESS_DENIED);
		}
	}

	// 사용자 존재 여부 확인
	private void validateUserExists(UUID userId) {
		if (!userClient.checkUserExists(userId)) {
			throw new CustomApiException(DeliveryManagerException.USER_NOT_FOUND);
		}
	}

	// 허브 존재 여부 확인 (현재는 항상 true, TODO: HubFeignClient 연동 필요)
	private boolean validateHubExists(UUID hubId) {
		// TODO: 허브 서비스 연동 필요
		return true;
	}

	// 허브 관리자 권한자의 접근 권한 검사
	private void validateHubAccess(UUID hubId, String requestUserId) {
		UUID userId = UUID.fromString(requestUserId);
		DeliveryManager hubManager = deliveryManagerRepository.findActiveByDmUserId(userId)
			.orElseThrow(() -> new CustomApiException(DeliveryManagerException.MANAGER_NOT_FOUND));

		if (!hubManager.getDmHubId().equals(hubId)) {
			throw new CustomApiException(DeliveryManagerException.ACCESS_DENIED);
		}
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
