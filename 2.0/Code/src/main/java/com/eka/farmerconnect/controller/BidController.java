package com.eka.farmerconnect.controller;

import static com.eka.farmerconnect.constants.CollectionAPIConstants.COLLECTION_URI;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.ACCEPTED;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.AGENT;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.BIDDER;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.IN_PROGRESS;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OFFEROR;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.PENDING_ON;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.PENDING_ON_NONE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.PRICE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.RATED_ON;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.RATING;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.REMARKS;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.STATUS;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.FILTERS;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.INVALID_DELIVERY_DATE;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.MISSING_MANDATORY_FIELDS;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.PAGINATION;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.SORTBY;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eka.db.mongo.MongoDBQueryException;
import com.eka.farmerconnect.exception.FunctionalException;
import com.eka.farmerconnect.model.AuthContext;
import com.eka.farmerconnect.model.BidConversionEntity;
import com.eka.farmerconnect.model.CustomerBid;
import com.eka.farmerconnect.model.GeneralSettings;
import com.eka.farmerconnect.service.BidService;
import com.eka.farmerconnect.service.CommonService;
import com.eka.util.PermCodeEnum;
import com.eka.util.PermissionsAllowed;
import com.mongodb.client.result.UpdateResult;

@RestController
@RequestMapping(path="${fc.api.context}/bids", produces=APPLICATION_JSON_UTF8_VALUE)
public class BidController extends FCBaseController{
	
	private static final Logger logger = ESAPI.getLogger(BidController.class);
	
	@Autowired
	private BidService bidService;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * Gets all the bids made by the logged-in Bidder. 
	 * Used to populate 'My Bids' listing for the Bidder in the mobile app.
	 * 
	 * @param requestParams Sample:
	 * Description:
	 * Filters: Filters can be clubbed. In advanced filters, logicalOperator and secondFilter are optional. 
	 * Basic filters are only applicable for string fields.
	 * 	{
		  "sortBy": {
		    "publishedPrice/quantity/cropYear/location/product/quality/ptaRefNumber/priceUnit/quantityUnit/username": "ASC/DESC"
		  },
		  "filters": [
		    {
		      "type": "basic",
		      "columnType": 1,
		      "columnId": "cropYear/location/product/quality/ptaRefNumber/priceUnit/quantityUnit/username",
		      "operator": "in/nin",
		      "value": [
		        "value 1...",
		        "value n"
		      ]
		    },
		    {
		      "type": "advanced",
		      "firstFilter": {
		        "columnType": 1,
		        "operator": "equals/notEquals/isBlank/isNotBlank/contains/notContains/startsWith/endsWith",
		        "columnId": "cropYear/location/product/quality/ptaRefNumber/priceUnit/quantityUnit/username",
		        "value": [
		          "value 1...",
		          "value n"
		        ]
		      },
		      "logicalOperator": "AND/OR",
		      "secondFilter": {
		        "columnType": 2,
		        "operator": "equals/notEquals/greaterThan/lessThan/greaterThanOrEqual/lessThanOrEqual",
		        "columnId": "publishedPrice/quantity",
		        "value": [
		          "value 1...",
		          "value n"
		        ]
		      }
		    }
		  ],
		  "pagination": {
		    "limit": 25,
		    "page": 0
		  }
		}
	 * 
	 * @return
	 * @throws FunctionalException 
	 * @throws MongoDBQueryException 
	 */
	@PermissionsAllowed(values = {PermCodeEnum.STD_APP_BIDDER_BID})
	@RequestMapping(value="/farmer", method=RequestMethod.GET)
	public ResponseEntity<String> getAllBids(@RequestParam(defaultValue = "{}") String requestParams) throws FunctionalException, MongoDBQueryException {
		
		String tenantId = AuthContext.getCurrentTenantId();
		JSONObject requestJSON = new JSONObject(requestParams);
		JSONObject sortBy = requestJSON.optJSONObject(SORTBY);
		JSONArray filters = requestJSON.optJSONArray(FILTERS);
		JSONObject pagination = requestJSON.optJSONObject(PAGINATION);
		return new ResponseEntity<>(bidService.getAllMyBids(sortBy, filters, pagination, tenantId, null).toString(), HttpStatus.OK);
	}
	
