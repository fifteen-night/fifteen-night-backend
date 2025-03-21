package com.fn.eureka.client.orderservice.presentation.dto;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequestDto {
	private UUID orderSupplyCompanyId;	// 공급업체
	private UUID orderReceiveCompanyId; // 주문자 = 수령업체
	private UUID orderProductId;	// 상품ID
	private Integer orderProductQuantity;	// 상품 수량
	private Timestamp orderDeadline;	// 상품 납품기한
	private String orderRequirement;	// 상품 요구사항
}
