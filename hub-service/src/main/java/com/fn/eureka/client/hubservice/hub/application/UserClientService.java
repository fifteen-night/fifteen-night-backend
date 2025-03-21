package com.fn.eureka.client.hubservice.hub.application;

import java.util.UUID;

public interface UserClientService {
	boolean checkUserIfManager(UUID userId);
}