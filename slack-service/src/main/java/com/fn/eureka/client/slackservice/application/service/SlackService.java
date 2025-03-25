package com.fn.eureka.client.slackservice.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fn.common.global.dto.CommonResponse;
import com.fn.eureka.client.slackservice.application.dto.request.SlackMessageRequestDto;
import com.fn.eureka.client.slackservice.application.dto.request.SlackUpdateRequestDto;
import com.fn.eureka.client.slackservice.application.dto.response.SlackGetResponseDto;
import com.fn.eureka.client.slackservice.application.dto.response.SlackMessageResponseDto;
import com.fn.eureka.client.slackservice.application.dto.response.SlackUpdateResponseDto;

public interface SlackService {

	CommonResponse<SlackMessageResponseDto> sendSlackMessage(SlackMessageRequestDto requestDto);

	CommonResponse<SlackGetResponseDto> getSlackMessage(UUID slackId);

	CommonResponse<Page<SlackGetResponseDto>> getSlackMessages(String keyword, Pageable pageable);

	CommonResponse<SlackUpdateResponseDto> updateSlackMessage(UUID slackId, SlackUpdateRequestDto requestDto);

	CommonResponse<Void> deleteSlackMessage(UUID slackId);
}

