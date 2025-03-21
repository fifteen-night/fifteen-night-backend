package com.fn.eureka.client.hubservice.hub.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fn.eureka.client.hubservice.hub.application.dto.response.GeoResponse;
import com.fn.eureka.client.hubservice.hub.config.GeoClientConfig;

@FeignClient(
	name = "geoClient",
	url = "https://naveropenapi.apigw.ntruss.com",
	configuration = GeoClientConfig.class
)
public interface GeoClient {

	@GetMapping("/map-geocode/v2/geocode")
	GeoResponse getPoint(@RequestParam("query") String query);
}