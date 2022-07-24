package com.eka.farmerconnect.exception;

public class FunctionalException extends Exception{
	
	private static final long serialVersionUID = -7786007225326323535L;
	
	private String message;
	
	public FunctionalException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
