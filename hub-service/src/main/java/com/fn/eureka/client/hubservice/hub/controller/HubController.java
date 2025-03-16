package com.fn.eureka.client.hubservice.hub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fn.eureka.client.hubservice.hub.dto.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.dto.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.service.HubService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hubs")
public class HubController {
	private final HubService hubService;

	@PostMapping
	public ResponseEntity<CreateHubResponse> createHub(@RequestBody CreateHubRequest request) {

		CreateHubResponse response = hubService.createHub(request);

		return ResponseEntity.ok(response);
	}
}