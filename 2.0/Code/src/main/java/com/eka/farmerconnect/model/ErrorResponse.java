package com.eka.farmerconnect.model;

public class ErrorResponse {
	
	private String msg;
	private boolean success;

	public ErrorResponse(String errorMessage) {
		super();
		this.msg = errorMessage;
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "{\"msg\":\"" + msg + "\", \"success\":" + success + "}";
	}
}
