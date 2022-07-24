package com.eka.farmerconnect.model;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.eka.document.parser.annotation.CustomEntity;
import com.eka.document.parser.annotation.Field;
import com.eka.farmerconnect.constants.TemplateConstants;

@CustomEntity
public class PublishedBid extends BaseBid {

	// Indicative Non-Mandatory Field
	@Field(TemplateConstants.COL_QUANTITY)
	private Double quantity;
	
	private String expiresIn;
	
	/*
	 *  EXTRA FIELDS:
	 *  Required for pushing offer data received 
	 *  from mobile clients to the offer collection  
	 */
	private String expiryDateISOString;
	private String deliveryFromDateISOString;
	private String deliveryToDateISOString;
	
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

	public String getExpiryDateISOString() {
		return expiryDateISOString;
	}

	public void setExpiryDateISOString(String expiryDateISOString) {
		this.expiryDateISOString = expiryDateISOString;
	}

	public String getDeliveryFromDateISOString() {
		return deliveryFromDateISOString;
	}

	public void setDeliveryFromDateISOString(String deliveryFromDateISOString) {
		this.deliveryFromDateISOString = deliveryFromDateISOString;
	}

	public String getDeliveryToDateISOString() {
		return deliveryToDateISOString;
	}

	public void setDeliveryToDateISOString(String deliveryToDateISOString) {
		this.deliveryToDateISOString = deliveryToDateISOString;
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
	
	/**
	 * Converts the published bid to JSONArray for
	 * pushing the record to system offers collection.
	 * Important: Order Needs to be maintained as per
	 * the fields defined in config.properties file.
	 * @return
	 */
	
	public JSONArray toOfferDataArray(String username) {
		
		return new JSONArray()
					.put(getBidId())
					.put(getProduct())
					.put(getQuality())
					.put(getCropYear())
					.put(getLocation())
					.put(getPublishedPrice())
					/*  Note:
					 *  For un-setting a number/string field, empty string is passed as the value.
					 *  But the behavior is different for a date field. If empty value is passed
					 *  for a date field, it's simply ignored. 
					 */
					.put(StringUtils.isEmpty(getExpiryDateISOString()) ? "" : getExpiryDateISOString())
					.put(getPriceUnit())
					.put(getIncoTerm())
					.put(getQuantity())
					.put(StringUtils.isEmpty(getQuantityUnit()) ? "" : getQuantityUnit())
					.put(username)
					.put(StringUtils.isEmpty(getRolesToPublish()) ? "" : getRolesToPublish())
					.put(StringUtils.isEmpty(getOfferType()) ? "" : getOfferType())
					.put(getDeliveryFromDateISOString())
					.put(getDeliveryToDateISOString())
					.put(StringUtils.isEmpty(getPaymentTerms()) ? "" : getPaymentTerms())
					.put(StringUtils.isEmpty(getPackingType()) ? "" : getPackingType())
					.put(StringUtils.isEmpty(getPackingSize()) ? "" : getPackingSize());
	}
	
	/**
	 * Overloaded method to pass alternative object if the current object has null or "" properties
	 * */
	public JSONArray toOfferDataArray(String username,Object alternateValue) {
		
		return new JSONArray()
					.put(getBidId())
					.put(getProduct())
					.put(getQuality())
					.put(getCropYear())
					.put(getLocation())
					.put(getPublishedPrice())
					/*  Note:
					 *  For un-setting a number/string field, empty string is passed as the value.
					 *  But the behavior is different for a date field. If empty value is passed
					 *  for a date field, it's simply ignored. 
					 */
					.put(StringUtils.isEmpty(getExpiryDateISOString()) ? alternateValue : getExpiryDateISOString())
					.put(getPriceUnit())
					.put(getIncoTerm())
					.put(getQuantity())
					.put(StringUtils.isEmpty(getQuantityUnit()) ? alternateValue : getQuantityUnit())
					.put(username)
					.put(StringUtils.isEmpty(getRolesToPublish()) ? alternateValue : getRolesToPublish())
					.put(StringUtils.isEmpty(getOfferType()) ? alternateValue : getOfferType())
					.put(getDeliveryFromDateISOString())
					.put(getDeliveryToDateISOString())
					.put(StringUtils.isEmpty(getPaymentTerms()) ? alternateValue : getPaymentTerms())
					.put(StringUtils.isEmpty(getPackingType()) ? alternateValue : getPackingType())
					.put(StringUtils.isEmpty(getPackingSize()) ? alternateValue : getPackingSize());
	}
	
	public JSONArray toExpireOfferDataArray(String username,Object alternateValue) {
		
		return new JSONArray()
					.put(getBidId())
					.put(getProduct())
					.put(getQuality())
					.put(getCropYear())
					.put(getLocation())
					.put(getPublishedPrice())
					/*  Note:
					 *  For un-setting a number/string field, empty string is passed as the value.
					 *  But the behavior is different for a date field. If empty value is passed
					 *  for a date field, it's simply ignored. 
					 */
					.put(StringUtils.isEmpty(getExpiryDateISOString()) ? "" : getExpiryDateISOString())
					.put(getPriceUnit())
					.put(getIncoTerm())
					.put(getQuantity())
					.put(StringUtils.isEmpty(getQuantityUnit()) ? alternateValue : getQuantityUnit())
					.put(username)
					.put(StringUtils.isEmpty(getRolesToPublish()) ? "" : getRolesToPublish())
					.put(StringUtils.isEmpty(getOfferType()) ? alternateValue : getOfferType())
					.put(StringUtils.isEmpty(getExpiryDateISOString()) ? "" : getExpiryDateISOString())
					.put(StringUtils.isEmpty(getExpiryDateISOString()) ? "" : getExpiryDateISOString())
					.put(StringUtils.isEmpty(getPaymentTerms()) ? alternateValue : getPaymentTerms())
					.put(StringUtils.isEmpty(getPackingType()) ? alternateValue : getPackingType())
					.put(StringUtils.isEmpty(getPackingSize()) ? alternateValue : getPackingSize());
	}
}
