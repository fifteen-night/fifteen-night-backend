package com.fn.eureka.client.companyservice.infrastructure.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.exception.CustomApiException;
import com.fn.common.global.exception.NotFoundException;
import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.companyservice.application.dto.HubResponseDto;
import com.fn.eureka.client.companyservice.application.dto.UserResponseDto;
import com.fn.eureka.client.companyservice.domain.repository.CompanyQueryRepository;
import com.fn.eureka.client.companyservice.domain.service.CompanyService;
import com.fn.eureka.client.companyservice.infrastructure.exception.CompanyException;
import com.fn.eureka.client.companyservice.presentation.request.CompanyRequestDto;
import com.fn.eureka.client.companyservice.application.dto.CompanyResponseDto;
import com.fn.eureka.client.companyservice.domain.model.Company;
import com.fn.eureka.client.companyservice.domain.repository.CompanyRepository;
import com.fn.eureka.client.companyservice.infrastructure.client.HubServiceClient;
import com.fn.eureka.client.companyservice.infrastructure.client.UserServiceClient;

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
	public CompanyResponseDto addCompany(CompanyRequestDto companyRequestDto, String userRole, UUID userId, String userName) {
		// TODO kafka를 이용해서 메세징 - 나 company 데이터 받았으니까 맞는지 확인해줘! 메세지 보내고 업체 생성하되 만약에 데이터가 안 맞는다는 메세지가 온다? 하면 바로 삭제
		HubResponseDto hubInfo = hubServiceClient.readHub(companyRequestDto.getCompanyHubId(), userRole, String.valueOf(userId), userName);
		if (hubInfo.getData() == null) {
			throw new CustomApiException(CompanyException.COMPANY_UNAUTHORIZED);
		}
		// 허브 관리자의 경우, 업체담당자ID가 존재하는 유저 ID인지 확인
		UserResponseDto userInfo = userServiceClient.getUser(companyRequestDto.getCompanyManagerId(), userRole, String.valueOf(userId), userName);
		if (userInfo.getData() == null){
			throw new CustomApiException(CompanyException.COMPANY_UNAUTHORIZED);
		}
		Company company = companyRepository.save(Company.from(companyRequestDto));
		return CompanyResponseDto.from(company);
	}

	// 업체 조회
	@Override
	public CompanyResponseDto findTheCompany(UUID companyId) {
		Company company = companyRepository.findById(companyId)
			.orElseThrow(() -> new CustomApiException(CompanyException.COMPANY_NOT_FOUND));
		return CompanyResponseDto.from(company);
	}

	// 업체 리스트 조회 (전체, 허브별) + 검색
	@Override
	public Page<CompanyResponseDto> findAllCompanies(UUID hubId, String type, String keyword, int page, int size,
		Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy, String userRole) {
		return companyQueryRepository.findCompanies(hubId, type, keyword, PageUtils.pageable(page, size), sortDirection, sortBy, userRole);
	}

	// 업체 수정
	@Override
	@Transactional
	public CompanyResponseDto modifyCompany(UUID companyId, CompanyRequestDto requestDto, String userRole, UUID userId) {
		Company company = companyRepository.findById(companyId)
			.orElseThrow(() -> new CustomApiException(CompanyException.COMPANY_NOT_FOUND));
		if ("HUB_MANAGER".equals(userRole)) {
			// 로그인 유저가 허브관리자인 경우, 유저ID(허브관리자ID)로 허브ID 조회
			UUID hubId = hubServiceClient.readHubIdByHubManagerId(userId);
			// 상품이 소속된 업체의 허브가 아닌 경우 권한 없음
			if (!hubId.equals(company.getCompanyHubId())) {
				throw new CustomApiException(CompanyException.COMPANY_UNAUTHORIZED);
			}
		} else if ("COMPANY_MANAGER".equals(userRole)) {
			// 로그인 유저가 업체담당자인 경우, 본인 업체 아니면 권한 없음
			if (!userId.equals(company.getCompanyManagerId())) {
				throw new CustomApiException(CompanyException.COMPANY_UNAUTHORIZED);
			}
		}
		company.modifyCompanyInfo(requestDto);
		return CompanyResponseDto.from(company);
	}

	// 업체 삭제
	@Override
	@Transactional
	public void removeCompany(UUID companyId, String userRole, UUID userId) {
		Company company = companyRepository.findById(companyId)
			.orElseThrow(() -> new CustomApiException(CompanyException.COMPANY_NOT_FOUND));
		if ("HUB_MANAGER".equals(userRole)) {
			// 로그인 유저가 허브관리자인 경우, 유저ID(허브관리자ID)로 허브ID 조회
			UUID hubId = hubServiceClient.readHubIdByHubManagerId(userId);
			// 상품이 소속된 업체의 허브가 아닌 경우 권한 없음
			if (!hubId.equals(company.getCompanyHubId())) {
				throw new CustomApiException(CompanyException.COMPANY_UNAUTHORIZED);
			}
		}
		company.markAsDeleted();
	}

	// for other services...

	// 허브별 업체ID 목록 조회
	@Override
	public List<UUID> findAllCompaniesByHubId(UUID hubId) {
		return companyQueryRepository.findCompanyIdByCompanyHubId(hubId);
	}

	// 업체담당자ID로 업체ID 조회
	@Override
	public UUID findCompanyIdByCompanyManagerId(UUID companyManagerId) {
		return companyQueryRepository.findCompanyIdByCompanyManagerId(companyManagerId);
	}

	// 허브ID로 업체ID 조회
	@Override
	public UUID findHubIdByCompanyId(UUID companyId) {
		return companyQueryRepository.findHubIdByCompanyId(companyId);
	}

}
