package com.fn.eureka.client.hubservice.hub.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageInfo {
	private long page;
	private long size;
	private long totalPage;
	private long totalElement;
}