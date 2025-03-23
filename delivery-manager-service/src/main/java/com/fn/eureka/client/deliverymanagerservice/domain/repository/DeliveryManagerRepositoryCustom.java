package com.fn.eureka.client.deliverymanagerservice.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fn.eureka.client.deliverymanagerservice.application.dto.request.DeliveryManagerSearchCondition;
import com.fn.eureka.client.deliverymanagerservice.domain.entity.DeliveryManager;

public interface DeliveryManagerRepositoryCustom {
	Page<DeliveryManager> search(DeliveryManagerSearchCondition condition, Pageable pageable);
}
