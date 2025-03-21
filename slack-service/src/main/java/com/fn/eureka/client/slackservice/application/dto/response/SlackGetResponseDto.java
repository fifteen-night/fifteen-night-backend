package com.fn.eureka.client.slackservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class SlackGetResponseDto {
	private UUID slackId;
	private String receiverId;
	private String message;
	private LocalDateTime sentAt;
	private boolean isDeleted;
	private LocalDateTime deletedAt;
}

