package com.fn.eureka.client.hubservice.hub.presentation;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fn.eureka.client.hubservice.hub.application.HubService;
import com.fn.eureka.client.hubservice.hub.application.dto.request.CreateHubRequest;
import com.fn.eureka.client.hubservice.hub.application.dto.response.CreateHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadHubResponse;
import com.fn.eureka.client.hubservice.hub.application.dto.response.SearchHubResponse;

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

	@GetMapping("/{hubId}")
	public ResponseEntity<ReadHubResponse> readHub(@PathVariable("hubId") UUID hubId) {

		ReadHubResponse response = hubService.readHub(hubId);

		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<SearchHubResponse> searchHub(Pageable pageable,
		@RequestParam(value = "hubName", required = false) String hubName) {

		SearchHubResponse response = hubService.searchHub(pageable, hubName);

		return ResponseEntity.ok(response);
	}
}