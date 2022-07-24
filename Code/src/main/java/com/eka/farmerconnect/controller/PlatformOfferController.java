package com.eka.farmerconnect.controller;

import static com.eka.farmerconnect.constants.FarmerConnectConstants.DATA;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.DEVICE_ID_HEADER;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.FETCH_CURR_USER_ROLES_URI;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.NAME;
import static com.eka.farmerconnect.constants.TemplateConstants.COL_ROLES_TO_PUBLISH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.eka.cac.security.AuthContext;
import com.eka.db.mongo.MongoDBQueryException;
import com.eka.farmerconnect.constants.FarmerConnectConstants;
import com.eka.farmerconnect.service.BidService;
import com.eka.util.PermCodeEnum;
import com.eka.util.PermissionsAllowed;

@RestController
@RequestMapping(value="/published-bids", produces=APPLICATION_JSON_UTF8_VALUE)
public class PlatformOfferController extends FCBaseController{
	
	private static final Logger logger = ESAPI.getLogger(PlatformOfferController.class);

	@Autowired
	private BidService bidService;
	
	/**
	 * Endpoint for the Bidders/Agents to fetch published bids.
	 * If 'rolesToPublish' field of 'Price Sheet' template is mapped,
	 * will fetch the bids which are applicable for the logged-in user's roles. 
	 * 
	 * @param request
	 * @param requestParams
	 * @return
	 * @throws MongoDBQueryException
	 */
	@PermissionsAllowed(values = {PermCodeEnum.STD_APP_BIDDER_BID, PermCodeEnum.STD_APP_BIDS_OFFEROR, PermCodeEnum.FC_FARMER_BIDS_MGMT})
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<Object> publihsedBids(HttpServletRequest request,
												@RequestParam String requestParams) throws MongoDBQueryException {
		
		JSONObject requestObject = new JSONObject(requestParams);
		JSONObject sortBy = requestObject.optJSONObject(FarmerConnectConstants.SORTBY);
		JSONArray filters = requestObject.optJSONArray(FarmerConnectConstants.FILTERS);
		JSONObject pagination = requestObject.optJSONObject(FarmerConnectConstants.PAGINATION);
		
		String tenantId = String.valueOf(AuthContext.getCurrentTenant().getId());
		List<String> userRoleNames = null;
		if(bidService.isFieldMappedInDataset(COL_ROLES_TO_PUBLISH, tenantId)) {
			userRoleNames = getCurrentUserRoleNames(request);
		}
		return new ResponseEntity<>(bidService.getPublishedBids(sortBy,
																filters, 
																pagination, 
																tenantId, 
																userRoleNames), 
									HttpStatus.OK);
	}
	
	/**
	 * Endpoint for the Bidders to fetch unique values of a field amongst the 'Published Bids'.
	 * 
	 * @param fieldName
	 * @return
	 * @throws MongoDBQueryException
	 */
	@PermissionsAllowed(values = {PermCodeEnum.STD_APP_BIDDER_BID, PermCodeEnum.FC_FARMER_BIDS_MGMT})
	@RequestMapping(value = "/values/{fieldName}", method = RequestMethod.GET)
	public ResponseEntity<String> getUniqueValuesOfField(@PathVariable String fieldName) throws MongoDBQueryException{
		
		return new ResponseEntity<>(bidService
										.getUniqueValueArrayFromPublishedBids(String
																.valueOf(AuthContext
																			.getCurrentTenant()
																			.getId()), 
															  fieldName).toString(), 
								    HttpStatus.OK);
	}
}
