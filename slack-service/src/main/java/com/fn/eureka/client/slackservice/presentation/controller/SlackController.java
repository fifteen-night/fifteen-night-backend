package com.fn.eureka.client.slackservice.presentation.controller;

import com.fn.common.global.dto.CommonResponse;
import com.fn.common.global.success.SuccessCode;
import com.fn.eureka.client.slackservice.application.dto.request.SlackMessageRequestDto;
import com.fn.eureka.client.slackservice.application.dto.request.SlackUpdateRequestDto;
import com.fn.eureka.client.slackservice.application.dto.response.SlackGetResponseDto;
import com.fn.eureka.client.slackservice.application.dto.response.SlackMessageResponseDto;
import com.fn.eureka.client.slackservice.application.dto.response.SlackUpdateResponseDto;
import com.fn.eureka.client.slackservice.application.service.SlackService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
public class SlackController {

	private final SlackService slackService;

	@PostMapping
	public ResponseEntity<CommonResponse<SlackMessageResponseDto>> sendSlackMessage(
		@RequestBody SlackMessageRequestDto requestDto) {
		CommonResponse<SlackMessageResponseDto> response = slackService.sendSlackMessage(requestDto);

		return ResponseEntity.status(SuccessCode.SLACK_MESSAGE_SENT.getStatusCode()).body(response);
	}

	@GetMapping("/{slackId}")
	public ResponseEntity<CommonResponse<SlackGetResponseDto>> getSlackMessage(@PathVariable UUID slackId) {
		CommonResponse<SlackGetResponseDto> response = slackService.getSlackMessage(slackId);

		return ResponseEntity.status(SuccessCode.SLACK_MESSAGE_FOUND.getStatusCode()).body(response);
	}

	@GetMapping
	public ResponseEntity<CommonResponse<Page<SlackGetResponseDto>>> getSlackMessages(
		@RequestParam(required = false) String keyword,
		Pageable pageable) {

		CommonResponse<Page<SlackGetResponseDto>> response = slackService.getSlackMessages(keyword, pageable);
		return ResponseEntity.status(SuccessCode.SLACK_MESSAGE_LIST_FOUND.getStatusCode()).body(response);
	}

	@PatchMapping("/{slackId}")
	public ResponseEntity<CommonResponse<SlackUpdateResponseDto>> updateSlackMessage(
		@PathVariable UUID slackId,
		@RequestBody SlackUpdateRequestDto requestDto) {

		CommonResponse<SlackUpdateResponseDto> response = slackService.updateSlackMessage(slackId, requestDto);

		return ResponseEntity.status(SuccessCode.SLACK_MESSAGE_UPDATED.getStatusCode()).body(response);
	}
}
