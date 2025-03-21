package com.fn.eureka.client.companyservice.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.exception.NotFoundException;
import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.companyservice.presentation.dto.CompanyRequestDto;
import com.fn.eureka.client.companyservice.presentation.dto.CompanyResponseDto;
import com.fn.eureka.client.companyservice.domain.entity.Company;
import com.fn.eureka.client.companyservice.domain.repository.CompanyQueryRepository;
import com.fn.eureka.client.companyservice.domain.repository.CompanyRepository;
import com.fn.eureka.client.companyservice.infrastructure.HubServiceClient;
import com.fn.eureka.client.companyservice.infrastructure.UserServiceClient;

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
		// TODO kafka를 이용해서 메세징 - 나 company 데이터 받았으니까 맞는지 확인해줘! 메세지 보내고 업체 생성하되 만약에 데이터가 안 맞는다는 메세지가 온다? 하면 바로 삭제
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
	@Transactional
	public CompanyResponseDto modifyCompany(UUID companyId, CompanyRequestDto requestDto) {
		Company company = companyRepository.findById(companyId)
			.orElseThrow(() -> new NotFoundException("해당 업체를 찾을 수 없습니다"));
		company.modifyCompanyInfo(requestDto);
		return new CompanyResponseDto(company);
	}

	// 업체 삭제 soft-delete
	@Override
	@Transactional
	public void removeCompany(UUID companyId) {
		Company company = companyRepository.findById(companyId)
			.orElseThrow(() -> new NotFoundException("해당 업체를 찾을 수 없습니다"));
		company.markAsDeleted();
	}

	// for other services...

	// 허브별 업체ID 목록 조회
	@Override
	public List<UUID> findAllCompaniesByHubId(UUID hubId) {
		return companyQueryRepository.findCompanyIdByCompanyHubId(hubId);
	}

	@Override
	public UUID findCompanyIdByCompanyManagerId(UUID companyManagerId) {
		return companyQueryRepository.findCompanyIdByCompanyManagerId(companyManagerId);
	}

	@Override
	public UUID findHubIdByCompanyId(UUID companyId) {
		return companyQueryRepository.findHubIdByCompanyId(companyId);
	}

}
