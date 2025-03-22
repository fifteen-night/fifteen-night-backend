package com.fn.eureka.client.productservice.domain.model;

import java.util.UUID;

import com.fn.common.global.BaseEntity;
import com.fn.common.global.exception.UnauthorizedException;
import com.fn.eureka.client.productservice.presentation.requeset.ProductRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="p_product")
@NoArgsConstructor
public class Product extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID productId;

	@Column(nullable = false)
	private String productName;

	@Column(nullable = false)
	private UUID productCompanyId;

	@Column(nullable = false)
	private Integer productQuantity;

	public Product(ProductRequestDto productRequestDto) {
		this.productName = productRequestDto.getProductName();
		this.productCompanyId = productRequestDto.getProductCompanyId();
		this.productQuantity = productRequestDto.getProductQuantity();
	}

	public void modifyProductInfo(String key, Object value, String userRole) {
		switch (key) {
			case "productName" -> this.productName = (String) value;
			case "productQuantity" -> this.productQuantity = (Integer) value;
			case "productCompanyId" -> {
				if (!"HUB_MANAGER".equalsIgnoreCase(userRole) && !"MASTER".equalsIgnoreCase(userRole)) {
					throw new UnauthorizedException("상품의 업체를 변경할 권한이 없습니다.");
				}
				this.productCompanyId = UUID.fromString((String) value);
			}
			default -> throw new IllegalStateException("잘못된 필드명 : " + key);
		}
	}
}
