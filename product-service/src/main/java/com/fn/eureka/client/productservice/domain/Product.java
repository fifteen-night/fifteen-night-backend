package com.fn.eureka.client.productservice.domain;

import java.util.UUID;

import com.fn.common.global.BaseEntity;
import com.fn.eureka.client.productservice.application.dto.ProductRequestDto;

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
}
