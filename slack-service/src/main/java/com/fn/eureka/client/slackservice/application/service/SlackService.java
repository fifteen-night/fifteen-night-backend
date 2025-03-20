package com.fn.eureka.client.slackservice.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fn.eureka.client.slackservice.application.dto.request.SlackMessageRequestDto;
import com.fn.eureka.client.slackservice.application.dto.response.SlackMessageResponseDto;
import com.fn.eureka.client.slackservice.domain.entity.Slack;
import com.fn.eureka.client.slackservice.domain.repository.SlackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlackService {

	@Value("${slack.webhook.url}")
	private String slackWebhookUrl;

	private final RestTemplate restTemplate; // RestTemplate 사용
	private final SlackRepository slackRepository; // DB 저장을 위한 JPA Repository

	@Transactional
	public SlackMessageResponseDto sendSlackMessage(SlackMessageRequestDto requestDto) {
		// Slack Webhook 요청 보내기
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String payload = "{\"text\": \"" + requestDto.getText() + "\"}";
		HttpEntity<String> entity = new HttpEntity<>(payload, headers);
		restTemplate.postForEntity(slackWebhookUrl, entity, String.class);

		// 메시지 DB 저장
		Slack slackMessage = Slack.of(requestDto.getSlackReceivedSlackId(), requestDto.getText());
		slackRepository.save(slackMessage);

		return SlackMessageResponseDto.builder()
			.slackId(slackMessage.getSlackId())
			.build();
	}
}
