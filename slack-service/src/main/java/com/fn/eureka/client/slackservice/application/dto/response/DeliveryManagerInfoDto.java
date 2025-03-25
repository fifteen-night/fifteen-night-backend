package com.fn.eureka.client.slackservice.application.dto.response;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryManagerInfoDto {

	private UUID id;
	private UUID dmUserId;
	private UUID dmHubId;
	private String dmSlackId;
	private String dmType;
	private Integer dmTurn;
}
