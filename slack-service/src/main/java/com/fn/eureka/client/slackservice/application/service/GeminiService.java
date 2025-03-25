package com.fn.eureka.client.slackservice.application.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fn.common.global.exception.CustomApiException;
import com.fn.eureka.client.slackservice.application.dto.request.DeliveryInfoDto;
import com.fn.eureka.client.slackservice.application.dto.request.GeminiRequestDto;
import com.fn.eureka.client.slackservice.application.dto.request.SlackMessageRequestDto;
import com.fn.eureka.client.slackservice.application.dto.response.DeliveryManagerInfoDto;
import com.fn.eureka.client.slackservice.application.dto.response.GeminiResponseDto;
import com.fn.eureka.client.slackservice.application.dto.response.OrderInfoDto;
import com.fn.eureka.client.slackservice.application.dto.response.ProductInfoDto;
import com.fn.eureka.client.slackservice.application.dto.response.UserInfoDto;
import com.fn.eureka.client.slackservice.application.exception.SlackException;
import com.fn.eureka.client.slackservice.infrastructure.client.DeliveryManagerServiceClient;
import com.fn.eureka.client.slackservice.infrastructure.client.GeminiClient;
import com.fn.eureka.client.slackservice.infrastructure.client.OrderServiceClient;
import com.fn.eureka.client.slackservice.infrastructure.client.ProductServiceClient;
import com.fn.eureka.client.slackservice.infrastructure.client.UserServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

	private final SlackService slackService;
	private final GeminiClient geminiClient;
	private final DeliveryManagerServiceClient deliveryManagerServiceClient;
	private final UserServiceClient userServiceClient;
	private final OrderServiceClient orderServiceClient;
	private final ProductServiceClient productServiceClient;

	@Value("${gemini.api.key}")
	private String apiKey;

	@Value("${gemini.model.name}")
	private String modelName;

	@Value("${slack.webhook.url}")
	private String slackAddress;

	public GeminiResponseDto requestAndResponse(DeliveryInfoDto deliveryInfoDto) {
		String contents = getDeliveryTimeRecommendation(deliveryInfoDto);
		if (contents == null) {
			throw new CustomApiException(SlackException.AI_INFO_NOT_FOUND);
		}
		GeminiRequestDto.TextPart textPart = GeminiRequestDto.createTextPart(contents);
		GeminiRequestDto geminiRequestDto = GeminiRequestDto.builder()
			.contents(List.of(new GeminiRequestDto.Content(List.of(textPart))))
			.build();
		GeminiResponseDto geminiResponseDto = geminiClient.getCompletion(modelName, apiKey, geminiRequestDto);
		// 슬랙메세지로 보내기
		SlackMessageRequestDto slackMessageRequestDto = SlackMessageRequestDto.builder()
			.SlackReceiverId(slackAddress)
			.text(geminiResponseDto.extractText())
			.build();
		slackService.sendSlackMessage(slackMessageRequestDto);
		return geminiResponseDto;
	}

	// 배송 시간 판단을 위한 메소드
	public String getDeliveryTimeRecommendation(DeliveryInfoDto deliveryInfoDto) {
		OrderInfoDto orderInfo = orderServiceClient.readOrder(deliveryInfoDto.getData().getOrderId());
		Integer quantity = orderInfo.getData().getOrderProductQuantity();
		Timestamp deadlineTimestamp = orderInfo.getData().getOrderDeadline();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String deadline = dateFormat.format(deadlineTimestamp);
		String orderCreatedAt = orderInfo.getData().getOrderCreatedAt();

		ProductInfoDto productInfo = productServiceClient.readProduct(orderInfo.getData().getOrderProductId());
		String productName = productInfo.getData().getProductName();

		log.info("DeliveryInfoId: {}", deliveryInfoDto.getData().getDeliveryId());
		List<DeliveryInfoDto.Sequence> sequences = deliveryInfoDto.getData().getDeliveryRoute().getSequence();
		String departure = sequences.get(0).getDepartureHubAddress();
		String destination = deliveryInfoDto.getData().getAddress();
		String waypoints = sequences.size() > 0
			? sequences.stream()
			.map(seq -> seq.getArrivalHubAddress()) // 경유지는 모든 sequence의 도착지
			.collect(Collectors.joining(", "))
			: "없음"; // 경유지가 없으면 "없음" 처리
		log.info("waypoints: {}", waypoints);
		DeliveryManagerInfoDto cdmInfo = deliveryManagerServiceClient.getDeliveryManager(
			deliveryInfoDto.getData().getCdmId());
		UUID cdmUserId = cdmInfo.getData().getDmUserId();
		UserInfoDto userInfo = userServiceClient.readUser(cdmUserId);
		String cdmName = userInfo.getData().getUserNickname();

		String orderId = String.valueOf(deliveryInfoDto.getData().getOrderId());
		String prompt = String.format(
			"다음 배송 주문 정보를 분석해서, 1000자 이내로 최적의 배송 시간과 관련 정보를 제공해주세요:\n\n" +
				"주문 번호: %s\n" +
				"- 주문자 정보: %s\n" +
				"- 상품명: %s\n" +
				"- 수량: %d\n" +
				"- 주문시각: %s\n" +
				"- 납품기한: %s\n" +
				"- 출발지: %s\n" +
				"- 경유지: %s\n" +    // 경유지 여러개일 수도 있음
				"- 도착지: %s\n" +
				"- 배송담당자: %s\n\n" +
				"다음 정보를 포함하여 물류 담당자가 이해하기 쉬운 형식으로 간략하게 답변해주세요(강조 기호 없이):\n" +
				"1. 권장 발송 시작 시간\n : (납품기한" +
				"2. 예상 배송 소요 시간\n" +
				"3. 배송 담당자(%s)를 위한 참고사항\n",

			orderId,
			deliveryInfoDto.getData().getReceiverName(),
			productName,
			quantity,
			orderCreatedAt,
			deadline,
			departure,
			waypoints,
			destination,
			cdmName,
			cdmName
		);
		return prompt;
	}
}