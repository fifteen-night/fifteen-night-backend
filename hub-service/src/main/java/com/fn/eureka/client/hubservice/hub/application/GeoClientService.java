package com.fn.eureka.client.hubservice.hub.application;

import com.fn.eureka.client.hubservice.hub.application.dto.response.Point;

public interface GeoClientService {
	Point getPoint(String query);
}