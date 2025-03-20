package com.fn.eureka.client.slackservice.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fn.common.global.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_slack")
public class Slack extends BaseEntity {
	@Id
	@UuidGenerator
	private UUID slackId;

	private String SlackReceivedSlackId;
	@Column(length = 255)
	private String slackMessage;

	// 정적 팩토리 메서드
	public static Slack of(String receiverId, String message) {

		return new Slack(receiverId, message);
	}

	// 프라이빗 생성자 (정적 팩토리 메서드만 사용하도록 제한)
	private Slack(String SlackReceivedSlackId, String slackMessage) {
		this.SlackReceivedSlackId = SlackReceivedSlackId;
		this.slackMessage = slackMessage;
	}

}
