package com.fn.eureka.client.product.presentation;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fn.common.global.exception.NotFoundException;
import com.fn.common.global.exception.UnauthorizedException;
import com.fn.eureka.client.product.application.dto.CompanyInfoDto;
import com.fn.eureka.client.product.application.dto.ProductRequestDto;
import com.fn.eureka.client.product.application.dto.ProductResponseDto;
import com.fn.eureka.client.product.domain.Product;
import com.fn.eureka.client.product.domain.ProductRepository;
import com.fn.eureka.client.product.infrastructure.CompanyServiceClient;
import com.fn.eureka.client.product.infrastructure.HubServiceClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	private final HubServiceClient hubServiceClient;
	private final CompanyServiceClient companyServiceClient;

	// 상품 생성
	@Override
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
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new NotFoundException("해당 상품은 존재하지 않습니다."));
		return new ProductResponseDto(product);
	}
}
