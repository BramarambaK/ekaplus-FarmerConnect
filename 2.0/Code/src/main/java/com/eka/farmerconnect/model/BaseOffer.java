package com.eka.farmerconnect.model;

import java.util.Date;

import org.json.JSONObject;

import com.google.gson.Gson;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class BaseOffer 
{

	private String offerId;
	private String product;
	private String quality;
	private String cropYear;
	private String location;
	private Double publishedPrice;
	private Date expiryDate;
	private String priceUnit;
	private String incoTerm;
	private String quantityUnit;
	private String rolesToPublish;
	private String offerType;
	private Date deliveryFromDate;
	private Date deliveryToDate;
	private String paymentTerms;
	private String packingType;
	private String packingSize;
	
	// Internal fields
	private String username;
	private int version;
	private String clientId;
	private long deliveryToDateInMillis;
	private long deliveryFromDateInMillis;
	private long expiryDateInMillis;
	
	// Offeror related fields, always stitched NOT saved as part of offer
	private String rating;
	private String offerorName;
	private String offerorMobileNo;
	
	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
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

	public String getCropYear() {
		return cropYear;
	}

	public void setCropYear(String cropYear) {
		this.cropYear = cropYear;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getPublishedPrice() {
		return publishedPrice;
	}

	public void setPublishedPrice(Double publishedPrice) {
		this.publishedPrice = publishedPrice;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}

	public String getIncoTerm() {
		return incoTerm;
	}

	public void setIncoTerm(String incoTerm) {
		this.incoTerm = incoTerm;
	}

	public String getQuantityUnit() {
		return quantityUnit;
	}

	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}

	public long getDeliveryToDateInMillis() {
		return deliveryToDateInMillis;
	}

	public void setDeliveryToDateInMillis(long deliveryToDateInMillis) {
		this.deliveryToDateInMillis = deliveryToDateInMillis;
	}

	public long getDeliveryFromDateInMillis() {
		return deliveryFromDateInMillis;
	}

	public void setDeliveryFromDateInMillis(long deliveryFromDateInMillis) {
		this.deliveryFromDateInMillis = deliveryFromDateInMillis;
	}

	public long getExpiryDateInMillis() {
		return expiryDateInMillis;
	}

	public void setExpiryDateInMillis(long expiryDateInMillis) {
		this.expiryDateInMillis = expiryDateInMillis;
	}

	public String getOfferorName() {
		return offerorName;
	}

	public void setOfferorName(String offerorName) {
		this.offerorName = offerorName;
	}

	public String getOfferorMobileNo() {
		return offerorMobileNo;
	}

	public void setOfferorMobileNo(String offerorMobileNo) {
		this.offerorMobileNo = offerorMobileNo;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getRolesToPublish() {
		return rolesToPublish;
	}

	public void setRolesToPublish(String rolesToPublish) {
		this.rolesToPublish = rolesToPublish;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	public Date getDeliveryFromDate() {
		return deliveryFromDate;
	}

	public void setDeliveryFromDate(Date deliveryFromDate) {
		this.deliveryFromDate = deliveryFromDate;
	}

	public Date getDeliveryToDate() {
		return deliveryToDate;
	}

	public void setDeliveryToDate(Date deliveryToDate) {
		this.deliveryToDate = deliveryToDate;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getPackingType() {
		return packingType;
	}

	public void setPackingType(String packingType) {
		this.packingType = packingType;
	}

	public String getPackingSize() {
		return packingSize;
	}

	public void setPackingSize(String packingSize) {
		this.packingSize = packingSize;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	public JSONObject toJSON(){
		
		return new JSONObject()
						// To maintain convention across various Mongo Collections
						.put("client_id", clientId)
						.put("publishedPrice", publishedPrice)
						.put("offerId", offerId)
						.put("product", product)
						.put("quality", quality)
						.put("cropYear", cropYear)
						.put("location", location)
						.put("priceUnit", priceUnit)
						.put("incoTerm", incoTerm)
						.put("quantityUnit", quantityUnit)
						.put("deliveryFromDateInMillis", deliveryFromDateInMillis)
						.put("deliveryToDateInMillis", deliveryToDateInMillis)
						.put("expiryDateInMillis", expiryDateInMillis)
						.put("username", username)
						.put("rating", rating)
						.put("offerType", offerType)
						.put("deliveryFromDate", deliveryFromDate)
						.put("deliveryToDate", deliveryToDate)
						.put("paymentTerms", paymentTerms)
						.put("packingType", packingType)
						.put("packingSize", packingSize)
						.put("offerorName", offerorName)
						.put("offerorMobileNo", offerorMobileNo);
	}
}
