package com.fn.eureka.client.deliveryservice.presentation;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fn.common.global.dto.CommonPageResponse;
import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.deliveryservice.application.HubToHubService;
import com.fn.eureka.client.deliveryservice.application.dto.request.CreateHubToHubRequestDto;
import com.fn.eureka.client.deliveryservice.application.dto.response.CreateHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.response.GetAllHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.response.GetHubToHubResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "허브와 허브 이동관계", description = "허브와 허브 연관관계 설정 컨트롤러")
public class HubToHubController {

	// TODO : 나중에 user 인증인가 완료되면 @AuthenticationPrincipal로 유저 검증

	private final HubToHubService hubToHubService;

	@PostMapping("/hub-to-hubs")
	@Operation(summary = "허브관계 등록", description = "허브 등록은 'MASTER' 만 가능")
	public ResponseEntity<CommonResponse<CreateHubToHubResponseDto>> createRoute(
		@RequestBody @Validated CreateHubToHubRequestDto createHubToHubRequestDto
	) throws Throwable {
		CreateHubToHubResponseDto createHubToHubResponseDto = hubToHubService.createRoute(createHubToHubRequestDto);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath()
			.path("/api/hub-to-hubs")
			.build()
			.toUri();

		return ResponseEntity
			.created(location)
			.body(new CommonResponse<>(SuccessCode.HUBTOHUB_CREATE, createHubToHubResponseDto));
	}

	@GetMapping("/hub-to-hubs/{hubToHubId}")
	@Operation(summary = "허브관계 단건 조회", description = "허브 조회는 '모두' 가능")
	public ResponseEntity<CommonResponse<GetHubToHubResponseDto>> getRoute(@NotNull UUID hubToHubId) {

		GetHubToHubResponseDto getHubToHubResponseDto = hubToHubService.searchOneHubToHub(hubToHubId);

		return ResponseEntity
			.ok()
			.body(new CommonResponse<>(SuccessCode.HUBTOHUB_SEARCH_ONE, getHubToHubResponseDto));
	}

	@GetMapping("/hub-to-hubs")
	@Operation(summary = "허브관계 모든 조회" , description = "허브 조회는 '모두' 가능")
	public ResponseEntity<CommonResponse<CommonPageResponse<GetAllHubToHubResponseDto>>> getAllRoute(
		@PageableDefault(size = 10, sort = "createdAt" , direction = Sort.Direction.ASC) Pageable pageable
	){

		CommonPageResponse<GetAllHubToHubResponseDto> getAllHubToHubResponseDtoCommonPageResponse
			= hubToHubService.searchAllHubToHub(pageable);

		return ResponseEntity
			.ok()
			.body(new CommonResponse<>(SuccessCode.HUBTOHUB_SEARCH_ALL , getAllHubToHubResponseDtoCommonPageResponse));
	}

}
