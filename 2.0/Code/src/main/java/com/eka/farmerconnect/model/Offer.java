package com.eka.farmerconnect.model;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Offer extends BaseOffer {

	private Double quantity;
	private String expiresIn;
	
	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}
	
	@Override
	public JSONObject toJSON(){
		
		return super.toJSON()
						.put("quantity", quantity)
						.put("expiresIn", expiresIn);
	}
	
	public JSONObject toJSON(String currentUserName){
		
		return this.toJSON().put("username", currentUserName);
	}
}