package com.fn.eureka.client.productservice.application.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompanyInfoDto {
	private UUID companyId;
	private String companyName;
	private String companyAddress;
	private String companyType;

	private UUID companyHubId;
	private UUID companyManagerId;
}
