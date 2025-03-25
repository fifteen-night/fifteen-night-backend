package com.fn.eureka.client.slackservice.application.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GeminiResponseDto {
	private List<Candidate> candidates;

	@Getter
	public static class Candidate {
		private Content content;
		private String finishReason;
		private int index;
		List<SafetyRating> safetyRatings;
	}

	@Getter
	public static class Content {
		private List<TextPart> parts;
		private String role;
	}

	@Getter
	public static class TextPart {
		private String text;
	}

	@Getter
	public static class SafetyRating {
		private String category;
		private String probability;
	}

	public String extractText() {
		return candidates != null && !candidates.isEmpty()
			? candidates.get(0).content.parts.get(0).text
			: "Gemini 응답 메시지를 생성할 수 없습니다.";
	}
}
