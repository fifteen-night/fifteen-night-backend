package com.fn.eureka.client.companyservice.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HubResponseDto {
	private UUID hubId;
	private String hubName;
	private String hubAddress;
	private String hubType;	// enum
	private UUID hubManagerId;
	private BigDecimal hubLatitude;
	private BigDecimal hubLongitude;
}
