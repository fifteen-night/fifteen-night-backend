package com.fn.eureka.client.deliveryservice.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fn.eureka.client.deliveryservice.application.HubToHubService;
import com.fn.eureka.client.deliveryservice.application.dto.request.CreateHubToHubRequestDto;
import com.fn.eureka.client.deliveryservice.application.dto.response.CreateHubToHubResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "허브와 허브 이동관계", description = "허브와 허브 연관관계 설정 컨트롤러")
public class HubToHubController {

	// TODO : 나중에 user 인증인가 완료되면 @AuthenticationPrincipal로 유저 검증

	private final HubToHubService hubToHubService;

	@PostMapping("/hub-to-hubs")
	@Operation(summary = "허브관계 등록" , description = "허브 등록은 'MASTER' 만 가능")
	public ResponseEntity<CreateHubToHubResponseDto> createRoute(@RequestBody @Validated CreateHubToHubRequestDto createHubToHubRequestDto) {

	 CreateHubToHubResponseDto createHubToHubResponseDto = hubToHubService.createRoute(createHubToHubRequestDto);


		return ResponseEntity.ok(createHubToHubResponseDto);
	}

}