	/**
	 * Gets all the bids made by the specified Bidder. 
	 * Used by Agent to populate 'My Bids' listing for the chosen Bidder in the mobile app.
	 * 
	 * @param bidderId
	 * @param requestParams See Sample at: {@link com.eka.farmerconnect.controller.BidController#getAllBids(String)}
	 * @return
	 * @throws FunctionalException
	 * @throws MongoDBQueryException
	 */
	@PermissionsAllowed(values = {PermCodeEnum.FC_FARMER_BIDS_MGMT})
	@RequestMapping(value="/farmer/{bidderId}", method=RequestMethod.GET)
	public ResponseEntity<String> getBidsOnBehalfOfBidder(@PathVariable Integer bidderId,
														  @RequestParam(defaultValue = "{}") String requestParams) throws FunctionalException, MongoDBQueryException {
		
		String tenantId = AuthContext.getCurrentTenantId();
		JSONObject requestJSON = new JSONObject(requestParams);
		JSONObject sortBy = requestJSON.optJSONObject(SORTBY);
		JSONArray filters = requestJSON.optJSONArray(FILTERS);
		JSONObject pagination = requestJSON.optJSONObject(PAGINATION);
		return new ResponseEntity<>(bidService.getAllMyBids(sortBy, filters, pagination, tenantId, bidderId).toString(), HttpStatus.OK);
	}
	
	/**
	 * Gets all the bids for the logged-in Offeror. 
	 * If the 'username' field of template is mapped, then the 
	 * logged-in offeror will get the bids which were initiated 
	 * on the bids published under his/her name. Otherwise, logged-in
	 * offeror gets all the bids for the client.
	 * 
	 * @param requestParams Sample: See Sample at: {@link com.eka.farmerconnect.controller.BidController#getAllBids(String)}
	 * @return
	 * @throws FunctionalException
	 * @throws MongoDBQueryException
	 */
	@PermissionsAllowed(values = {PermCodeEnum.STD_APP_BIDS_OFFEROR})
	@RequestMapping(value="/offeror", method=RequestMethod.GET)
	public ResponseEntity<String> getAllBidsForOfferor(@RequestParam(defaultValue = "{}") String requestParams) throws FunctionalException, MongoDBQueryException {
		
		String tenantId = AuthContext.getCurrentTenantId();
		JSONObject requestJSON = new JSONObject(requestParams);
		JSONObject sortBy = requestJSON.optJSONObject(SORTBY);
		JSONArray filters = requestJSON.optJSONArray(FILTERS);
		JSONObject pagination = requestJSON.optJSONObject(PAGINATION);
		return new ResponseEntity<>(bidService.getAllBidsForOfferor(sortBy, filters, pagination, tenantId, AuthContext.getCurrentUsername()).toString(), HttpStatus.OK);
	}
	
	/**
	 * Gets a specific bid's details based on the refId. 
	 * Can be used both all viz. Bidder, Offeror and Agent.
	 * 
	 * @param refId
	 * @return
	 * @throws FunctionalException 
	 * @throws MongoDBQueryException 
	 */
	@PermissionsAllowed(values = {PermCodeEnum.STD_APP_BIDDER_BID, PermCodeEnum.FC_FARMER_BIDS_MGMT, PermCodeEnum.STD_APP_BIDS_OFFEROR})
	@RequestMapping(value = "/{refId}", method = RequestMethod.GET)
	public ResponseEntity<String> bidDetail(@PathVariable String refId) throws MongoDBQueryException {
		
		return new ResponseEntity<>(bidService.getBidDetailByReferenceId(refId, 
																		Integer.parseInt(AuthContext.getCurrentTenantId())).toString(), 
									HttpStatus.OK);
	}
	
	/**
	 * Endpoint for the Bidders to create bids.
	 * 
	 * @param inputBid :Requires bidId, quantity and status. 
	 * 					If status is 'In Progress', counter price is also required.
	 * @return
	 * @throws Exception
	 */
	@PermissionsAllowed(values = {PermCodeEnum.STD_APP_BIDDER_BID})
	@RequestMapping(value="", method=RequestMethod.POST)
	public ResponseEntity<String> createBid(@Valid @RequestBody CustomerBid inputBid,
											HttpServletRequest request) throws Exception{
		
		validateDeliveryDate(inputBid);
		bidService.createBid(inputBid, Integer.parseInt(AuthContext.getCurrentTenantId()), 
							 AuthContext.getCurrentUsername(), BIDDER, null, 
							 getBidConversionEntity(request, ACCEPTED.equals(inputBid.getStatus())));
		return new ResponseEntity<>(CREATED);
	}
	
