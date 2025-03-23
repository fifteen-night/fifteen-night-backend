package com.fn.eureka.client.companyservice.domain.model;

import java.util.UUID;

import org.hibernate.annotations.Comment;

import com.fn.common.global.BaseEntity;
import com.fn.eureka.client.companyservice.presentation.request.CompanyRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="p_company")
@NoArgsConstructor
@AllArgsConstructor
@Builder
// @DynamicUpdate
public class Company extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Comment("업체 ID")
	private UUID companyId;

	@Column(nullable = false)
	@Comment("업체명")
	private String companyName;

	@Column(nullable = false)
	@Comment("업체 주소")
	private String companyAddress;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Comment("업체 타입")
	private CompanyType companyType;	// SUPPLER, RECEIVER

	@Column(nullable = false)
	@Comment("허브 ID")
	private UUID companyHubId;

	@Column(nullable = false)
	@Comment("업체담당자 ID")
	private UUID companyManagerId;

	public static Company from(CompanyRequestDto companyRequestDto) {
		return Company.builder()
			.companyName(companyRequestDto.getCompanyName())
			.companyAddress(companyRequestDto.getCompanyAddress())
			.companyType(CompanyType.valueOf(companyRequestDto.getCompanyType().toUpperCase())) // 닫는 괄호 추가
			.companyHubId(companyRequestDto.getCompanyHubId())
			.companyManagerId(companyRequestDto.getCompanyManagerId())
			.build();
	}

	public void modifyCompanyInfo(CompanyRequestDto requestDto) {
		this.companyName = requestDto.getCompanyName();
		this.companyAddress = requestDto.getCompanyAddress();
		this.companyType = CompanyType.valueOf(requestDto.getCompanyType().toUpperCase());
		this.companyHubId = requestDto.getCompanyHubId();
		this.companyManagerId = requestDto.getCompanyManagerId();
	}
}
