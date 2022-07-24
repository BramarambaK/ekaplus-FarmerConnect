package com.eka.farmerconnect.model;

import java.time.Instant;

import javax.annotation.Nonnegative;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class CustomerBid extends BaseBid {
	
	// Quantity & Shipment Date are provided by bidder
	// Kept as String to support localization for mobile apps
	@NotNull
	@Nonnegative
	private String quantity;
	@NotNull
	private String status;
	
	// Fields stored in the collection under 
	// negotiationLogs but kept here to get user input
	private String remarks;
	private String price;
	private String pendingOn;
	
	// Deduced internally
	private String refId;
	private String customerId;
	private String clientId;
	private long updatedDate;
	private Integer version;
	private String agentId;
	private String currentBidRating;
	private String userId;
	
	/*
	 * Fields in the internal JSONObject: by:"Bidder/Offeror", remarks:"", price:123.45, date:ISODate, userId:User login id
	 *
	 * Sample Json: "negotiationLogs":[{"logType":0,"price":450,"by":"Offeror","remarks":"Published Bid Price"},{"date":"NumberLong(1525238022208)","price":485,"by":"Bidder","userId":"Naveen@ekaplus.com","remarks":"Please accept"},{"date":"NumberLong(1525238054443)","price":475,"by":"Offeror","userId":"Fconnect@ekaplus.com","remarks":"Not more than that!"}]
	 */ 
	private JSONArray negotiationLogs;
	// Used internally to copy entries in output collection
	private Boolean bidUpdated;
	
	@NotNull
	@Override
	public String getBidId() {
		return super.getBidId();
	}
	
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPendingOn() {
		return pendingOn;
	}

	public void setPendingOn(String pendingOn) {
		this.pendingOn = pendingOn;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getCurrentBidRating() {
		return currentBidRating;
	}

	public void setCurrentBidRating(String currentBidRating) {
		this.currentBidRating = currentBidRating;
	}

	public JSONArray getNegotiationLogs() {
		return negotiationLogs;
	}

	public void setNegotiationLogs(JSONArray negotiationLogs) {
		this.negotiationLogs = negotiationLogs;
	}
	public Boolean getBidUpdated() {
		return bidUpdated;
	}

	public void setBidUpdated(Boolean bidUpdated) {
		this.bidUpdated = bidUpdated;
  }
  
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Converts the customer bid to JSONArray for
	 * pushing the record to system bid to contract collection.
	 * Important: Order Needs to be maintained as per
	 * the fields defined in config.properties file.
	 * @return
	 */
	public JSONArray toContractDataArray() {
		
		return new JSONArray()
					.put(getBidId()+"-"+getRefId())
					.put(Instant.now().toString())
					.put(getIncoTerm())
					.put(StringUtils.isEmpty(getPaymentTerms()) ? "" : getPaymentTerms())
					.put(StringUtils.isEmpty(getQuantityUnit()) ? "" : getQuantityUnit())
					.put(getProduct())
					.put(getLocation())
					.put(getCropYear())
					.put(getQuality())
					.put(getQuantity())
					.put(StringUtils.isEmpty(getPackingType()) ? "" : getPackingType())
					.put(StringUtils.isEmpty(getPackingSize()) ? "" : getPackingSize())
					.put(getQuantity())
					.put(getPublishedPrice())
					.put(getPriceUnit())
					.put(Instant.ofEpochMilli(getDeliveryFromDateInMillis()).toString())
					.put(Instant.ofEpochMilli(getDeliveryToDateInMillis()).toString())
					.put(getPriceUnit())
					.put(StringUtils.isEmpty(getOfferType()) ? "" : getOfferType());
	}
	
	public static CustomerBid convertToCustomerBid(JSONObject bidAsJson) {
		
		CustomerBid bid = new CustomerBid();
		bid.setRefId(bidAsJson.optString("refId"));
		bid.setBidId(bidAsJson.optString("bidId"));
		bid.setIncoTerm(bidAsJson.optString("incoTerm"));
		bid.setPaymentTerms(bidAsJson.optString("paymentTerms"));
		bid.setQuantityUnit(bidAsJson.optString("quantityUnit"));
		bid.setProduct(bidAsJson.optString("product"));
		bid.setLocation(bidAsJson.optString("location"));
		bid.setCropYear(bidAsJson.optString("cropYear"));
		bid.setQuality(bidAsJson.optString("quality"));
		bid.setQuantity(bidAsJson.optString("quantity"));
		bid.setPackingType(bidAsJson.optString("packingType"));
		bid.setPackingSize(bidAsJson.optString("packingSize"));
		bid.setPublishedPrice(Double.parseDouble(bidAsJson.optString("publishedPrice")));
		bid.setPriceUnit(bidAsJson.optString("priceUnit"));
		bid.setDeliveryFromDateInMillis(bidAsJson.optLong("deliveryFromDateInMillis"));
		bid.setDeliveryToDateInMillis(bidAsJson.optLong("deliveryToDateInMillis"));
		bid.setOfferType(bidAsJson.optString("offerType"));
		return bid;
	}
	
	@Override
	public JSONObject toJSON() {
		
		return super.toJSON()
						.put("refId", refId)
						.put("quantity", Double.parseDouble(quantity.replace(",", ".")))
						// To maintain convention across various Mongo Collections
						.put("client_id", clientId)
						.put("customerId", customerId)
						.put("status", status)
						.put("version", version)
						.put("pendingOn", pendingOn)
						.put("updatedDate", updatedDate)
						.put("agentId", agentId)
						.put("negotiationLogs", negotiationLogs)
						.put("rolesToPublish", getRolesToPublish())
						.put("bidUpdated", bidUpdated);
	}
}
