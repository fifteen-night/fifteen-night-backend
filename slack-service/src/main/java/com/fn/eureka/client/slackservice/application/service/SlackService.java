package com.fn.eureka.client.slackservice.application.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.slackservice.application.dto.request.SlackMessageRequestDto;
import com.fn.eureka.client.slackservice.application.dto.response.SlackGetResponseDto;
import com.fn.eureka.client.slackservice.application.dto.response.SlackMessageResponseDto;
import com.fn.eureka.client.slackservice.application.exception.SlackException;
import com.fn.eureka.client.slackservice.domain.entity.Slack;
import com.fn.eureka.client.slackservice.domain.repository.SlackRepository;
import com.fn.eureka.client.slackservice.infrastructure.security.RequestUserDetails;
import com.fn.common.global.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlackService {

	@Value("${slack.webhook.url}")
	private String slackWebhookUrl;

	private final RestTemplate restTemplate;
	private final SlackRepository slackRepository;

	@Transactional
	public CommonResponse<SlackMessageResponseDto> sendSlackMessage(SlackMessageRequestDto requestDto) {
		// Slack Webhook 요청 보내기
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String payload = "{\"text\": \"" + requestDto.getText() + "\"}";
		HttpEntity<String> entity = new HttpEntity<>(payload, headers);
		restTemplate.postForEntity(slackWebhookUrl, entity, String.class);

		// 메시지 DB 저장
		Slack slackMessage = Slack.of(requestDto.getSlackReceiverId(), requestDto.getText());
		slackRepository.save(slackMessage);

		SlackMessageResponseDto responseDto = SlackMessageResponseDto.builder()
			.slackId(slackMessage.getSlackId())
			.build();

		return new CommonResponse<>(SuccessCode.SLACK_MESSAGE_SENT, responseDto);
	}

	@Transactional(readOnly = true)
	public CommonResponse<SlackGetResponseDto> getSlackMessage(UUID slackId) {
		validateMasterRole();

		Slack slackMessage = slackRepository.findById(slackId)
			.orElseThrow(() -> new CustomApiException(SlackException.SLACK_MESSAGE_NOT_FOUND));

		SlackGetResponseDto responseDto = SlackGetResponseDto.builder()
			.slackId(slackMessage.getSlackId())
			.receiverId(slackMessage.getSlackReceiverId())
			.message(slackMessage.getSlackMessage())
			.sentAt(slackMessage.getCreatedAt())
			.build();

		return new CommonResponse<>(SuccessCode.SLACK_MESSAGE_FOUND, responseDto);
	}

	@Transactional(readOnly = true)
	public CommonResponse<Page<SlackGetResponseDto>> getSlackMessages(String keyword, Pageable pageable) {
		validateMasterRole();

		Page<Slack> messages = (keyword == null || keyword.trim().isEmpty()) ?
			slackRepository.findAll(pageable) :
			slackRepository.findByKeyword(keyword, pageable);

		Page<SlackGetResponseDto> responseDtoPage = messages.map(slack -> SlackGetResponseDto.builder()
			.slackId(slack.getSlackId())
			.receiverId(slack.getSlackReceiverId())
			.message(slack.getSlackMessage())
			.sentAt(slack.getCreatedAt())
			.build());

		return new CommonResponse<>(SuccessCode.SLACK_MESSAGE_LIST_FOUND, responseDtoPage);
	}

	private void validateMasterRole() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new CustomApiException(SlackException.UNAUTHORIZED_ACCESS);
		}

		Object principal = authentication.getPrincipal();
		if (!(principal instanceof RequestUserDetails userDetails)) {
			throw new CustomApiException(SlackException.UNAUTHORIZED_ACCESS);
		}

		boolean isMaster = userDetails.getAuthorities().stream()
			.anyMatch(auth -> auth.getAuthority().equals("ROLE_MASTER"));

		if (!isMaster) {
			throw new CustomApiException(SlackException.UNAUTHORIZED_ACCESS);
		}
	}
}
