package com.eka.farmerconnect.controller;

import static com.eka.farmerconnect.constants.FarmerConnectConstants.INVALID_NUMBER_VALUE;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.MISSING_MANDATORY_FIELDS;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.OFFER_TYPE_UPDATE_ERR;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.PURCHASE;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.SALE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eka.db.mongo.MongoDBQueryException;
import com.eka.farmerconnect.constants.CustomerBidOfferConstants;
import com.eka.farmerconnect.constants.FarmerConnectConstants;
import com.eka.farmerconnect.exception.FunctionalException;
import com.eka.farmerconnect.model.AuthContext;
import com.eka.farmerconnect.model.GeneralSettings;
import com.eka.farmerconnect.model.Offer;
import com.eka.farmerconnect.model.OfferField;
import com.eka.farmerconnect.service.OfferService;
import com.eka.util.PermCodeEnum;
import com.eka.util.PermissionsAllowed;

@RestController
@RequestMapping("${fc.api.context}/offers")
public class OfferController extends FCBaseController{
	
	@Autowired
	private OfferService offerService;
	
	/**
	 * Fetches allowed values(configured via the Collection(s)) for the specified field(s).
	 * 
	 * @param fieldIds - Sublist of: location,quantityUnit,quality,product,incoTerm,priceUnitpaymentTerms,packingType,packingSize
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
												.getMultipleFieldValues(offerFields),
										  HttpStatus.OK);
	}

	/**
	 * Fetches allowed values(configured via the Collection) for the specified field.
	 * 
	 * @param fieldId - Supported Values: location,quantityUnit,quality,product,incoTerm,priceUnitpaymentTerms,packingType,packingSize
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
												.getFieldValues(offerField),
										  HttpStatus.OK);
	}

	/**
	 * Endpoint for the Bidders/Agents to fetch published offers.
	 * If 'rolesToPublish' field of 'Price Sheet' template is mapped,
	 * will fetch the bids which are applicable for the logged-in user's roles. 
	 * 
	 * @param request
	 * @param requestParams
	 * @return
	 * @throws MongoDBQueryException
	 */
	@PermissionsAllowed(values = {PermCodeEnum.STD_APP_BIDDER_BID, PermCodeEnum.STD_APP_BIDS_OFFEROR, PermCodeEnum.FC_FARMER_BIDS_MGMT})
	@GetMapping
	public ResponseEntity<Object> getOffers(HttpServletRequest request,
											@RequestParam String requestParams) throws MongoDBQueryException {
		
		JSONObject requestObject = new JSONObject(requestParams);
		JSONObject sortBy = requestObject.optJSONObject(FarmerConnectConstants.SORTBY);
		JSONArray filters = requestObject.optJSONArray(FarmerConnectConstants.FILTERS);
		JSONObject pagination = requestObject.optJSONObject(FarmerConnectConstants.PAGINATION);
		
		String tenantId = AuthContext.getCurrentTenantId();
		return new ResponseEntity<>(offerService.getOffers(sortBy,
													      filters, 
													      pagination, 
													      tenantId, 
													      getAppGeneralSettings(request)), 
									HttpStatus.OK);
	}
	
	/**
	 * Publishes an offer on behalf of the current user. 
	 * Important: Invoking this API successfully would
	 * make the FarmerConnect app paradigm user-based.  
	 * 
	 * @param offerDetails Example:
 * 			{
		    "product": "Wheat",
		    "quality": "Top Quality",
		    "cropYear": "2018/2019",
		    "location": "BLR",
		    "publishedPrice": 300,
		    "expiryDateInMillis": 1580828355193,
		    "priceUnit": "USD/MT",
		    "incoTerm": "ABC",
		    "quantity": 10,
		    "quantityUnit": "MT",
		     --------- optional fields ------
		    "deliveryFromDateInMillis": 1580827355193,
		    "deliveryToDateInMillis": 1580827355193,
		    "rolesToPublish": "",
		    "offerType": "Purchase",
		    "paymentTerms":"",
    		"packingType":"",
    		"packingSize":""
			}
	 * @return HttpStatus - 201 With {"bidId:"systemGeneratedBidId"}
	 * @throws MongoDBQueryException 
	 * @throws FunctionalException 
	 */
	@PermissionsAllowed(values = PermCodeEnum.STD_APP_BIDS_OFFEROR)
	@PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> publishOffer(@RequestBody @Valid Offer offerDetails,
												HttpServletRequest request) throws MongoDBQueryException, FunctionalException{
		
		GeneralSettings appSettings = getAppGeneralSettings(request);
		validateOfferFields(offerDetails);
		String bidId = offerService.publishOffer(offerDetails,
												AuthContext.getCurrentUsername(),
												appSettings);
		Map<String, String> response = new HashMap<>();
		response.put(CustomerBidOfferConstants.OFFER_ID, bidId);
		return new ResponseEntity<Object>(response, HttpStatus.CREATED);
	}
	
	private void validateOfferFields(Offer offerDetails) throws FunctionalException {
		
		if(StringUtils.isAnyBlank(offerDetails.getCropYear(), offerDetails.getIncoTerm(), offerDetails.getLocation(),
								  offerDetails.getPriceUnit(), offerDetails.getProduct(), offerDetails.getQuality())){
			throw new FunctionalException(FarmerConnectConstants.MISSING_MANDATORY_FIELDS);
		}
		validateDateFields(offerDetails);
		if(!StringUtils.equalsAny(offerDetails.getOfferType(), SALE, PURCHASE)) {
			throw new FunctionalException(FarmerConnectConstants.INVALID_OFFER_TYPE);
		}
	}

