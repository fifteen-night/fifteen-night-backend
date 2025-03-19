package com.fn.eureka.client.deliveryservice.application;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonPageResponse;
import com.fn.common.global.exception.CustomApiException;
import com.fn.eureka.client.deliveryservice.application.dto.request.CreateHubToHubRequestDto;
import com.fn.eureka.client.deliveryservice.application.dto.response.CreateHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.response.GetAllHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.response.GetHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.domain.HubToHub;
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
public class HubToHubService {

	private final NaverMapService naverMapService;

	private final HubToHubRepository hubToHubRepository;

	private final String ACCEPT_HEADER = "application/json";

	@Value("${NAVER_MAP_API_KEY_ID}")
	private String apiKeyId;

	@Value("${NAVER_MAP_API_KEY}")
	private String apiKey;

	@Transactional
	public CreateHubToHubResponseDto createRoute(CreateHubToHubRequestDto createHubToHubRequestDto) throws Throwable {
		HubToHub hub = CreateHubToHubRequestDto.toHubToHub(createHubToHubRequestDto);

		logHubDetails(hub);

		if (hubToHubRepository.existsByDepartureHubAddressAndArrivalHubAddressAndIsDeletedIsFalse(
			hub.getDepartureHubAddress(),
			hub.getArrivalHubAddress()
		)) {
			throw new CustomApiException(HubToHubException.ALREADY_EXISTS_ROUTE);
		}

		String[] departureCoords = extractCoordinates(hub.getDepartureHubAddress());
		String[] arrivalCoords = extractCoordinates(hub.getArrivalHubAddress());

		if (departureCoords == null || arrivalCoords == null) {
			throw new CustomApiException(HubToHubException.NOT_FOUND_COORDS);
		}

		NaverMapDirResponseDto naverMapDirResponseDto = requestDirection(departureCoords, arrivalCoords);

		HubToHub saveHubToHub = HubToHub.builder()
			.departureHubAddress(hub.getDepartureHubAddress())
			.arrivalHubAddress(hub.getArrivalHubAddress())
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

		hubToHubRepository.save(saveHubToHub);

		return CreateHubToHubResponseDto.fromHubToHub(saveHubToHub);
	}

	public GetHubToHubResponseDto searchOneHubToHub(UUID hubToHubId) {

		HubToHub targetHubToHub = findHubToHub(hubToHubId);

		return GetHubToHubResponseDto.fromHubToHub(targetHubToHub);
	}

	public CommonPageResponse<GetAllHubToHubResponseDto> searchAllHubToHub(Pageable pageable) {

		Page<HubToHub> hubToHubs = hubToHubRepository.findAllByIsDeletedIsFalse(pageable);

		if (hubToHubs.isEmpty()){
			throw new CustomApiException(HubToHubException.NOT_FOUND_HUBTOHUB);
		}

		Page<GetAllHubToHubResponseDto> getAllHubToHubResponseDtos = hubToHubs.map(GetAllHubToHubResponseDto::fromHubToHub);

		return new CommonPageResponse<>(getAllHubToHubResponseDtos);
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

	private NaverMapDirResponseDto requestDirection(String[] departureCoords, String[] arrivalCoords) throws Throwable {
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

	private LocalTime convertTime(int milliseconds) {

		Duration duration = Duration.ofMillis(milliseconds);
		long hours = duration.toHours();
		long minutes = duration.toMinutes() % 60;
		long seconds = duration.getSeconds() % 60;

		return LocalTime.of((int)hours, (int)minutes, (int)seconds);
	}

	private HubToHub findHubToHub(UUID hthId) {

		HubToHub targetHubToHub = hubToHubRepository.findByHthIdAndIsDeletedIsFalse(hthId)
			.orElseThrow(() -> new CustomApiException(HubToHubException.NOT_FOUND_HUBTOHUB));

		return targetHubToHub;
	}

}
