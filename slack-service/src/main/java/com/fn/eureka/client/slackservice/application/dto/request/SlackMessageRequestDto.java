package com.fn.eureka.client.slackservice.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackMessageRequestDto {
	private String SlackReceivedSlackId;
	private String text;
}
