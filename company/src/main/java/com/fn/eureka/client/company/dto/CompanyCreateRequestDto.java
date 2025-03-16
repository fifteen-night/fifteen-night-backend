package com.fn.eureka.client.company.dto;

import java.util.UUID;

import com.fn.eureka.client.company.entity.CompanyType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompanyCreateRequestDto {

	private String companyName;
	private String companyAddress;
	private String companyType;

	private UUID companyHubId;
	private UUID companyManagerId;	// 허브관리자가 지정 또는 유저 본인
}
