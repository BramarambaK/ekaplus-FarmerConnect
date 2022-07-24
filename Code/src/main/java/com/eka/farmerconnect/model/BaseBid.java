package com.eka.farmerconnect.model;

import java.util.Date;

import org.json.JSONObject;

import com.eka.dataobjects.BaseModel;
import com.eka.document.parser.annotation.CustomEntity;
import com.eka.document.parser.annotation.Field;
import com.eka.farmerconnect.constants.TemplateConstants;
import com.google.gson.Gson;

@CustomEntity
public class BaseBid extends BaseModel {

	@Field(TemplateConstants.COL_PTA_REF_NUMBER)
	private String bidId;
	
	@Field(TemplateConstants.COL_PRODUCT)
	private String product;
	
	@Field(TemplateConstants.COL_QUALITY)
	private String quality;

	@Field(TemplateConstants.COL_CROPYEAR)
	private String cropYear;

	@Field(TemplateConstants.COL_LOCATION)
	private String location;
	
	@Field(TemplateConstants.COL_PUBLISHED_PRICE)
	private Double publishedPrice;
	
	@Field(TemplateConstants.COL_EXPIRY_DATE)
	private Date expiryDate;

	@Field(TemplateConstants.COL_PRICE_UNIT)
	private String priceUnit;
	
	@Field(TemplateConstants.COL_INCO_TERM)
	private String incoTerm;
	
	// Indicative Non-Mandatory Field
	@Field(TemplateConstants.COL_QUANTITY_UNIT)
	private String quantityUnit;
	
	// Indicative Non-Mandatory Field
	@Field(TemplateConstants.COL_USERNAME)
	private String username;
	
	// Indicative Non-Mandatory Field
	@Field(TemplateConstants.COL_ROLES_TO_PUBLISH)
	private String rolesToPublish;
	
	@Field(TemplateConstants.COL_OFFER_TYPE)
	private String offerType;
	
	@Field(TemplateConstants.COL_DELIVERY_FROM_DATE)
	private Date deliveryFromDate;
	
	@Field(TemplateConstants.COL_DELIVERY_TO_DATE)
	private Date deliveryToDate;
	
	@Field(TemplateConstants.COL_PAYMENT_TERMS)
	private String paymentTerms;
	
	@Field(TemplateConstants.COL_PACKING_TYPE)
	private String packingType;
	
	@Field(TemplateConstants.COL_PACKING_SIZE)
	private String packingSize;
	private long deliveryToDateInMillis;
	private long deliveryFromDateInMillis;
	
	// Offeror related fields, always stitched NOT saved as part of bid
	private String rating;
	private String offerorName;
	private String offerorMobileNo;
	
	
	public String getProduct() {
		return product;
	}

	public String getBidId() {
		return bidId;
	}

	public void setBidId(String bidId) {
		this.bidId = bidId;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	public JSONObject toJSON(){
		
		return new JSONObject()
						.put("publishedPrice", publishedPrice)
						.put("bidId", bidId)
						.put("product", product)
						.put("quality", quality)
						.put("cropYear", cropYear)
						.put("location", location)
						.put("priceUnit", priceUnit)
						.put("incoTerm", incoTerm)
						.put("quantityUnit", quantityUnit)
						.put("deliveryFromDateInMillis", deliveryFromDateInMillis)
						.put("deliveryToDateInMillis", deliveryToDateInMillis)
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
