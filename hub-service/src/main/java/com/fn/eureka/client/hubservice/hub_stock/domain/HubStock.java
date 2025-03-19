package com.fn.eureka.client.hubservice.hub_stock.domain;

import java.util.UUID;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fn.eureka.client.hubservice.hub.domain.Hub;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Comment("허브 재고")
@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_hub_stock")
public class HubStock {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(SqlTypes.UUID)
	@Comment("허브 재고 ID")
	private UUID hsId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hs_hub_id", nullable = false)
	private Hub hsHub;

	@Column(nullable = false)
	@Comment("상품 ID")
	private UUID hsProductId;

	@Comment("허브 재고 수량")
	private int hsQuantity;

	@Builder
	public HubStock(Hub hsHub, UUID hsProductId, int hsQuantity) {
		this.hsHub = hsHub;
		this.hsProductId = hsProductId;
		this.hsQuantity = hsQuantity;
	}

	public void updateQuantity(int quantity) {
		this.hsQuantity += quantity;
	}
}