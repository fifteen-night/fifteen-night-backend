package com.fn.eureka.client.hubservice.hub.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fn.eureka.client.hubservice.hub.domain.Hub;

public interface HubRepositoryCustom {
	Page<Hub> searchHubs(Pageable pageable, String hubName);
}