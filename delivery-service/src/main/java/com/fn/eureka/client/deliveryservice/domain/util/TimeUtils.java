package com.fn.eureka.client.deliveryservice.domain.util;

import java.time.Duration;
import java.time.LocalTime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeUtils {

	// millisec >> Time 으로 변환
	public static LocalTime convertTime(int milliseconds) {

		log.info("convert time {} ms", milliseconds);

		Duration duration = Duration.ofMillis(milliseconds);
		long hours = duration.toHours();
		long minutes = duration.toMinutes() % 60;
		long seconds = duration.getSeconds() % 60;

		return LocalTime.of((int)hours, (int)minutes, (int)seconds);
	}

	// Time >> millisec변환
	public static int convertMilliseconds(LocalTime time) {

		return (time.getHour() * 3600000) + (time.getMinute() * 60000) + (time.getSecond() * 1000);
	}
}
