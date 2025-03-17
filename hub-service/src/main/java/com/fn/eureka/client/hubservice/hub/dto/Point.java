package com.fn.eureka.client.hubservice.hub.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Point {
	private BigDecimal latitude;
	private BigDecimal longitude;
}