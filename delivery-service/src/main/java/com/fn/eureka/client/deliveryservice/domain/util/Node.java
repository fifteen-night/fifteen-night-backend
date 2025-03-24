package com.fn.eureka.client.deliveryservice.domain.util;

import lombok.Getter;

@Getter
public class Node {

	private final String nextHubName;
	private final int timeCost;

	public Node(String nextHubName, int timeCost) {
		this.nextHubName = nextHubName;
		this.timeCost = timeCost;
	}

}
