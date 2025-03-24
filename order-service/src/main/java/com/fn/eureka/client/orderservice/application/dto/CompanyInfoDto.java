package com.fn.eureka.client.orderservice.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompanyInfoDto {

	private String message;

	private CompanyData data;
	@Getter
	public static class CompanyData {
		private UUID companyId;
		private String companyName;
		private String companyAddress;
		private String companyType;

		private UUID companyHubId;
		private UUID companyManagerId;
	}
}