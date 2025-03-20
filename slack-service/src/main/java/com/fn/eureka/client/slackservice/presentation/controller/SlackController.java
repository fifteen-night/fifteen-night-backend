package com.fn.eureka.client.slackservice.presentation.controller;

import com.fn.eureka.client.slackservice.application.dto.request.SlackMessageRequestDto;
import com.fn.eureka.client.slackservice.application.dto.response.SlackMessageResponseDto;
import com.fn.eureka.client.slackservice.application.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
public class SlackController {

	private final SlackService slackService;

	@PostMapping
	public ResponseEntity<SlackMessageResponseDto> sendSlackMessage(@RequestBody SlackMessageRequestDto requestDto) {
		SlackMessageResponseDto responseDto = slackService.sendSlackMessage(requestDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}
}

