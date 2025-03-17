package com.fn.eureka.client.company.application.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompanyRequestDto {

	private String companyName;
	private String companyAddress;
	private String companyType;

	private UUID companyHubId;
	private UUID companyManagerId;	// 허브관리자가 지정 또는 유저 본인
}
