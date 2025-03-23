package com.fn.eureka.client.companyservice.application.dto;

import java.util.UUID;

import com.fn.eureka.client.companyservice.domain.model.Company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponseDto {

	private UUID companyId;
	private String companyName;
	private String companyAddress;
	private String companyType;

	private UUID companyHubId;
	private UUID companyManagerId;

	public static CompanyResponseDto from(Company company) {
		return CompanyResponseDto.builder()
			.companyId(company.getCompanyId())
			.companyName(company.getCompanyName())
			.companyAddress(company.getCompanyAddress())
			.companyType(company.getCompanyType().name())
			.companyHubId(company.getCompanyHubId())
			.companyManagerId(company.getCompanyManagerId())
			.build();
	}
}
