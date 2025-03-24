package com.fn.eureka.client.deliverymanagerservice.application.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
import com.fn.eureka.client.deliverymanagerservice.application.dto.response.DeliveryManagerUpdateResponseDto;
import com.fn.eureka.client.deliverymanagerservice.application.exception.DeliveryManagerException;
import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManager;
import com.fn.eureka.client.deliverymanagerservice.domain.repository.DeliveryManagerRepository;
import com.fn.eureka.client.deliverymanagerservice.infrastructure.client.HubClient;
import com.fn.eureka.client.deliverymanagerservice.infrastructure.client.UserClient;
import com.fn.eureka.client.deliverymanagerservice.infrastructure.security.RequestUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryManagerServiceImpl implements DeliveryManagerService {

	private final DeliveryManagerRepository deliveryManagerRepository;
	private final UserClient userClient;
	private final HubClient hubClient;

	// 라운드로빈 배정을 위한 마지막 turn 저장용 맵 (임시)
	private final Map<String, Integer> lastAssignedTurnMap = new ConcurrentHashMap<>();

	/**
	 * [라운드로빈] 허브(HUB) 배송 담당자 배정
	 */
	@Transactional
	public UUID assignHubDeliveryManager() {
		// 1) 허브 담당자 목록(dmType=HUB, isDeleted=false) dmTurn ASC
		List<DeliveryManager> managers = deliveryManagerRepository.getHubManagers();

		if (managers.isEmpty()) {
			throw new CustomApiException(DeliveryManagerException.NO_MANAGER_FOUND);
		}

		// 2) key="HUB"로, 마지막 배정 turn을 가져와 +1
		String key = "HUB";
		int lastTurn = lastAssignedTurnMap.getOrDefault(key, -1);
		int candidate = lastTurn + 1;

		// 3) candidate 이상인 turn 보유자를 찾고, 없으면 wrap-around
		DeliveryManager selected = managers.stream()
			.filter(m -> m.getDmTurn() >= candidate)
			.findFirst()
			.orElse(managers.get(0)); // ASC 첫 번째

		// 4) turnMap 갱신
		lastAssignedTurnMap.put(key, selected.getDmTurn());

		// 5) 결과 반환
		return selected.getDmId();
	}

	/**
	 * [라운드로빈] 업체(COMPANY) 배송 담당자 배정
	 *  - 동일 hubId를 가진 COMPANY 담당자 중에서만
	 */
	@Transactional
	public UUID assignCompanyDeliveryManager(UUID hubId) {
		// 1) 해당 hubId + COMPANY, isDeleted=false, dmTurn ASC
		List<DeliveryManager> managers = deliveryManagerRepository.getCompanyManagersByHub(hubId);
		if (managers.isEmpty()) {
			throw new CustomApiException(DeliveryManagerException.NO_MANAGER_FOUND);
		}

		// 2) key="COMPANY-{hubId}"
		String key = "COMPANY-" + hubId;
		int lastTurn = lastAssignedTurnMap.getOrDefault(key, -1);
		int candidate = lastTurn + 1;

		// 3) candidate 이상인 turn 보유자를 찾고, 없으면 wrap-around
		DeliveryManager selected = managers.stream()
			.filter(m -> m.getDmTurn() >= candidate)
			.findFirst()
			.orElse(managers.get(0));

		lastAssignedTurnMap.put(key, selected.getDmTurn());

		return selected.getDmId();
	}

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

		// 3-1) 이미 배송담당자로 등록된 유저인지 확인
		if (deliveryManagerRepository.existsByDmUserId(requestDto.getDmUserId())) {
			throw new CustomApiException(DeliveryManagerException.DUPLICATE_MANAGER);
		}

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
	public CommonResponse<DeliveryManagerUpdateResponseDto> updateDeliveryManager(UUID dmId, DeliveryManagerUpdateRequestDto requestDto) {
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
		DeliveryManagerUpdateResponseDto responseDto = DeliveryManagerUpdateResponseDto.builder()
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

	// 허브 존재 여부 확인
	private void validateHubExists(UUID hubId) {
		try {
			boolean exists = hubClient.checkHub(hubId);
			if (!exists) {
				throw new CustomApiException(DeliveryManagerException.HUB_NOT_FOUND);
			}
		} catch (Exception e) {
			throw new CustomApiException(DeliveryManagerException.HUB_SERVICE_UNAVAILABLE);
		}
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
