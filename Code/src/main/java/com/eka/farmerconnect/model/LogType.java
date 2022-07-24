package com.eka.farmerconnect.model;

public enum LogType {

	PUBLISHED_PRICE(0),
	ACCEPTED(1),
	REJECTED(-1),
	CANCELLED(-2);
	
	Integer value;
	
	public Integer getValue() {
		return value;
	}

	private LogType(Integer value) {
		this.value = value;
	}
}
