package com.fn.eureka.client.deliveryservice.application;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.fn.common.global.dto.CommonPageResponse;
import com.fn.eureka.client.deliveryservice.application.route.dto.request.CreateHubToHubRequestDto;
import com.fn.eureka.client.deliveryservice.application.route.dto.request.UpdateHubToHubRequestDto;
import com.fn.eureka.client.deliveryservice.application.route.dto.response.CreateHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.application.route.dto.response.GetAllHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.application.route.dto.response.GetHubToHubResponseDto;
import com.fn.eureka.client.deliveryservice.application.route.dto.response.UpdateHubToHubResponseDto;

public interface HubToHubService {
	CreateHubToHubResponseDto createRoute(CreateHubToHubRequestDto createHubToHubRequestDto);

	GetHubToHubResponseDto searchOneHubToHub(UUID hubToHubId);

	CommonPageResponse<GetAllHubToHubResponseDto> searchAllHubToHub(Pageable pageable);

	void softDeleteHubToHub(UUID hubToHubId);

	UpdateHubToHubResponseDto updateHubToHub(UUID hubToHubId, UpdateHubToHubRequestDto updateHubToHubRequestDto);
}
