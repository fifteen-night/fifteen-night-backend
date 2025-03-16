package com.fn.eureka.client.company.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fn.common.global.exception.NotFoundException;
import com.fn.eureka.client.company.client.HubServiceClient;
import com.fn.eureka.client.company.client.UserServiceClient;
import com.fn.eureka.client.company.dto.CompanyCreateRequestDto;
import com.fn.eureka.client.company.dto.CompanyResponseDto;
import com.fn.eureka.client.company.entity.Company;
import com.fn.eureka.client.company.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

	private final CompanyRepository companyRepository;
	private final UserServiceClient userServiceClient;
	private final HubServiceClient hubServiceClient;

	// 업체 생성
	@Override
	public CompanyResponseDto addCompany(CompanyCreateRequestDto companyCreateRequestDto, String userRole) {
		// TODO 권한 - 유저 ROLE이 허브관리자 또는 업체담당자일 경우만 업체 생성 가능
		// companyHubId라는 허브가 존재하는지 확인
		// boolean existedHubId = hubServiceClient.checkExistHubIdInHubList(companyCreateRequestDto.getCompanyHubId());
		// if (!existedHubId) {
		// 	throw new NotFoundException("해당 허브는 존재하지 않습니다.");
		// }
		// 허브 관리자의 경우, companyManagerId가 존재하는 유저 ID인지 확인
		// boolean existedCompanyManagerId = userServiceClient.checkExistUserIdInUserList(companyCreateRequestDto.getCompanyManagerId());
		// if ("HUB MANAGER".equalsIgnoreCase(userRole) && !existedCompanyManagerId){
		// 	throw new NotFoundException("해당 업체 담당자ID는 존재하지 않습니다.");
		// }
		Company company = companyRepository.save(new Company(companyCreateRequestDto));
		return new CompanyResponseDto(company);
	}

	// 업체 조회
	@Override
	public CompanyResponseDto findTheCompany(UUID companyId) {
		Company company = companyRepository.findById(companyId)
			.orElseThrow(() -> new NotFoundException("해당 업체는 존재하지 않습니다."));
		return new CompanyResponseDto(company);
	}

}
