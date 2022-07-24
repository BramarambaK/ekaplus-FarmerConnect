package com.eka.blockchain.contract.model;

import org.json.JSONObject;

public class ContractDetails {

	private String customerId;
	private String customerAddress;
	private String price;
	private String product;
	private String quality;
	private String quantity;
	private String refId;
	private String bidId;
	private String rating;
	private String ratingDetails;
	private String userId;
	private String userAddress;
	private String contractAddress;
	private int clientId;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getBidId() {
		return bidId;
	}

	public void setBidId(String bidId) {
		this.bidId = bidId;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getRatingDetails() {
		return ratingDetails;
	}

	public void setRatingDetails(String ratingDetails) {
		this.ratingDetails = ratingDetails;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}
	
	public String toString() {
		return this.toJSON().toString();
	}
	
	public JSONObject toJSON() {
		
		return new JSONObject()
				.put("customerId", customerId)
				.put("customerAddress", customerAddress)
				.put("price", price)
				.put("product", product)
				.put("quality", quality)
				.put("quantity", quantity)
				.put("refId", refId)
				.put("bidId", bidId)
				.put("rating", rating)
				.put("ratingDetails", ratingDetails)
				.put("userId", userId)
				.put("userAddress", userAddress)
				.put("contractAddress", contractAddress)
				.put("clientId", clientId);
	}

}
