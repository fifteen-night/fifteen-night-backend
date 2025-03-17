package com.fn.eureka.client.deliveryservice.infrastructure;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.fn.eureka.client.deliveryservice.presentation.dto.response.NaverMapDirResponseDto;
import com.fn.eureka.client.deliveryservice.presentation.dto.response.NaverMapGeoResponseDto;

import jakarta.validation.constraints.NotBlank;

@FeignClient(name = "NaverMapService", url = "${BASE_URL}")
public interface NaverMapService {

	@GetMapping("${MAP_GEOCODE_URL}")
	NaverMapGeoResponseDto findLocation(
		@RequestHeader("Accept") String accept,
		@RequestHeader("x-ncp-apigw-api-key-id") String apiKeyId,
		@RequestHeader("x-ncp-apigw-api-key") String apiKey,
		@RequestParam("query") @NotBlank String hthDepartureHubId);

	@GetMapping("${MAP_DIRECTION}")
	NaverMapDirResponseDto findDirection(@RequestHeader("Accept") String acceptHeader,
		@RequestHeader("x-ncp-apigw-api-key-id") String apiKeyId,
		@RequestHeader("x-ncp-apigw-api-key") String apiKey,
		@RequestParam("goal") String goal,
		@RequestParam("start") String start
	);

}