	private void validateDateFields(Offer offerDetails) throws FunctionalException {
		
		final long currTime = System.currentTimeMillis();
		if(currTime > offerDetails.getExpiryDateInMillis()) {
			throw new FunctionalException(FarmerConnectConstants.INVALID_EXPIRY_DATE);
		} 
		if(currTime > offerDetails.getDeliveryFromDateInMillis() || 
				currTime > offerDetails.getDeliveryToDateInMillis()) {
			throw new FunctionalException(FarmerConnectConstants.INVALID_DELIVERY_DATE);
		}
		
		if(offerDetails.getPublishedPrice() <= 0 || offerDetails.getQuantity() <= 0) {
			throw new FunctionalException(FarmerConnectConstants.INVALID_NUMBER_VALUE);
		}
		
	}

	/**
	 * Update a published offer.
	 * @param offerId
	 * @param updatedFields one or more of the below fields:
	 * 		{
		    "quality": "Top Quality",
		    "cropYear": "2018/2019",
		    "location": "BLR",
		    "publishedPrice": 300,
		    "expiryDateInMillis": 1580828355193,
		    "priceUnit": "USD/MT",
		    "incoTerm": "ABC",
		    "quantity": "10",
		    "quantityUnit": "MT",
		    "deliveryFromDateInMillis": 1580827355193,
		    "deliveryToDateInMillis": 1580827355193,
		    "rolesToPublish": "",
		    "paymentTerms":"",
    		"packingType":"",
    		"packingSize":""
			}
			To un-set a non-mandatory field, specify value as empty string(for numeric values as well)
	 * 
	 * @return HttpStatus - 200 With Empty Response or 400 If bidId is invalid/expired/inaccessible to user.
	 * @throws FunctionalException 
	 * @throws MongoDBQueryException 
	 */
	@PutMapping(value = "/{offerId}", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
	@PermissionsAllowed(values = PermCodeEnum.STD_APP_BIDS_OFFEROR)
	public ResponseEntity<Object> updateOffer(@PathVariable String offerId,
											  @RequestBody Offer updatedOffer,
											  HttpServletRequest request) throws FunctionalException, MongoDBQueryException {
		validateUpdateParams(updatedOffer);
		boolean isUpdated = offerService.updateOffer(offerId,
													updatedOffer,
													AuthContext.getCurrentUsername(), 
													AuthContext.getCurrentTenantId());
		Map<String, Object> response = new HashMap<>();
		HttpStatus statusCode = isUpdated ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		response.put(FarmerConnectConstants.SUCCESS, isUpdated);
		return new ResponseEntity<>(response, statusCode);
	}

	private void validateUpdateParams(Offer updatedOffer) throws FunctionalException {
		
		if(StringUtils.equalsAny("", updatedOffer.getProduct(), updatedOffer.getQuality(), updatedOffer.getCropYear(), updatedOffer.getLocation(), 
				updatedOffer.getPriceUnit(), updatedOffer.getIncoTerm())) {
			throw new FunctionalException(MISSING_MANDATORY_FIELDS);
		}
		validateDateFields(updatedOffer);
		if((null != updatedOffer.getQuantity() && updatedOffer.getQuantity() <= 0) ||
				(null != updatedOffer.getPublishedPrice() && updatedOffer.getPublishedPrice() <= 0)) {
			throw new FunctionalException(INVALID_NUMBER_VALUE);
		}
		
		if(!StringUtils.isBlank(updatedOffer.getOfferType())) {
			throw new FunctionalException(OFFER_TYPE_UPDATE_ERR);
		}
	}

	/**
	 * Delete a published offer.
	 * 
	 * @param offerId
	 * @param httpRequest
	 * @return
	 * @throws FunctionalException 
	 * @throws MongoDBQueryException 
	 */
	@DeleteMapping(value = "/{offerId}", produces = APPLICATION_JSON_UTF8_VALUE)
	@PermissionsAllowed(values = PermCodeEnum.STD_APP_BIDS_OFFEROR)
	public ResponseEntity<Object> deleteOffer(@PathVariable String offerId,
											  HttpServletRequest request) throws FunctionalException, MongoDBQueryException {
		
		boolean isDeleted = offerService.deleteOffer(offerId,
													AuthContext.getCurrentUsername(), 
												  	AuthContext.getCurrentTenantId());
		Map<String, Object> response = new HashMap<>();
		HttpStatus statusCode = isDeleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		response.put(FarmerConnectConstants.SUCCESS, isDeleted);
		if(!isDeleted) {
			response.put(FarmerConnectConstants.MSG, FarmerConnectConstants.INVALID_BID_ID_MSG);
		}
		return new ResponseEntity<>(response, statusCode);
	}
	
	/**
	 * Endpoint for the Bidders to fetch unique values of a field amongst the 'Published Offers'.
	 * 
	 * @param fieldName
	 * @return
	 * @throws MongoDBQueryException
	 */
	@PermissionsAllowed(values = {PermCodeEnum.STD_APP_BIDDER_BID, PermCodeEnum.FC_FARMER_BIDS_MGMT})
	@RequestMapping(value = "/values/{fieldName}", method = RequestMethod.GET)
	public ResponseEntity<Object> getUniqueValuesOfField(@PathVariable String fieldName) throws MongoDBQueryException{
		
		return new ResponseEntity<>(offerService
										.getUniqueValueSetFromOffers(
												AuthContext.getCurrentTenantId(), 
												fieldName), 
								    HttpStatus.OK);
	}
}
