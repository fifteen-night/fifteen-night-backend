package com.fn.eureka.client.deliveryservice.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.eureka.client.deliveryservice.application.dto.request.CreateHubToHubRequestDto;
import com.fn.eureka.client.deliveryservice.application.dto.response.CreateHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.domain.HubToHub;
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

	private final String ACCEPT_HEADER = "application/json";

	@Value("${NAVER_MAP_API_KEY_ID}")
	private String apiKeyId;

	@Value("${NAVER_MAP_API_KEY}")
	private String apiKey;

	@Transactional
	public CreateHubToHubResponseDto createRoute(CreateHubToHubRequestDto createHubToHubRequestDto) {

		try {
			HubToHub hub = CreateHubToHubRequestDto.toHubToHub(createHubToHubRequestDto);

			log.info("Created HubToHub: {}", hub.getDepartureHubName());
			log.info("Created HubToHub: {}", hub.getArrivalHubName());

			NaverMapGeoResponseDto departure = naverMapService.findLocation(ACCEPT_HEADER,
				apiKeyId,
				apiKey,
				hub.getDepartureHubName());
			NaverMapGeoResponseDto arrival = naverMapService.findLocation(ACCEPT_HEADER,
				apiKeyId,
				apiKey,
				hub.getArrivalHubName());

			String departureX = null;
			String arrivalX = null;
			String departureY = null;
			String arrivalY = null;

			// 주소가 비어 있지 않은지 확인
			if (departure.getAddresses() != null && !departure.getAddresses().isEmpty()) {
				departureX = departure.getAddresses().get(0).getX();
				departureY = departure.getAddresses().get(0).getY();
				log.info("Departure Location: x = {}, y = {}", departureX, departureY);
			} else {
				log.warn("Departure address not found : {}", hub.getDepartureHubName());
			}

			if (arrival.getAddresses() != null && !arrival.getAddresses().isEmpty()) {
				arrivalX = arrival.getAddresses().get(0).getX();
				arrivalY = arrival.getAddresses().get(0).getY();
				log.info("Arrival Location: x = {}, y = {}", arrivalX, arrivalY);
			} else {
				log.warn("Arrival address not found : {}", hub.getArrivalHubName());
			}

			String goal = departureX + "," + departureY;
			String start = arrivalX + "," + arrivalY;

			NaverMapDirReqestDto naverMapDirReqestDto = new NaverMapDirReqestDto(goal, start);

			NaverMapDirResponseDto naverMapDirResponseDto = naverMapService.findDirection(
				ACCEPT_HEADER,
				apiKeyId,
				apiKey,
				naverMapDirReqestDto.getGoal(),
				naverMapDirReqestDto.getStart()
			);

			if(naverMapDirResponseDto != null ) {
				return CreateHubToHubResponseDto.fromHubToHub(hub, naverMapDirResponseDto);
			}

		} catch (Exception e) {
			log.error("Error : {}", e.getMessage());
		}

		return null;
	}
}
