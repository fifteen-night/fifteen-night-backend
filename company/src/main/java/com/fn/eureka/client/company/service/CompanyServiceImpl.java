package com.fn.eureka.client.company.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.exception.NotFoundException;
import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.company.client.HubServiceClient;
import com.fn.eureka.client.company.client.UserServiceClient;
import com.fn.eureka.client.company.dto.CompanyRequestDto;
import com.fn.eureka.client.company.dto.CompanyResponseDto;
import com.fn.eureka.client.company.entity.Company;
import com.fn.eureka.client.company.repository.CompanyQueryRepository;
import com.fn.eureka.client.company.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

	private final CompanyRepository companyRepository;
	private final CompanyQueryRepository companyQueryRepository;

	private final UserServiceClient userServiceClient;
	private final HubServiceClient hubServiceClient;

	// 업체 생성
	@Override
	@Transactional
	public CompanyResponseDto addCompany(CompanyRequestDto companyRequestDto, String userRole) {
		// TODO 권한 - 유저 ROLE이 허브관리자 또는 업체담당자일 경우만 업체 생성 가능
		// TODO 질문 - 만약 프론트엔드에서 이미 검증된 데이터를 보낸다면 굳이 필요없을 것 같음.
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
		Company company = companyRepository.save(new Company(companyRequestDto));
		return new CompanyResponseDto(company);
	}

	// 업체 조회
	@Override
	public CompanyResponseDto findTheCompany(UUID companyId) {
		Company company = companyRepository.findById(companyId)
			.orElseThrow(() -> new NotFoundException("해당 업체는 존재하지 않습니다."));
		return new CompanyResponseDto(company);
	}

	// 업체 리스트 조회 (전체, 허브별) + 검색
	@Override
	public Page<CompanyResponseDto> findAllCompaniesByType(UUID hubId, String type, String keyword, int page, int size,
		Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy, String userRole) {
		return companyQueryRepository.findCompaniesByType(hubId, type, keyword, PageUtils.pageable(page, size), sortDirection, sortBy, userRole);
	}

	// 업체 수정
	@Override
	public CompanyResponseDto modifyCompany(UUID companyId, CompanyRequestDto requestDto) {
		Company company = companyRepository.findById(companyId)
			.orElseThrow(() -> new NotFoundException("해당 업체를 찾을 수 없습니다"));
		company.modifyCompanyInfo(requestDto);
		companyRepository.save(company);
		return new CompanyResponseDto(company);
	}

	// 업체 삭제
	@Override
	public void removeCompany(UUID companyId) {
		Company company = companyRepository.findById(companyId)
			.orElseThrow(() -> new NotFoundException("해당 업체를 찾을 수 없습니다"));
		company.markAsDeleted();
		companyRepository.save(company);
	}

}
