package com.fn.eureka.client.deliveryservice.presentation.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NaverMapDirResponseDto {

	private int code;
	private String currentDateTime;
	private String message;
	private Route route;

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Route {
		private List<Traoptimal> traoptimal;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Traoptimal {
		private List<GuideDto> guide;
		private List<List<Double>> path;
		private SummaryDto summary;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class GuideDto {
		private int distance;
		private int duration;
		private String instructions;
		private int pointIndex;
		private int type;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class SummaryDto {
		private List<List<Double>> bbox;
		private String departureTime;
		private int distance;
		private int duration;
		private int fuelPrice;
		private int taxiFare;
		private int tollFare;
		private GoalDto goal;
		private StartDto start;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class GoalDto {
		private int dir;
		private List<Double> location;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class StartDto {
		private List<Double> location;
	}
}
