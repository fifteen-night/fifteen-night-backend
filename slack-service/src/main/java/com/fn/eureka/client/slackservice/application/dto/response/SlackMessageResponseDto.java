package com.fn.eureka.client.slackservice.application.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackMessageResponseDto {
	private UUID slackId;
}

