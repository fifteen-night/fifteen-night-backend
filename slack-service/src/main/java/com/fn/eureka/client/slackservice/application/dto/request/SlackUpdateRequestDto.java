package com.fn.eureka.client.slackservice.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackUpdateRequestDto {
	private String slackMessage;
}
