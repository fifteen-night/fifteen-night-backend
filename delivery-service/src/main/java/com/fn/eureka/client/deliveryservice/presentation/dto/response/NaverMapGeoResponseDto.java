package com.fn.eureka.client.deliveryservice.presentation.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class NaverMapGeoResponseDto {

	private String status;
	private Meta meta;

	private List<AddressDto> addresses;

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Meta {
		private int totalCount;
		private int page;
		private int count;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class AddressDto {
		private String roadAddress;
		private String jibunAddress;
		private String englishAddress;
		private List<AddressElementDto> addressElements;
		private String x;  // 경도
		private String y;  // 위도
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class AddressElementDto {
		private List<String> types;
		private String longName;
		private String shortName;
		private String code;
	}

}