	private BidConversionEntity getBidConversionEntity(HttpServletRequest request, boolean assignURIAndHeaders) {
		
		GeneralSettings appSettings = getAppGeneralSettings(request);
		if(assignURIAndHeaders) {
			return new BidConversionEntity(getRequestHeaders(request), 
										  commonService
										  		.getPlatformUriBuilder(AuthContext.getCurrentHttpRequest(), 
										  								COLLECTION_URI), 
										  appSettings);
		}
		return new BidConversionEntity(appSettings);
	}

	private void validateDeliveryDate(CustomerBid inputBid) throws FunctionalException {
		long currTime = System.currentTimeMillis(); 
		if(currTime > inputBid.getDeliveryFromDateInMillis() || currTime > inputBid.getDeliveryToDateInMillis()) {
			throw new FunctionalException(INVALID_DELIVERY_DATE);
		}
		
	}

	/**
	 * Endpoint for the Agents to create bids on bidders' behalf.
	 * @param bidderId
	 * @param inputBid :Requires bidId, quantity and status. 
	 * 					If status is 'In Progress', counter price is also required.
	 * @return
	 * @throws Exception
	 */
	@PermissionsAllowed(values = {PermCodeEnum.FC_FARMER_BIDS_MGMT})
	@RequestMapping(value="/{bidderId}", method=RequestMethod.POST)
	public ResponseEntity<String> createBidOnBehalfOfFarmer(@PathVariable Integer bidderId,
															@Valid @RequestBody CustomerBid inputBid,
															HttpServletRequest request) throws Exception{
		
		validateDeliveryDate(inputBid);
		bidService.createBid(inputBid, Integer.parseInt(AuthContext.getCurrentTenantId()), 
							 AuthContext.getCurrentUsername(), AGENT, bidderId,
							 getBidConversionEntity(request, ACCEPTED.equals(inputBid.getStatus())));
		return new ResponseEntity<>(CREATED);
	}
	
	/**
	 * Endpoint for the Offerors/Bidders to update('Counter'/'Accept'/'Reject') existing bids.
	 * .
	 * @param refId
	 * @param requestBody: Status and remarks as key value pairs.
	 * @return
	 * @throws Exception
	 */
	@PermissionsAllowed(values = {PermCodeEnum.STD_APP_BIDDER_BID, PermCodeEnum.STD_APP_BIDS_ACCEPT, PermCodeEnum.STD_APP_BIDS_REJECT, PermCodeEnum.STD_APP_BIDS_COUNTER})
	@RequestMapping(value="/{refId}", method=RequestMethod.PUT)
	public ResponseEntity<String> updateBid(@PathVariable String refId,
											@RequestBody String requestBody,
											@RequestParam(defaultValue="false") boolean byOfferor,
											HttpServletRequest httpRequest) throws Exception{
		
		String userRoleType = validateAndGetUserRoleType(byOfferor, AuthContext.getCurrentUserPermCodes());
		JSONObject bidUpdateParams = validateRequestAndUpdateBidParams(requestBody, OFFEROR.equals(userRoleType) ? BIDDER : OFFEROR);
		UpdateResult result = bidService.updateBid(refId, userRoleType, AuthContext.getCurrentUsername(), 
													Integer.parseInt(AuthContext.getCurrentTenantId()), bidUpdateParams,
												   getBidConversionEntity(httpRequest, ACCEPTED.equals(bidUpdateParams.getString(STATUS))));
		if(result.getModifiedCount() == 0){
			throw new FunctionalException("Either refId doesn't exist or the user can't update the bid at the present!");
		}
		bidService.statusUpdated(bidUpdateParams.getString(STATUS),refId,Integer.parseInt(AuthContext.getCurrentTenantId()));
		return new ResponseEntity<>(SUCCESS_BODY, HttpStatus.OK);
	}
	
