package com.fn.eureka.client.hubservice.hub.application.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class GeoResponse {
	private List<Address> addresses;

	@Getter
	public static class Address {
		private String roadAddress;
		private String jibunAddress;
		private String x; // longitude
		private String y; // latitude
	}
}