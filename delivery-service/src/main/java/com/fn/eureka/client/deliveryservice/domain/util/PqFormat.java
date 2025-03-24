package com.fn.eureka.client.deliveryservice.domain.util;

import lombok.Getter;

@Getter
public class PqFormat implements Comparable<PqFormat> {

	private final String index;
	private final int time;

	public PqFormat(String index, int time) {
		this.index = index;
		this.time = time;
	}

	@Override
	public int compareTo(PqFormat o) {
		return Integer.compare(this.time, o.time);
	}

}