	/**
	 * Endpoint for the Agents to update('Counter'/'Accept'/'Reject') 
	 * existing bids on a bidder's behalf.
	 * 
	 * @param refId
	 * @param bidderId
	 * @param requestBody: Status and remarks as key value pairs.
	 * @return
	 * @throws Exception
	 */
	@PermissionsAllowed(values = {PermCodeEnum.FC_FARMER_BIDS_MGMT})
	@RequestMapping(value="/{refId}/{bidderId}", method=RequestMethod.PUT)
	public ResponseEntity<String> updateBidOnBehalfOfFarmer(@PathVariable String refId,
															@PathVariable Integer bidderId,
															@RequestBody String requestBody,
															HttpServletRequest httpRequest) throws Exception{
		
		JSONObject bidUpdateParams = validateRequestAndUpdateBidParams(requestBody, OFFEROR);
		UpdateResult result = bidService.updateBid(refId, AGENT, AuthContext.getCurrentUsername(), 
													Integer.parseInt(AuthContext.getCurrentTenantId()), bidUpdateParams,
												   getBidConversionEntity(httpRequest, ACCEPTED.equals(bidUpdateParams.getString(STATUS))));
		if(result.getModifiedCount() == 0){
			throw new FunctionalException("Either refId doesn't exist or the user can't update the bid at the present!");
		}
		bidService.statusUpdated(bidUpdateParams.getString(STATUS),refId,Integer.parseInt(AuthContext.getCurrentTenantId()));
		return new ResponseEntity<>(SUCCESS_BODY, HttpStatus.OK);
	}
	
	/**
	 * Endpoint for the Offerors to cancel an accepted bid.
	 * 
	 * @param refIds
	 * @param cancellationInfo: {"remarks":"Cancellation Remarks"}
	 * @return
	 * @throws Exception
	 */
	@PermissionsAllowed(values = PermCodeEnum.STD_APP_BIDS_CANCEL)
	@RequestMapping(value="/offeror/cancel/{refIds}", method=RequestMethod.POST)
	public ResponseEntity<String> cancelBids(@PathVariable List<String> refIds,
											 @RequestBody Map<String, Object> cancellationInfo) throws Exception{
		
		List<String> unupdatedRefIds = bidService.cancelBids(refIds, 
															cancellationInfo, 
															AuthContext.getCurrentUsername(), 
															AuthContext.getCurrentTenantId());
		if(!CollectionUtils.isEmpty(unupdatedRefIds)){
			throw new FunctionalException("Bid(s) with following refId(s) could not be updated: "+ unupdatedRefIds + 
					". Either refId(s) doesn't exist or the user can't update the bid at the present!");
		}
		return new ResponseEntity<>(SUCCESS_BODY, HttpStatus.OK);
	}
	
	/**
	 * Endpoint for the Offerors to update('Counter'/'Accept'/'Reject') existing bids.
	 * 
	 * @param refIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PermissionsAllowed(values = {PermCodeEnum.STD_APP_BIDS_COUNTER, PermCodeEnum.STD_APP_BIDS_ACCEPT, PermCodeEnum.STD_APP_BIDS_REJECT})
	@RequestMapping(value="/offeror/{refIds}", method=RequestMethod.POST)
	public ResponseEntity<String> updateBids(@PathVariable List<String> refIds,
											 @RequestBody String request,
											 HttpServletRequest httpRequest) throws Exception{
		
		JSONObject bidUpdateParams = validateRequestAndUpdateBidParams(request, BIDDER);
		List<String> unupdatedRefIds = bidService.updateBids(refIds, OFFEROR, AuthContext.getCurrentUsername(), 
													Integer.parseInt(AuthContext.getCurrentTenantId()), bidUpdateParams,
												   getBidConversionEntity(httpRequest, ACCEPTED.equals(bidUpdateParams.getString(STATUS))));
		if(!CollectionUtils.isEmpty(unupdatedRefIds)){
			throw new FunctionalException("Bid(s) with following refId(s) could not be updated: "+ unupdatedRefIds + 
										  ". Either refId(s) do(es) not exist or the user can't update the bid at the present!");
		}
		return new ResponseEntity<>(SUCCESS_BODY, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/logs/{refId}", method = RequestMethod.GET)
	public ResponseEntity<String> getBidLogs(@PathVariable String refId) throws MongoDBQueryException {
		return new ResponseEntity<>(bidService.getBidLogs(refId, 
				Integer.parseInt(AuthContext.getCurrentTenantId())).toString(), HttpStatus.OK);
	}
	
	/**
	 * Endpoint for the Bidders to fetch unique values of a field amongst bids 'My Bids'.
	 * 
	 * @param fieldName
	 * @return
	 * @throws MongoDBQueryException
	 */
	@PermissionsAllowed(values = {PermCodeEnum.STD_APP_BIDDER_BID})
	@RequestMapping(value = "/values/{fieldName}", method = RequestMethod.GET)
	public ResponseEntity<String> getUniqueValuesOfField(@PathVariable String fieldName) throws MongoDBQueryException{
		
		return new ResponseEntity<>(bidService
										.getCustomerBidValue(AuthContext.getCurrentTenantId(),
															  AuthContext.getCurrentUsername(),
															  fieldName,
															  AuthContext.getCurrentUsername(),
															  null).toString(), 
								    HttpStatus.OK);
	}	

