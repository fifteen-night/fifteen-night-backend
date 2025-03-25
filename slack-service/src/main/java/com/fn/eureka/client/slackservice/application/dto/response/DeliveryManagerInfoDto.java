package com.fn.eureka.client.slackservice.application.dto.response;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryManagerInfoDto {
	private String message;
	private DeliveryManagerData data;
	@Getter
	public static class DeliveryManagerData {
		private UUID id;
		private UUID dmUserId;
		private UUID dmHubId;
		private String dmSlackId;
		private String dmType;
		private Integer dmTurn;
	}
}
