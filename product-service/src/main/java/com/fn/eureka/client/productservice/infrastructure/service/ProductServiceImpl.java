package com.fn.eureka.client.productservice.infrastructure.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.exception.CustomApiException;
import com.fn.common.global.exception.NotFoundException;
import com.fn.common.global.util.PageUtils;
import com.fn.eureka.client.productservice.application.ProductService;
import com.fn.eureka.client.productservice.application.dto.CompanyInfoDto;
import com.fn.eureka.client.productservice.application.dto.HubStockRequestDto;
import com.fn.eureka.client.productservice.application.dto.HubStockResponseDto;
import com.fn.eureka.client.productservice.application.dto.ProductResponseDto;
import com.fn.eureka.client.productservice.domain.model.Product;
import com.fn.eureka.client.productservice.domain.repository.ProductQueryRepository;
import com.fn.eureka.client.productservice.domain.repository.ProductRepository;
import com.fn.eureka.client.productservice.infrastructure.client.CompanyServiceClient;
import com.fn.eureka.client.productservice.infrastructure.client.DeliveryServiceClient;
import com.fn.eureka.client.productservice.infrastructure.client.HubServiceClient;
import com.fn.eureka.client.productservice.infrastructure.client.OrderServiceClient;
import com.fn.eureka.client.productservice.infrastructure.exception.ProductException;
import com.fn.eureka.client.productservice.presentation.requeset.ProductRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductQueryRepository productQueryRepository;

	private final HubServiceClient hubServiceClient;
	private final CompanyServiceClient companyServiceClient;
	private final DeliveryServiceClient deliveryServiceClient;
	private final OrderServiceClient orderServiceClient;

	// 상품 생성
	@Override
	@Transactional
	public ProductResponseDto addProduct(ProductRequestDto productRequestDto, String userRole, UUID userId) {
		// 업체 조회
		CompanyInfoDto.CompanyData companyInfo = Objects.requireNonNull(
			companyServiceClient.getCompany(productRequestDto.getProductCompanyId()).getData());
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
		Product product = productRepository.save(Product.from(productRequestDto));
		return ProductResponseDto.from(product);
	}

	// 상품 조회
	@Override
	public ProductResponseDto findProduct(UUID productId) {
		Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
			.orElseThrow(() -> new NotFoundException("해당 상품은 존재하지 않습니다."));
		return ProductResponseDto.from(product);
	}

	// 전체/허브별/업체별 상품 리스트 조회 + 검색
	@Override
	public Page<ProductResponseDto> findAllProducts(String keyword, int page, int size,
		Sort.Direction sortDirection, PageUtils.CommonSortBy sortBy, String userRole, UUID userId) {
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
			case "COMPANY_MANAGER" :
				companyId = companyServiceClient.readCompanyIdByCompanyManagerId(userId);
				break;
			default: throw new CustomApiException(ProductException.PRODUCT_UNAUTHORIZED);
		}

		return productQueryRepository.findProducts(userRole, companies, products, companyId, keyword, PageUtils.pageable(page, size), sortDirection, sortBy);
	}

	// 상품 수정
	@Override
	@Transactional
	public ProductResponseDto modifyProduct(UUID productId, Map<String, Object> updates, String userRole, UUID userId) {
		Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
			.orElseThrow(() -> new CustomApiException(ProductException.PRODUCT_NOT_FOUND));
		validateUserPermissionForUpdateProduct(product, userRole, userId);
		updates.forEach((key, value) -> product.modifyProductInfo(key, value, userRole));
		return ProductResponseDto.from(product);
	}

	// 상품 삭제
	@Override
	@Transactional
	public void removeProduct(UUID productId, String userRole, UUID userId) {
		Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
			.orElseThrow(() -> new CustomApiException(ProductException.PRODUCT_NOT_FOUND));
		validateUserPermissionForDeleteProduct(product, userRole, userId);
		product.markAsDeleted();
	}

	// 허브에 상품 입고 요청
	@Override
	@Transactional
	public HubStockResponseDto addProductInHub(UUID hubId, HubStockRequestDto hubStockRequestDto, String userRole, UUID userId) {
		UUID productId = hubStockRequestDto.getProductId();
		Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
			.orElseThrow(() -> new CustomApiException(ProductException.PRODUCT_NOT_FOUND));
		// 허브에 입고하는 건 마스터, 허브관리자만 가능 - 상품 삭제 권한 검증 재활용
		validateUserPermissionForDeleteProduct(product, userRole, userId);
		// 이미 허브에 상품이 있으면 수량 추가되고, 없으면 생성
		HubStockResponseDto hubStockResponseDto = hubServiceClient.createHubStock(hubId, hubStockRequestDto);
		product.updateProductQuantity(hubStockRequestDto.getQuantity());
		return hubStockResponseDto;
	}

	// 상품 수정 권한 검증 (마스터, 허브관리자, 업체담당자)
	private void validateUserPermissionForUpdateProduct(Product product, String userRole, UUID userId) {
		if ("MASTER".equals(userRole)) {
			return;
		}
		if ("HUB_MANAGER".equals(userRole)) {
			// 로그인 유저가 허브관리자인 경우, 유저ID(허브관리자ID)로 허브ID 조회
			UUID hubId = hubServiceClient.readHubIdByHubManagerId(userId);
			// 업체ID로 업체 소속 허브ID 조회
			UUID companyHubId = companyServiceClient.readHubIdByCompanyId(product.getProductCompanyId());
			// 상품이 소속된 업체의 허브가 아닌 경우 권한 없음
			if (!hubId.equals(companyHubId)) {
				throw new CustomApiException(ProductException.PRODUCT_UNAUTHORIZED);
			}
			return;
		}
		if ("COMPANY_MANAGER".equals(userRole)) {
			// 업체담당자ID로 업체ID 조회
			UUID companyId = companyServiceClient.readCompanyIdByCompanyManagerId(userId);
			// 상품이 소속된 업체가 아닌 경우 권한 없음
			if (!companyId.equals(product.getProductCompanyId())) {
				throw new CustomApiException(ProductException.PRODUCT_UNAUTHORIZED);
			}
			return;
		}
		throw new CustomApiException(ProductException.PRODUCT_UNAUTHORIZED);
	}

	// 상품 삭제 권한 검증 (마스터, 허브관리자만 가능)
	private void validateUserPermissionForDeleteProduct(Product product, String userRole, UUID userId) {
		if ("MASTER".equals(userRole)) {
			return;
		}
		if ("HUB_MANAGER".equals(userRole)) {
			UUID hubId = hubServiceClient.readHubIdByHubManagerId(userId);
			UUID companyHubId = companyServiceClient.readHubIdByCompanyId(product.getProductCompanyId());
			if (!hubId.equals(companyHubId)) {
				throw new CustomApiException(ProductException.PRODUCT_UNAUTHORIZED);
			}
			return;
		}
		throw new CustomApiException(ProductException.PRODUCT_UNAUTHORIZED);
	}

	// 상품ID 리스트로 상품 리스트 조회
	@Override
	public List<ProductResponseDto> findProductListByProductIdList(List<UUID> products) {
		List<Product> productInfoListByproducts = productQueryRepository.findProductListByProductIdList(products);
		return productInfoListByproducts.stream().map(ProductResponseDto::from).toList();
	}
}