	/**
	 * Endpoint for the Agents to fetch unique values of a field amongst the bids made by the specified Bidder.
	 * 
	 * @param fieldName
	 * @param bidderId
	 * @return
	 * @throws MongoDBQueryException
	 */
	@PermissionsAllowed(values = {PermCodeEnum.FC_FARMER_BIDS_MGMT})
	@RequestMapping(value = "/values/{fieldName}/{bidderId}", method = RequestMethod.GET)
	public ResponseEntity<String> getUniqueValuesOfField(@PathVariable String fieldName,
														 @PathVariable Integer bidderId) throws MongoDBQueryException{
		
		return new ResponseEntity<>(bidService
										.getCustomerBidValue(AuthContext.getCurrentTenantId(),
															  AuthContext.getCurrentUsername(),
															  fieldName,
															  null,
															  bidderId).toString(), 
								    HttpStatus.OK);
	}	

	/**
	 * Endpoint for the Bidders to rate an accepted bid.
	 * 
	 * @param refId
	 * @param rating
	 * @param requestBody: {"remarks":"Rating Remarks", "ratedOn":["Quality",../"All"]}
	 * @return
	 * @throws Exception
	 */
	@PermissionsAllowed(values = {PermCodeEnum.STD_APP_BIDDER_BID})
	@RequestMapping(value="/ratings/{refId}/{rating}", method=RequestMethod.POST)
	public ResponseEntity<String> rateBid(@PathVariable String refId,
										  @PathVariable Integer rating,
										  @RequestBody String requestBody) throws Exception{
		
		JSONObject requestJson = new JSONObject(requestBody);
		JSONObject ratingInfo = new JSONObject();
		ratingInfo.put(RATED_ON, requestJson.getJSONArray(RATED_ON));
		ratingInfo.put(REMARKS, requestJson.optString(REMARKS));
		ratingInfo.put(RATING, rating);
		
		UpdateResult result = bidService.rateBid(refId,
												 ratingInfo,
												 AuthContext.getCurrentTenantId());
		if(result.getModifiedCount() == 0){
			throw new FunctionalException("Invalid refId or the user can't rate the bid at present!");
		}
		return new ResponseEntity<>(SUCCESS_BODY, HttpStatus.OK);
	}
	
	private String validateAndGetUserRoleType(boolean byOfferor, List<String> currentPermCodes) throws FunctionalException {
		
		if(byOfferor && currentPermCodes.contains(PermCodeEnum.STD_APP_BIDS_OFFEROR.getPermCode())) {
			return OFFEROR;
		}else if(currentPermCodes.contains(PermCodeEnum.STD_APP_BIDDER_BID.getPermCode())) {
			return BIDDER;
		}
		throw new FunctionalException("UserUnauthorized!");
	}
	
	private JSONObject validateRequestAndUpdateBidParams(String request, String willBePendingOn) throws FunctionalException {
		
		JSONObject bidUpdateParams = new JSONObject(request);
		String status = bidUpdateParams.optString(STATUS);
		if(StringUtils.isEmpty(status)){
			throw new FunctionalException(MISSING_MANDATORY_FIELDS);
		}
		
		if(IN_PROGRESS.equalsIgnoreCase(status) && !bidUpdateParams.has(PRICE)){
			throw new FunctionalException(MISSING_MANDATORY_FIELDS);
		}
		bidUpdateParams.put(PENDING_ON, IN_PROGRESS.equalsIgnoreCase(bidUpdateParams.getString(STATUS)) ? 
										willBePendingOn	: PENDING_ON_NONE);
		return bidUpdateParams;
	}
}
