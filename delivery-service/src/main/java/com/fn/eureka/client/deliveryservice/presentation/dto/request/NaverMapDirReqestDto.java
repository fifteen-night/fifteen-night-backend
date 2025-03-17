package com.fn.eureka.client.deliveryservice.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NaverMapDirReqestDto {

	@NotBlank
	private String goal;

	@NotBlank
	private String start;

}
