package com.fn.eureka.client.slackservice.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackUpdateResponseDto {
	private UUID slackId;
	private String updatedMessage;
	private LocalDateTime updatedAt;
}
