package com.fn.eureka.client.hubservice.hub.service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fn.eureka.client.hubservice.hub.dto.Point;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeoService {

	private static final String NAVER_GEOCODE_API = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
	@Value("${naver.api.client-id}")
	private String clientId;
	@Value("${naver.api.client-secret}")
	private String clientSecret;

	public Point getPoint(String address) {
		try {
			String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
			String url = NAVER_GEOCODE_API + "?query=" + encodedAddress;

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
			headers.set("X-NCP-APIGW-API-KEY", clientSecret);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(response.getBody());

			JsonNode addresses = root.path("addresses");
			if (addresses.isEmpty()) {
				throw new RuntimeException("주소를 찾을 수 없습니다.");
			}

			JsonNode first = addresses.get(0);
			BigDecimal longitude = new BigDecimal(first.get("x").asText());
			BigDecimal latitude = new BigDecimal(first.get("y").asText());

			return new Point(latitude, longitude);
		} catch (Exception e) {
			throw new RuntimeException("GeoService 실패: " + e.getMessage());
		}
	}
}