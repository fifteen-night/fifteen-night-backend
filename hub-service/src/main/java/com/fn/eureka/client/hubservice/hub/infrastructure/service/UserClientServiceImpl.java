package com.fn.eureka.client.hubservice.hub.infrastructure.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.hubservice.hub.application.UserClientService;
import com.fn.eureka.client.hubservice.hub.application.dto.response.ReadUserResponse;
import com.fn.eureka.client.hubservice.hub.infrastructure.client.UserClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClientService {

	private final UserClient userClient;

	@Override
	public boolean checkUserIfManager(UUID userId) {
		CommonResponse<ReadUserResponse> response = userClient.readUser(userId);
		ReadUserResponse data = response.getData();

		return data.getUserRole().equals("HUB_MANAGER");
	}
}