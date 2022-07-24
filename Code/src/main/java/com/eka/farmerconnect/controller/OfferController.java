package com.eka.farmerconnect.controller;

import static com.eka.farmerconnect.constants.FarmerConnectConstants.INVALID_NUMBER_VALUE;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.MISSING_MANDATORY_FIELDS;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.OFFER_TYPE_UPDATE_ERR;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.PURCHASE;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.SALE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eka.cac.security.AuthContext;
import com.eka.db.mongo.MongoDBQueryException;
import com.eka.exceptions.FunctionalException;
import com.eka.farmerconnect.constants.CustomerBidsCollectionConstants;
import com.eka.farmerconnect.constants.FarmerConnectConstants;
import com.eka.farmerconnect.model.GeneralSettings;
import com.eka.farmerconnect.model.OfferField;
import com.eka.farmerconnect.model.PublishedBid;
import com.eka.farmerconnect.service.OfferService;
import com.eka.util.PermCodeEnum;
import com.eka.util.PermissionsAllowed;

@RestController
@RequestMapping(value = "/offers")
public class OfferController extends FCBaseController{
	
	@Autowired
	private OfferService offerService;
	
	/**
	 * Fetches allowed values(configured via the Collection(s)) for the specified field(s).
	 * 
	 * @param fieldIds - Sublist of: location,quantityUnit,quality,product,incoTerm,priceUnit
	 * @return
	 */
	@PermissionsAllowed(values = PermCodeEnum.STD_APP_BIDS_OFFEROR)
	@GetMapping(value = "/fields/{fieldIds}/values", produces = APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getOfferFieldsValues(@PathVariable List<String> fieldIds,
													   HttpServletRequest request) throws FunctionalException{
		
		List<OfferField> offerFields = new ArrayList<>();
		for (int i = 0; i < fieldIds.size(); i++) {
			OfferField offerField = OfferField.findById(fieldIds.get(i));
			if(null == offerField) {
				throw new FunctionalException("Invalid field "+ fieldIds.get(i));
			}
			offerFields.add(offerField);
		}
		
		return new ResponseEntity<Object>(offerService
												.getMultipleFieldValues(constructCollectionAPIUriBuilder(request), 
																getRequestHeaders(request), 
																offerFields),
										  HttpStatus.OK);
	}

	/**
	 * Fetches allowed values(configured via the Collection) for the specified field.
	 * 
	 * @param fieldId - Supported Values: location,quantityUnit,quality,product,incoTerm,priceUnit
	 * @return
	 */
	@PermissionsAllowed(values = PermCodeEnum.STD_APP_BIDS_OFFEROR)
	@GetMapping(value = "/field/{fieldId}/values", produces = APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getOfferFieldValues(@PathVariable String fieldId,
													  HttpServletRequest request) throws FunctionalException{
		
		OfferField offerField = OfferField.findById(fieldId);
		if(null == offerField) {
			throw new FunctionalException("Invalid field "+ offerField);
		}
		
		return new ResponseEntity<Object>(offerService
												.getFieldValues(constructCollectionAPIUriBuilder(request), 
																getRequestHeaders(request), 
																offerField),
										  HttpStatus.OK);
	}

	/**
	 * Publishes an offer on behalf of the current user. 
	 * Important: Invoking this API successfully would
	 * make the FarmerConnect app paradigm user-based.  
	 * 
	 * @param inputBid Example:
 * 			{
		    "product": "Wheat",
		    "quality": "Top Quality",
		    "cropYear": "2018/2019",
		    "location": "BLR",
		    "publishedPrice": 300,
		    "expiryDateISOString": "2019-08-31T05:47:36.785Z",
		    "priceUnit": "USD/MT",
		    "incoTerm": "ABC",
		    "quantity": 10,
		    "quantityUnit": "MT",
		     --------- optional fields ------
		    "shipmentDateISOString": "2019-12-31T05:47:36.785Z",
		    "rolesToPublish": "",
		    "offerType": "Purchase"
			}
	 * @return HttpStatus - 201 With {"bidId:"systemGeneratedBidId"}
	 * @throws MongoDBQueryException 
	 * @throws FunctionalException 
	 */
	@PermissionsAllowed(values = PermCodeEnum.STD_APP_BIDS_OFFEROR)
	@PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> publishOffer(@RequestBody @Valid PublishedBid offerDetails,
												HttpServletRequest request) throws MongoDBQueryException, FunctionalException{
		
		GeneralSettings appSettings = getAppGeneralSettings(request);
		validateOfferFields(offerDetails);
		String bidId = offerService.publishOffer(constructCollectionAPIUriBuilder(request), 
														getRequestHeaders(request), 
														offerDetails,
														AuthContext.getCurrentUserName(),
														appSettings);
		Map<String, String> response = new HashMap<>();
		response.put(CustomerBidsCollectionConstants.BID_ID, bidId);
		return new ResponseEntity<Object>(response, HttpStatus.CREATED);
	}
	
	private void validateOfferFields(PublishedBid offerDetails) throws FunctionalException {
		
		if(StringUtils.isAnyBlank(offerDetails.getCropYear(), offerDetails.getIncoTerm(), offerDetails.getLocation(),
								  offerDetails.getPriceUnit(), offerDetails.getProduct(), offerDetails.getQuality(), 
								  offerDetails.getExpiryDateISOString(), offerDetails.getDeliveryFromDateISOString(),
								  offerDetails.getDeliveryToDateISOString())){
			throw new FunctionalException(FarmerConnectConstants.MISSING_MANDATORY_FIELDS);
		}
		
		final Instant now = Instant.now();
		if(Instant.parse(offerDetails.getExpiryDateISOString()).isBefore(now)) {
			throw new FunctionalException(FarmerConnectConstants.INVALID_EXPIRY_DATE);
		} 
		if(Instant.parse(offerDetails.getDeliveryFromDateISOString()).isBefore(now) ||
				Instant.parse(offerDetails.getDeliveryToDateISOString()).isBefore(now)) {
			throw new FunctionalException(FarmerConnectConstants.INVALID_DELIVERY_DATE);
		}
		
		if(offerDetails.getPublishedPrice() <= 0 || offerDetails.getQuantity() <= 0) {
			throw new FunctionalException(FarmerConnectConstants.INVALID_NUMBER_VALUE);
		}
		
		if(!StringUtils.equalsAny(offerDetails.getOfferType(), SALE, PURCHASE)) {
			throw new FunctionalException(FarmerConnectConstants.INVALID_OFFER_TYPE);
		}
	}

	/**
	 * Update a published offer.
	 * @param bidId
	 * @param updatedFields one or more of the below fields:
	 * 		{
		    "product": "Wheat",
		    "quality": "Top Quality",
		    "cropYear": "2018/2019",
		    "location": "BLR",
		    "publishedPrice": 300,
		    "expiryDateISOString": "2019-08-31T05:47:36.785Z",
		    "priceUnit": "USD/MT",
		    "incoTerm": "ABC",
		    "quantity": "10",
		    "quantityUnit": "MT",
		    "shipmentDateISOString": "2019-12-31T05:47:36.785Z",
		    "rolesToPublish": ""
			}
			To unset a non-mandatory field, specify value as empty string(for numeric values as well)
	 * 
	 * @return HttpStatus - 200 With Empty Response or 400 If bidId is invalid/expired/inaccessible to user.
	 * @throws FunctionalException 
	 */
	@PutMapping(value = "/{bidId}", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
	@PermissionsAllowed(values = PermCodeEnum.STD_APP_BIDS_OFFEROR)
	public ResponseEntity<Object> updateOffer(@PathVariable String bidId,
											  @RequestBody PublishedBid updatedBid,
											  HttpServletRequest request) throws FunctionalException {
		validateUpdateParams(updatedBid);
		boolean isUpdated = offerService.updateOffer(constructCollectionAPIUriBuilder(request), 
													getRequestHeaders(request),
													bidId,
													updatedBid,
													AuthContext.getCurrentUserName(), 
													String.valueOf(AuthContext
															  		.getCurrentTenant()
															  		.getId()));
		Map<String, Object> response = new HashMap<>();
		HttpStatus statusCode = isUpdated ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		response.put(FarmerConnectConstants.SUCCESS, isUpdated);
		return new ResponseEntity<>(response, statusCode);
	}

	private void validateUpdateParams(PublishedBid updatedBid) throws FunctionalException {
		
		if(StringUtils.equalsAny("", updatedBid.getProduct(), updatedBid.getQuality(), updatedBid.getCropYear(), updatedBid.getLocation(), 
				updatedBid.getExpiryDateISOString(), updatedBid.getPriceUnit(), updatedBid.getIncoTerm())) {
			throw new FunctionalException(MISSING_MANDATORY_FIELDS);
		}
		
		if((null != updatedBid.getQuantity() && updatedBid.getQuantity() <= 0) ||
				null != updatedBid.getPublishedPrice() && updatedBid.getPublishedPrice() <= 0) {
			throw new FunctionalException(INVALID_NUMBER_VALUE);
		}
		
		if(!StringUtils.isBlank(updatedBid.getOfferType())) {
			throw new FunctionalException(OFFER_TYPE_UPDATE_ERR);
		}
	}

	/**
	 * Delete a published offer.
	 * 
	 * @param bidId
	 * @param httpRequest
	 * @return
	 * @throws FunctionalException 
	 */
	@DeleteMapping(value = "/{bidId}", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
	@PermissionsAllowed(values = PermCodeEnum.STD_APP_BIDS_OFFEROR)
	public ResponseEntity<Object> deleteOffer(@PathVariable String bidId,
											  HttpServletRequest request) throws FunctionalException {
		
		boolean isDeleted = offerService.deleteOffer(constructCollectionAPIUriBuilder(request), 
													getRequestHeaders(request),
													bidId,
													AuthContext
												  		.getCurrentUserName(), 
												  	String.valueOf(AuthContext
															  		.getCurrentTenant()
															  		.getId()));
		Map<String, Object> response = new HashMap<>();
		HttpStatus statusCode = isDeleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		response.put(FarmerConnectConstants.SUCCESS, isDeleted);
		if(!isDeleted) {
			response.put(FarmerConnectConstants.MSG, FarmerConnectConstants.INVALID_BID_ID_MSG);
		}
		return new ResponseEntity<>(response, statusCode);
	}
}
