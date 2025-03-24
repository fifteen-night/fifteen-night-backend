// package com.fn.eureka.client.deliveryservice.domain.repository;
//
// import java.util.List;
// import java.util.UUID;
//
// import com.fn.eureka.client.deliveryservice.application.dto.delivery.response.CreateDeliveryResponseDto;
// import com.fn.eureka.client.deliveryservice.application.dto.deliveryRoute.response.CreateDeliveryRouteResponseDto;
// import com.fn.eureka.client.deliveryservice.application.dto.deliveryRouteSequence.response.CreateSequenceResponseDto;
// import com.fn.eureka.client.deliveryservice.domain.model.delivery.QDelivery;
// import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.QDeliveryRoute;
// import com.fn.eureka.client.deliveryservice.domain.model.deliveryRouteSequence.QDeliveryRouteSequence;
// import com.querydsl.core.types.Projections;
// import com.querydsl.jpa.JPAExpressions;
// import com.querydsl.jpa.impl.JPAQueryFactory;
//
// import lombok.RequiredArgsConstructor;
//
// @RequiredArgsConstructor
// public class DeliveryRepositoryImpl implements DeliveryRepositoryCustom{
//
// 	private final JPAQueryFactory queryFactory;
//
// 	@Override
// 	public CreateDeliveryResponseDto findDeliveryWithRoute(UUID deliveryId) {
//
// 		QDelivery qDelivery = QDelivery.delivery;
// 		QDeliveryRoute qDeliveryRoute = QDeliveryRoute.deliveryRoute;
// 		QDeliveryRouteSequence qDeliveryRouteSequence = QDeliveryRouteSequence.deliveryRouteSequence;
//
// 		List<CreateSequenceResponseDto> sequenceList = JPAExpressions.select(
// 				Projections.bean(
// 					CreateSequenceResponseDto.class,
// 					qDeliveryRouteSequence.sequenceId,
// 					qDeliveryRouteSequence.sequenceNumber,
// 					qDeliveryRouteSequence.distance,
// 					qDeliveryRouteSequence.quantity
// 				)
// 			)
// 			.from(qDeliveryRouteSequence)
// 			.where(qDeliveryRouteSequence.deliveryRoute.eq(qDeliveryRoute))
// 			.orderBy(qDeliveryRouteSequence.sequenceNumber.asc())
// 			.fetch();
//
// 		CreateDeliveryResponseDto createDeliveryResponseDto = queryFactory.select(
// 				Projections.bean(
// 					CreateDeliveryResponseDto.class,
// 					qDelivery.deliveryId,
// 					qDelivery.orderId,
// 					qDelivery.departureHubId,
// 					qDelivery.destinationHubId,
// 					qDelivery.status,
// 					qDelivery.address,
// 					qDelivery.receiverName,
// 					qDelivery.receiverSlackId,
// 					qDelivery.cdmId,
// 					JPAExpressions.select(
// 							Projections.bean(
// 								CreateDeliveryRouteResponseDto.class,
// 								qDeliveryRoute.deliveryRouteId,
// 								qDeliveryRoute.estimatedTime,
// 								qDeliveryRoute.estimatedDistance,
// 								qDeliveryRoute.currentStatus
// 							)
// 						)
// 						.from(qDeliveryRoute)
// 						.where(qDeliveryRoute.delivery.eq(qDelivery))
// 						.fetchFirst() // 첫 번째 결과만 가져오기
// 						.as("deliveryRoute") // 첫 번째 결과에 대한 'deliveryRoute' 매핑
// 				)
// 			)
// 			.from(qDelivery)
// 			.where(qDelivery.deliveryId.eq(deliveryId))
// 			.fetchOne();
//
// 		return createDeliveryResponseDto;
// 	}
//
// }
