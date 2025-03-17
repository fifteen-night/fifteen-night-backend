package com.fn.eureka.client.hubservice.hub.application;

import com.fn.eureka.client.hubservice.hub.application.dto.Point;

public interface GeoService {
	Point getPoint(String query);
}