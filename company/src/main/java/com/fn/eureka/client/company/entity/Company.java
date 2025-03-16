package com.fn.eureka.client.company.entity;

import java.util.UUID;

import com.fn.common.global.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="p_company")
@NoArgsConstructor
// @DynamicUpdate
public class Company extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID companyId;

	@Column(nullable = false)
	private String companyName;

	@Column(nullable = false)
	private String companyAddress;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CompanyType companyType;

	@Column(nullable = false)
	private UUID companyHubId;

	@Column(nullable = false)
	private UUID companyManagerId;

}
