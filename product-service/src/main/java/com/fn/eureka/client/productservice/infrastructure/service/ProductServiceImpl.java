package com.fn.eureka.client.productservice.infrastructure.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.exception.CustomApiException;
import com.fn.common.global.exception.NotFoundException;
import com.fn.common.global.exception.UnauthorizedException;
import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.productservice.application.ProductService;
import com.fn.eureka.client.productservice.domain.model.Product;
import com.fn.eureka.client.productservice.infrastructure.client.DeliveryServiceClient;
import com.fn.eureka.client.productservice.infrastructure.client.OrderServiceClient;
import com.fn.eureka.client.productservice.infrastructure.exception.ProductException;
import com.fn.eureka.client.productservice.infrastructure.repository.ProductQueryRepositoryImpl;
import com.fn.eureka.client.productservice.domain.repository.ProductRepository;
import com.fn.eureka.client.productservice.infrastructure.client.CompanyServiceClient;
import com.fn.eureka.client.productservice.infrastructure.client.HubServiceClient;
import com.fn.eureka.client.productservice.application.dto.CompanyInfoDto;
import com.fn.eureka.client.productservice.presentation.requeset.ProductRequestDto;
import com.fn.eureka.client.productservice.application.dto.ProductResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductQueryRepositoryImpl productQueryRepository;

	private final HubServiceClient hubServiceClient;
	private final CompanyServiceClient companyServiceClient;
	private final DeliveryServiceClient deliveryServiceClient;
	private final OrderServiceClient orderServiceClient;

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
				UUID hubId = hubServiceClient.readHubIdByHubManagerId(userId);
				if (!companyInfo.getCompanyHubId().equals(hubId)) {
					throw new CustomApiException(ProductException.PRODUCT_UNAUTHORIZED);
				}
				break;
			// COMPANY MANAGER 본인 업체 상품만 생성 가능
			case "COMPANY_MANAGER" :
				if (!companyInfo.getCompanyManagerId().equals(userId)) {
					throw new CustomApiException(ProductException.PRODUCT_UNAUTHORIZED);
				}
				break;
			default:
				throw new CustomApiException(ProductException.PRODUCT_UNAUTHORIZED);
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
	public Page<ProductResponseDto> findAllProducts(String keyword, int page, int size,
		Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy, String userRole, UUID userId) {
		// TODO client 해결되면 허브별 상품 리스트 조회 코드 추가
		List<UUID> companies = null;
		List<UUID> products = null;
		UUID companyId = null;
		switch (userRole) {
			case "MASTER" : break;
			case "HUB_MANAGER" :
				UUID hubId = hubServiceClient.readHubIdByHubManagerId(userId);
				// 허브가 같은 업체 리스트
				companies = companyServiceClient.readCompaniesByHubId(hubId);
				break;
			case "DELIVERY_MANAGAER" :
				// 로그인 유저(배송담당자)가 담당하는 배송ID 리스트 조회
				List<UUID> deliveries = deliveryServiceClient.readDeliveriesByDeliveryManagerId(userId);
				// 배송ID로 주문된 상품ID 리스트 조회
				products = orderServiceClient.readOrderProductIdListByDeliveryId(deliveries);
				break;
			case "COMANY_MANAGER" :
				companyId = companyServiceClient.readCompanyIdByCompanyManagerId(userId);
				break;
			default: throw new CustomApiException(ProductException.PRODUCT_UNAUTHORIZED);
		}

		return productQueryRepository.findProducts(userRole, companies, products, companyId, keyword, PageUtils.pageable(page, size), sortDirection, sortBy);
	}

	// 상품 수정
	@Override
	@Transactional
	public ProductResponseDto modifyProduct(UUID productId, Map<String, Object> updates, String userRole) {
		// TODO 상품의 업체 담당자, <업체의 소속 허브 관리자만 삭제 가능
		// TODO Security - 마스터, 허브관리자, 업체관리자만 상품 수정 가능
		Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
			.orElseThrow(() -> new CustomApiException(ProductException.PRODUCT_NOT_FOUND));
		updates.forEach((key, value) -> product.modifyProductInfo(key, value, userRole));
		return new ProductResponseDto(product);
	}

	@Override
	@Transactional
	public void removeProduct(UUID productId) {
		// TODO 상품의 업체 담당자, < 업체의 소속 허브 관리자만 삭제 가능
		Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
			.orElseThrow(() -> new CustomApiException(ProductException.PRODUCT_NOT_FOUND));
		product.markAsDeleted();
	}



}
