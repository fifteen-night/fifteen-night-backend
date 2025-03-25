package com.fn.eureka.client.slackservice.application.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeminiRequestDto {
	private List<Content> contents;

	public GeminiRequestDto(String text) {
		this.contents = List.of(new Content(List.of(new TextPart(text))));
	}

	public static class Content {
		private List<TextPart> parts;

		// Content 객체에 TextPart 설정
		public Content(List<TextPart> parts) {
			this.parts = parts;
		}

		@JsonProperty("parts") // 'parts' 필드를 직렬화하도록 설정
		public List<TextPart> getParts() {
			return parts;
		}
	}

	public interface Part {}

	// TextPart 생성자를 protected로 두고 외부에서 직접 생성할 수 없도록 함
	public static class TextPart implements Part {
		private String text;

		// 생성자를 protected로 두어서 외부에서 직접 생성할 수 없도록 함
		protected TextPart(String text) {
			this.text = text;
		}

		@JsonProperty("text") // 'text' 필드를 직렬화하도록 설정
		public String getText() {
			return text;
		}
	}

	public static class InlineDataPart implements Part {
		private InlineData inlineData;

		public InlineDataPart(InlineData inlineData) {
			this.inlineData = inlineData;
		}
	}

	public static class InlineData {
		private String mimeType;
		private String data;

		public InlineData(String mimeType, String data) {
			this.mimeType = mimeType;
			this.data = data;
		}
	}

	// 팩토리 메서드로 외부에서 TextPart 객체를 생성할 수 있게 함
	public static TextPart createTextPart(String text) {
		return new TextPart(text);
	}

	// Getter와 Setter
	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}
}
