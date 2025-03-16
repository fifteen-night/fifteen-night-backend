package com.fn.eureka.client.company.dto;

import java.util.UUID;

import com.fn.eureka.client.company.entity.Company;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompanyResponseDto {

	private UUID companyId;
	private String companyName;
	private String companyAddress;
	private String companyType;

	private UUID companyHubId;
	private UUID companyManagerId;

	public CompanyResponseDto(Company company) {
		this.companyId = company.getCompanyId();
		this.companyName = company.getCompanyName();
		this.companyAddress = company.getCompanyAddress();
		this.companyType = company.getCompanyType().name();
		this.companyHubId = company.getCompanyHubId();
		this.companyManagerId = company.getCompanyManagerId();
	}
}
