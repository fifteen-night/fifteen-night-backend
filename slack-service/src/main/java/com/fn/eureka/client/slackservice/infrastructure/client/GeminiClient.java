package com.fn.eureka.client.slackservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fn.eureka.client.slackservice.application.dto.request.GeminiRequestDto;
import com.fn.eureka.client.slackservice.application.dto.response.GeminiResponseDto;

@FeignClient(name = "geminiClient", url = "https://api.example.com/v1beta/models")
public interface GeminiClient {
	@PostMapping("/{model}:generateContent")
	GeminiResponseDto getCompletion(
		@PathVariable("model") String model,
		@RequestParam("key") String key,
		@RequestBody GeminiRequestDto geminiRequestDto
	);
}
