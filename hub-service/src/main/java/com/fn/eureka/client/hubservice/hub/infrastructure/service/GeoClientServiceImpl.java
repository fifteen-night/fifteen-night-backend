package com.fn.eureka.client.hubservice.hub.infrastructure.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.fn.common.global.exception.CustomApiException;
import com.fn.eureka.client.hubservice.hub.application.GeoClientService;
import com.fn.eureka.client.hubservice.hub.application.dto.response.GeoResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.Point;
import com.fn.eureka.client.hubservice.hub.exception.HubException;
import com.fn.eureka.client.hubservice.hub.infrastructure.client.GeoClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeoClientServiceImpl implements GeoClientService {
	private final GeoClient geoClient;

	public Point getPoint(String address) {
		GeoResponse response = geoClient.getPoint(address);

		if (response.getAddresses() == null || response.getAddresses().isEmpty()) {
			throw new CustomApiException(HubException.HUB_ADDRESS_NOT_FOUND);
		}

		GeoResponse.Address result = response.getAddresses().get(0);
		BigDecimal longitude = new BigDecimal(result.getX());
		BigDecimal latitude = new BigDecimal(result.getY());

		return new Point(latitude, longitude);
	}
}