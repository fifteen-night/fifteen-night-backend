package com.fn.eureka.client.deliveryservice.domain.service;

import static com.fn.eureka.client.deliveryservice.domain.util.TimeUtils.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonPageResponse;
import com.fn.common.global.exception.CustomApiException;
import com.fn.eureka.client.deliveryservice.application.HubToHubRepository;
import com.fn.eureka.client.deliveryservice.application.HubToHubService;
import com.fn.eureka.client.deliveryservice.application.route.dto.request.CreateHubToHubRequestDto;
import com.fn.eureka.client.deliveryservice.application.route.dto.request.UpdateHubToHubRequestDto;
import com.fn.eureka.client.deliveryservice.application.route.dto.response.CreateHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.application.route.dto.response.GetAllHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.application.route.dto.response.GetHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.application.route.dto.response.UpdateHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.domain.route.HubToHub;
import com.fn.eureka.client.deliveryservice.exception.HubToHubException;
import com.fn.eureka.client.deliveryservice.infrastructure.NaverMapService;
import com.fn.eureka.client.deliveryservice.presentation.dto.request.NaverMapDirReqestDto;
import com.fn.eureka.client.deliveryservice.presentation.dto.response.NaverMapDirResponseDto;
import com.fn.eureka.client.deliveryservice.presentation.dto.response.NaverMapGeoResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubToHubServiceImpl implements HubToHubService {

	private final NaverMapService naverMapService;

	private final HubToHubRepository hubToHubRepository;

	private final String ACCEPT_HEADER = "application/json";

	@Value("${NAVER_MAP_API_KEY_ID}")
	private String apiKeyId;

	@Value("${NAVER_MAP_API_KEY}")
	private String apiKey;

	@Override
	@Transactional
	public CreateHubToHubResponseDto createRoute(CreateHubToHubRequestDto createHubToHubRequestDto) {
		HubToHub hub = CreateHubToHubRequestDto.toHubToHub(createHubToHubRequestDto);

		logHubDetails(hub);
	
		// 이미 동일 경로 있는지 확인
		if (hubToHubRepository.existsByDepartureHubAddressAndArrivalHubAddressAndIsDeletedIsFalse(
			hub.getDepartureHubAddress(),
			hub.getArrivalHubAddress()
		)) {
			throw new CustomApiException(HubToHubException.ALREADY_EXISTS_ROUTE);
		}
		
		// 경도 위도 값 뽑기
		String[] departureCoords = extractCoordinates(hub.getDepartureHubAddress());
		String[] arrivalCoords = extractCoordinates(hub.getArrivalHubAddress());

		if (departureCoords == null || arrivalCoords == null) {
			throw new CustomApiException(HubToHubException.NOT_FOUND_COORDS);
		}
		
		// 길찾기
		NaverMapDirResponseDto naverMapDirResponseDto = requestDirection(departureCoords, arrivalCoords);
		
		// 저장 허브 엔티티생성
		HubToHub saveHubToHub = createEntity(hub, naverMapDirResponseDto);

		// 저장
		hubToHubRepository.save(saveHubToHub);

		return CreateHubToHubResponseDto.fromHubToHub(saveHubToHub);
	}

	@Override
	public GetHubToHubResponseDto searchOneHubToHub(UUID hubToHubId) {

		log.info("Search hub to hub with id {}", hubToHubId);

		HubToHub targetHubToHub = findHubToHub(hubToHubId);

		return GetHubToHubResponseDto.fromHubToHub(targetHubToHub);
	}

	@Override
	public CommonPageResponse<GetAllHubToHubResponseDto> searchAllHubToHub(Pageable pageable) {

		Page<HubToHub> hubToHubs = hubToHubRepository.findAllByIsDeletedIsFalse(pageable);

		if (hubToHubs.isEmpty()){
			throw new CustomApiException(HubToHubException.NOT_FOUND_HUBTOHUB);
		}

		Page<GetAllHubToHubResponseDto> getAllHubToHubResponseDtos = hubToHubs.map(GetAllHubToHubResponseDto::fromHubToHub);

		return new CommonPageResponse<>(getAllHubToHubResponseDtos);
	}

	@Override
	@Transactional
	public void softDeleteHubToHub(UUID hubToHubId) {

		HubToHub targetHubToHub = findHubToHub(hubToHubId);

		targetHubToHub.markAsDeleted();
	}

	@Override
	@Transactional
	public UpdateHubToHubResponseDto updateHubToHub(UUID hubToHubId, UpdateHubToHubRequestDto updateHubToHubRequestDto) {

		HubToHub targetHubToHub = findHubToHub(hubToHubId);

		targetHubToHub.updateAddress(updateHubToHubRequestDto);

		// 경도 위도 값 뽑기
		String[] departureCoords = extractCoordinates(targetHubToHub.getDepartureHubAddress());
		String[] arrivalCoords = extractCoordinates(targetHubToHub.getArrivalHubAddress());

		if (departureCoords == null || arrivalCoords == null) {
			throw new CustomApiException(HubToHubException.NOT_FOUND_COORDS);
		}

		// 길찾기
		NaverMapDirResponseDto naverMapDirResponseDto = requestDirection(departureCoords, arrivalCoords);

		targetHubToHub.update(naverMapDirResponseDto);

		return UpdateHubToHubResponseDto.fromHubToHub(targetHubToHub);
	}

	private HubToHub createEntity(HubToHub entity , NaverMapDirResponseDto naverMapDirResponseDto) {

		return HubToHub.builder()
			.departureHubAddress(entity.getDepartureHubAddress())
			.arrivalHubAddress(entity.getArrivalHubAddress())
			.hthQuantity(
				convertTime(
					naverMapDirResponseDto
						.getRoute()
						.getTraoptimal()
						.get(0)
						.getSummary()
						.getDuration()
				)
			)
			.hthDistance(
				BigDecimal.valueOf(
					naverMapDirResponseDto
						.getRoute()
						.getTraoptimal()
						.get(0)
						.getSummary()
						.getDistance()
				)
			)
			.build();
	}

	private void logHubDetails(HubToHub hub) {
		log.info("HubToHub : {} -> {}", hub.getDepartureHubAddress(), hub.getArrivalHubAddress());
	}

	private String[] extractCoordinates(String hubAddress) {
		NaverMapGeoResponseDto response = naverMapService.findLocation(ACCEPT_HEADER, apiKeyId, apiKey, hubAddress);

		if (response.getAddresses() == null || response.getAddresses().isEmpty()) {
			log.warn("좌표 생성 실패: {}", hubAddress);
			return null;
		}

		String x = response.getAddresses().get(0).getX();
		String y = response.getAddresses().get(0).getY();
		log.info("허브 좌표 : {} -> x = {}, y = {}", hubAddress, x, y);

		return new String[] {x, y};
	}

	private NaverMapDirResponseDto requestDirection(String[] departureCoords, String[] arrivalCoords) {
		NaverMapDirReqestDto requestDto = new NaverMapDirReqestDto(
			departureCoords[0] + "," + departureCoords[1],
			arrivalCoords[0] + "," + arrivalCoords[1]
		);

		return naverMapService.findDirection(
				ACCEPT_HEADER,
				apiKeyId,
				apiKey,
				requestDto.getGoal(),
				requestDto.getStart()
			)
			.orElseThrow(() -> new CustomApiException(HubToHubException.NOT_FOUND_DIRECTION));
	}

	private HubToHub findHubToHub(UUID hthId) {

		HubToHub targetHubToHub = hubToHubRepository.findByHthIdAndIsDeletedIsFalse(hthId)
			.orElseThrow(() -> new CustomApiException(HubToHubException.NOT_FOUND_HUBTOHUB));

		return targetHubToHub;
	}
	@Transactional
	public void updateAllRoutes() {

		hubToHubRepository.findAllByIsDeletedIsFalse().forEach(hubToHub -> {

			log.info("전: {}" , hubToHub.getHthQuantity());

			try {
				updateHubToHub(
					hubToHub.getHthId(),
					new UpdateHubToHubRequestDto(
						hubToHub.getDepartureHubAddress(),
						hubToHub.getArrivalHubAddress()
					)
				);
			}catch (Exception e) {
				log.error("HubToHub 경로 업데이트 실패: {}", hubToHub.getHthId(), e);
			}

			log.info("후: {}" , hubToHub.getHthQuantity());
		});
	}

}
