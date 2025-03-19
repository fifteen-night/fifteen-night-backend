package com.fn.eureka.client.productservice.presentation;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.exception.NotFoundException;
import com.fn.common.global.exception.UnauthorizedException;
import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.productservice.application.dto.CompanyInfoDto;
import com.fn.eureka.client.productservice.application.dto.ProductRequestDto;
import com.fn.eureka.client.productservice.application.dto.ProductResponseDto;
import com.fn.eureka.client.productservice.domain.Product;
import com.fn.eureka.client.productservice.domain.repository.ProductQueryRepository;
import com.fn.eureka.client.productservice.domain.repository.ProductRepository;
import com.fn.eureka.client.productservice.infrastructure.CompanyServiceClient;
import com.fn.eureka.client.productservice.infrastructure.HubServiceClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductQueryRepository productQueryRepository;

	private final HubServiceClient hubServiceClient;
	private final CompanyServiceClient companyServiceClient;

	// 상품 생성
	@Override
	@Transactional
	public ProductResponseDto addProduct(ProductRequestDto productRequestDto, String userRole, UUID userId) {
		// 업체 조회
		CompanyInfoDto companyInfo= companyServiceClient.getCompany(productRequestDto.getProductCompanyId());
		switch(userRole) {
			// MASTER 어디든 상품 생성 가능
			case "MASTER" :
				break;
			// HUB MANAGER 본인 허브에 소속된 상품만 생성 가능 (업체ID로 허브ID 확인 필요)
			case "HUB_MANAGER" :
				// 허브관리자ID로 허브ID 검색
				// UUID hubId = hubServiceClient.getHubByHubManagerId(userId);
				// if (!companyInfo.getCompanyHubId().equals(hubId)) {
				// 	throw new UnauthorizedException("해당 허브는 상품 업체의 소속 허브가 아닙니다.");
				// }
				break;
			// COMPANY MANAGER 본인 업체 상품만 생성 가능
			case "COMPANY_MANAGER" :
				if (!companyInfo.getCompanyManagerId().equals(userId)) {
					throw new UnauthorizedException("해당 업체의 상품이 아닙니다.");
				}
				break;
			default:
				throw new UnauthorizedException("상품을 생성할 권한이 없습니다.");
		}
		Product product = productRepository.save(new Product(productRequestDto));
		return new ProductResponseDto(product);
	}

	// 상품 조회
	@Override
	public ProductResponseDto findProduct(UUID productId) {
		Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
			.orElseThrow(() -> new NotFoundException("해당 상품은 존재하지 않습니다."));
		return new ProductResponseDto(product);
	}

	// 전체/허브별/업체별 상품 리스트 조회 + 검색
	@Override
	public Page<ProductResponseDto> findAllProductsByType(String type, UUID id, String keyword, int page, int size,
		Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy) {
		// TODO client 해결되면 허브별 상품 리스트 조회 코드 추가
		// id와 같은 HubID를 가진 업체ID 목록 가져오기
		// List<UUID> companies = null;
		// if ("hub".equalsIgnoreCase(type) && id != null) {
		// 	companies = companyServiceClient.getCompanyIdByCompanyHubId(id);
		// }
		return productQueryRepository.findProductsByType(type, id, keyword, PageUtils.pageable(page, size), sortDirection, sortBy);
	}

	// 상품 수정
	@Override
	@Transactional
	public ProductResponseDto modifyProduct(UUID productId, Map<String, Object> updates, String userRole) {
		// TODO Security - 마스터, 허브관리자, 업체관리자만 상품 수정 가능
		Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
			.orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));
		updates.forEach((key, value) -> product.modifyProductInfo(key, value, userRole));
		return new ProductResponseDto(product);
	}

}
