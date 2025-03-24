package com.fn.eureka.client.productservice.domain.model;

import java.util.UUID;

import org.hibernate.annotations.Comment;

import com.fn.common.global.BaseEntity;
import com.fn.common.global.exception.UnauthorizedException;
import com.fn.eureka.client.productservice.presentation.requeset.ProductRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="p_product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Comment("상품 ID")
	private UUID productId;

	@Column(nullable = false)
	@Comment("상품명")
	private String productName;

	@Column(nullable = false)
	@Comment("업체 ID")
	private UUID productCompanyId;

	@Column(nullable = false)
	@Comment("상품 수량")
	private Integer productQuantity;

	public static Product from(ProductRequestDto productRequestDto) {
		return Product.builder()
			.productName(productRequestDto.getProductName())
			.productCompanyId(productRequestDto.getProductCompanyId())
			.productQuantity(productRequestDto.getProductQuantity())
			.build();
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

	public void updateProductQuantity(int quantity) {
		this.productQuantity -= quantity;
	}
}
