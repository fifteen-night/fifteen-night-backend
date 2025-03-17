package com.fn.eureka.client.company.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HubResponseDto {

	private UUID hubId;
	private String hubName;
	private String hubAddress;
	private String hubType;
	private UUID hubManager;
}
