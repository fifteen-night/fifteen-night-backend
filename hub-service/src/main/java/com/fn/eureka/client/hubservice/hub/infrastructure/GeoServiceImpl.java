package com.fn.eureka.client.hubservice.hub.infrastructure;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.fn.eureka.client.hubservice.hub.application.GeoService;
import com.fn.eureka.client.hubservice.hub.application.dto.GeoResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.Point;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeoServiceImpl implements GeoService {
	private final GeoFeignClient geoFeignClient;

	public Point getPoint(String address) {
		GeoResponse response = geoFeignClient.getPoint(address);

		if (response.getAddresses() == null || response.getAddresses().isEmpty()) {
			throw new RuntimeException("없는 주소입니다.");
		}

		GeoResponse.Address result = response.getAddresses().get(0);
		BigDecimal longitude = new BigDecimal(result.getX());
		BigDecimal latitude = new BigDecimal(result.getY());

		return new Point(latitude, longitude);
	}
}