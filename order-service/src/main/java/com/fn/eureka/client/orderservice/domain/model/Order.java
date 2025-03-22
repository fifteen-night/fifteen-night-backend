package com.fn.eureka.client.orderservice.domain.model;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.Comment;

import com.fn.common.global.BaseEntity;
import com.fn.eureka.client.orderservice.presentation.request.OrderRequestDto;

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
@Entity
@Table(name="p_order")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Comment("주문 ID")
	private UUID orderId;

	@Column(nullable = false)
	@Comment("공급업체 ID")
	private UUID orderSupplyCompanyId;

	@Column(nullable = false)
	@Comment("수령업체 ID")
	private UUID orderReceiveCompanyId;

	@Column
	@Comment("배송 ID")
	private UUID orderDeliveryId;

	@Column(nullable = false)
	@Comment("상품 ID")
	private UUID orderProductId;

	@Column(nullable = false)
	@Comment("납품수량")
	private Integer orderProductQuantity;

	@Column(nullable = false)
	@Comment("납품기한")
	private Timestamp orderDeadline;

	@Column
	@Comment("요청사항")
	private String orderRequirement;

	public static Order from(OrderRequestDto requestDto) {
		return Order.builder()
			.orderSupplyCompanyId(requestDto.getOrderSupplyCompanyId())
			.orderReceiveCompanyId(requestDto.getOrderReceiveCompanyId())
			.orderProductId(requestDto.getOrderProductId())
			.orderProductQuantity(requestDto.getOrderProductQuantity())
			.orderDeadline(requestDto.getOrderDeadline())
			.orderRequirement(requestDto.getOrderRequirement())
			.build();
	}

	public void saveOrderDeliveryId(UUID deliveryId) {
		this.orderDeliveryId = deliveryId;
	}

	public void modifyOrderInfo(String key, Object value) {
		switch (key) {
			case "orderProductQuantity" -> this.orderProductQuantity = (Integer) value;
			case "orderDeadline" -> this.orderDeadline = (Timestamp) value;
			case "orderRequirement" -> this.orderRequirement = (String) value;
			default -> throw new IllegalArgumentException("잘못된 필드명 : " + key);
		}
	}
}
