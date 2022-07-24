package com.eka.farmerconnect.service;

import static com.eka.db.mongo.constants.UpdateOperatorEnum.addToSet;
import static com.eka.db.mongo.constants.UpdateOperatorEnum.inc;
import static com.eka.db.mongo.constants.UpdateOperatorEnum.set;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.ACCEPTED;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.AGENT;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.AGENT_ID;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.APPLICABLE_ROLES;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.AVG_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.BIDDER;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.BIDS_COLLECTION_NAME;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.BID_UPDATED;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.BY;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.CANCELLED;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.CARET;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.COLUMN_ID;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.COLUMN_TYPE;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.COMMA;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.CURR_BID_RATING;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.CUSTOMER_ID;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.DATE;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.DOLLAR;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.DOT;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.EQ_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.EXISTS_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.FIRST_FILTER;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.FITER_BASIC;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.IN_PROGRESS;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.LATEST_BIDDER_PRICE;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.LATEST_OFFEROR_PRICE;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.LATEST_REMARKS;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.LOGICAL_OPERATOR;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.LOG_TYPE;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.NEGOTIATION_LOGS;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.NE_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.OFFEROR;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.OPERATOR;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.OPTIONS_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.OPT_CASE_INSENSITIVE;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.PENDING_ON;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.PENDING_ON_NONE;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.PRICE;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.PRICE_UNIT;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.PRIMARY_KEY;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.PUBLISHED_BID_PRICE_REMARKS;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.RATING;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.RATING_INFO;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.REF_ID;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.REGEX_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.REMARKS;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.ROLES_TO_PUBLISH;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.ROLE_SEPARATOR;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.SECOND_FILTER;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.SHIPMENT_DATE;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.STATUS;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.TYPE;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.UPDATED_BY;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.UPDATED_DATE;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.USERNAME;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.VALUE;
import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.VERSION;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.DEFAULT_OFFER_TYPE;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.EXPIRY_DATE;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.FARMER_CONNECT_APP_ID;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.FORMAT_DAYS;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.INVALID_BID_ID_FORMAT;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.PREFIX_FOR_REF_ID;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.RATING_PENDING;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.REGEX_COMMA;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.ROLES_PATTERN_BEGIN;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.ROLES_PATTERN_END;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.TEMPLATE_ID;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.TODAY;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.TOMORROW;
import static com.eka.farmerconnect.constants.TemplateConstants.COL_EXPIRY_DATE;
import static com.eka.farmerconnect.constants.TemplateConstants.COL_PTA_REF_NUMBER;
import static com.eka.farmerconnect.constants.TemplateConstants.COL_ROLES_TO_PUBLISH;
import static com.eka.farmerconnect.constants.TemplateConstants.COL_USERNAME;
import static com.eka.service.constants.SmartAPPPlatformConstants.CLIENT_ID;
import static com.eka.service.constants.SmartAPPPlatformConstants.EMPTY_STR;
import static com.eka.service.constants.SmartAPPPlatformConstants.USER_ID_CAMEL_CASE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.eka.blockchain.contract.model.ContractDetails;
import com.eka.cac.security.AuthContext;
import com.eka.cac.security.crypto.utils.CryptoUtils;
import com.eka.core.audit.AuditAction;
import com.eka.core.audit.annotation.AuditAcitivity;
import com.eka.core.audit.annotation.Entity;
import com.eka.db.hibernate.BasicReadOnlyHibernateDAO;
import com.eka.db.hibernate.NamedQueryParameters;
import com.eka.db.hibernate.ReadOnly;
import com.eka.db.mongo.MongoDBAggregationQuery;
import com.eka.db.mongo.MongoDBQuery;
import com.eka.db.mongo.MongoDBQueryException;
import com.eka.db.mongo.MongoQueryFactory;
import com.eka.db.mongo.constants.ConditionEnum;
import com.eka.db.mongo.constants.OrderEnum;
import com.eka.db.mongo.constants.ProjectionEnum;
import com.eka.exceptions.FunctionalException;
import com.eka.farmerconnect.constants.FarmerConnectConstants;
import com.eka.farmerconnect.constants.TemplateConstants;
import com.eka.farmerconnect.model.BidConversionEntity;
import com.eka.farmerconnect.model.CustomerBid;
import com.eka.farmerconnect.model.LogType;
import com.eka.farmerconnect.model.PublishedBid;
import com.eka.farmerconnect.model.UserRoleInfo;
import com.eka.model.TenantCustomer;
import com.eka.model.User;
import com.eka.service.CommonOperationService;
import com.eka.service.constants.CollectionPlatformConstants;
import com.eka.service.constants.CollectionStatusConstants;
import com.eka.service.constants.SmartAPPPlatformConstants;
import com.eka.service.spring.CommonUtilService;
import com.eka.templates.dataloaders.AppDataLoader;
import com.eka.util.AESEncryptor;
import com.eka.util.ModuleEnum;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;

@Service
public class BidService extends AbstractCommonService{
	
	private static final Logger LOG = ESAPI.getLogger(BidService.class);
	
	private static final Document MONGO_EXISTS_AND_NON_EMPTY_CLAUSE = new Document();
	
	// stores column Name to column Id map
	private static final Map<String, String> COLUMN_ID_MAP = new HashMap<>();
	private static final Map<String, String> MONGO_OPERATOR_MAP = new HashMap<>();
	static {
		COLUMN_ID_MAP.put("cropYear", TemplateConstants.COL_CROPYEAR);
		COLUMN_ID_MAP.put("expiryDate", TemplateConstants.COL_EXPIRY_DATE);
		COLUMN_ID_MAP.put("location", TemplateConstants.COL_LOCATION);
		COLUMN_ID_MAP.put("product", TemplateConstants.COL_PRODUCT);
		COLUMN_ID_MAP.put("quality", TemplateConstants.COL_QUALITY);
		COLUMN_ID_MAP.put("ptaRefNumber", TemplateConstants.COL_PTA_REF_NUMBER);
		COLUMN_ID_MAP.put("publishedPrice", TemplateConstants.COL_PUBLISHED_PRICE);
		COLUMN_ID_MAP.put("priceUnit", TemplateConstants.COL_PRICE_UNIT);
		COLUMN_ID_MAP.put("incoTerm", TemplateConstants.COL_INCO_TERM);
		COLUMN_ID_MAP.put("quantity", TemplateConstants.COL_QUANTITY);
		COLUMN_ID_MAP.put("quantityUnit", TemplateConstants.COL_QUANTITY_UNIT);
		COLUMN_ID_MAP.put("deliveryFromDate", TemplateConstants.COL_DELIVERY_FROM_DATE);
		COLUMN_ID_MAP.put("deliveryToDate", TemplateConstants.COL_DELIVERY_TO_DATE);
		COLUMN_ID_MAP.put("username", TemplateConstants.COL_USERNAME);
		COLUMN_ID_MAP.put("paymentTerms", TemplateConstants.COL_PAYMENT_TERMS);
		COLUMN_ID_MAP.put("packingType", TemplateConstants.COL_PACKING_TYPE);
		COLUMN_ID_MAP.put("packingSize", TemplateConstants.COL_PACKING_SIZE);
		
		MONGO_OPERATOR_MAP.put("lessThan", "lt");
		MONGO_OPERATOR_MAP.put("lessThanOrEqual", "lte");
		MONGO_OPERATOR_MAP.put("greaterThan", "gt");
		MONGO_OPERATOR_MAP.put("greaterThanOrEqual", "gte");
		MONGO_OPERATOR_MAP.put("equal", "eq");
		MONGO_OPERATOR_MAP.put("equals", "eq");
		MONGO_OPERATOR_MAP.put("notEqual", "ne");
		MONGO_OPERATOR_MAP.put("notEquals", "ne");
		
		MONGO_EXISTS_AND_NON_EMPTY_CLAUSE.put(EXISTS_MONGO, true);
		MONGO_EXISTS_AND_NON_EMPTY_CLAUSE.put(NE_MONGO, EMPTY_STR);
	}
	
	@Autowired
	private BasicReadOnlyHibernateDAO basicDAO;
	
	@Autowired
	private PushNotificationService notificationService;
	
	@Autowired
	@Qualifier("CommonUtilService")
	private CommonUtilService commonUtilService;
	
	@Autowired
	private AsyncBlockchainRequest asyncBlockchainRequest;
	
	@Autowired
	private ContractIntegrationService contractIntegrationService;
	
	/**
	 * 
	 * @param sortBy - json object with sorting key and value as ASC or DESC
	 * @param filters - filter criteria as key value pair
	 * @param pagination - json having keys limit per page and page number to fetch
	 * @param tenantId - current tenant Id
	 * @return - List of published bids for that tenant
	 * @throws JSONException 
	 * @throws MongoDBQueryException
	 */
	@ReadOnly
	public List<PublishedBid> getPublishedBids(JSONObject sortBy,
			JSONArray filters, JSONObject pagination, String tenantId, List<String> userRoleNames) throws MongoDBQueryException {
		
		boolean isUsernameMapped = isFieldMappedInDataset(COL_USERNAME, tenantId);
		MongoDBQuery queryObject = new MongoDBQuery();
		queryObject.where(tenantId);
		queryObject.and(ConditionEnum.gte, COL_EXPIRY_DATE, new Date());
		if(isUsernameMapped){
			queryObject.and(createFieldNonEmptyClause(COL_USERNAME));
		}
		if(!CollectionUtils.isEmpty(userRoleNames)) {
			String userRolesRegex = userRoleNames
										.stream()
										.map(String::trim)
										.map(r -> ROLES_PATTERN_BEGIN + r + ROLES_PATTERN_END)
										.collect(Collectors.joining(FarmerConnectConstants.PIPE));
			
			// Final Pattern: (^|,\\s?)roleOne(,\\s?|$)|(^|,\\s?)roleTwo(,\\s?|$)
			Document roleNamesRegexFilter = new Document();
			roleNamesRegexFilter.put(REGEX_MONGO, userRolesRegex);
			roleNamesRegexFilter.put(OPTIONS_MONGO, OPT_CASE_INSENSITIVE);
			Bson noRolesToPublish = ConditionEnum.exists.build(COL_ROLES_TO_PUBLISH, false);
			Bson emptyRolesToPublish = ConditionEnum.eq.build(COL_ROLES_TO_PUBLISH, "");
			Bson inCurrUserRoles = ConditionEnum.eq.build(COL_ROLES_TO_PUBLISH, roleNamesRegexFilter);
			queryObject.and(Filters.or(noRolesToPublish, emptyRolesToPublish, inCurrUserRoles));
		}
		appendSortCriteria(queryObject, sortBy, OrderEnum.DESCENDING, EXPIRY_DATE, true);
		appendFilters(queryObject, filters, true);
		appendPagination(queryObject, pagination);
		
		// Loads data, as objects of PublisedBid class, according to passed query object
		List<PublishedBid> publishedBids = AppDataLoader
												.INSTANCE
												.loadData(FARMER_CONNECT_APP_ID, TEMPLATE_ID, 
														  tenantId, null, 
														  queryObject, PublishedBid.class);

		// Additional handling for user based published prices
		if(isUsernameMapped){
			return populateOfferorInfo(publishedBids, tenantId);
		}
		// Otherwise, append only expiresIn and shipmentDateInMillis(optional)
		return publishedBids.stream().map(bid -> appendDateRelatedFields(bid)).collect(Collectors.toList());
	}
	
	private List<PublishedBid> populateOfferorInfo(List<PublishedBid> publishedBids, String tenantId) throws MongoDBQueryException {
		
		Set<String> usernameSet = getUniqueValueSetFromPublishedBids(tenantId, FarmerConnectConstants.USERNAME);
		Map<String, TenantCustomer> customerMap = getCustomerInfo(usernameSet, Integer.parseInt(tenantId));
		Map<String, User> usersMap = getUserInfo(usernameSet, Integer.parseInt(tenantId));
		Map<String, String> offerorRatingMap = getAverageOfferorRatings(tenantId);
		return 
			publishedBids
				.stream()
				// Validate bids
				.map(bid -> handleInvalidPublishedPrices(customerMap, usersMap, bid))
				// Need to append the offeror info related fields 
				.map(bid -> appendDateAndOfferorFields(usernameSet, customerMap, usersMap, offerorRatingMap, bid))
				.collect(Collectors.toList());
	}

	private PublishedBid handleInvalidPublishedPrices(Map<String, TenantCustomer> customerMap, Map<String, User> usersMap,
			PublishedBid bid) {
		
		if(customerMap.containsKey(bid.getUsername().toLowerCase()) ||
				usersMap.containsKey(bid.getUsername().toLowerCase())) {
			return bid;
		}
		
		bid.setBidId(String.format(INVALID_BID_ID_FORMAT, bid.getBidId()));
		return bid;
	}

	private List<PublishedBid> filterBidsByRoles(List<PublishedBid> publishedBids, List<String> userRoleNames) {
		
		if(CollectionUtils.isEmpty(userRoleNames)) {
			return publishedBids;
		}
		return publishedBids
					.stream()
					.filter(bid -> {
									String publisedRolesStr = bid.getRolesToPublish();
									// If bid is not restricted to a role, it'll be visible to all
									if(null == publisedRolesStr) return true;
									List<String> publishedRoles = Arrays
																	.asList(publisedRolesStr.split(REGEX_COMMA))
																	.stream().map(String::trim).map(String::toLowerCase).collect(Collectors.toList());
									return userRoleNames
												.stream()
												.map(String::trim)
												.map(String::toLowerCase)
												.anyMatch(userRole -> publishedRoles.contains(userRole));
									})
					.collect(Collectors.toList());
	}

	/*
	 *  Appends:
	 *  offerorName
	 *  offerorMobileNo     
	 *  rating              
	 *  deliveryFromDateInMillis
	 *  deliveryToDateInMillis
	 */
	private PublishedBid appendDateAndOfferorFields(Set<String> usernameSet, Map<String, TenantCustomer> customerMap, 
			Map<String, User> usersMap, Map<String, String> offerorRatingMap, PublishedBid bid){
		
		appendDateRelatedFields(bid);
		// Offeror Related Fields:
		String rating = offerorRatingMap.get(bid.getUsername().toLowerCase());
		bid.setRating(StringUtils.isEmpty(rating) ? 
					 RATING_PENDING 			  :
					 rating);
		if(customerMap.containsKey(bid.getUsername().toLowerCase())){
			TenantCustomer customer = customerMap.get(bid.getUsername().toLowerCase());
			bid.setOfferorName(customer.getFirstName() + " " + customer.getLastName());
			bid.setOfferorMobileNo(CryptoUtils.getPIICryptoService().decryptQuietly(customer.getMobile()));
		}else if(usersMap.containsKey(bid.getUsername().toLowerCase())){
			User user = usersMap.get(bid.getUsername().toLowerCase());
			bid.setOfferorName(user.getFirstName() + " " + user.getLastName());
			bid.setOfferorMobileNo(FarmerConnectConstants.NOT_AVAILABLE);
		}else {
			bid.setOfferorName(FarmerConnectConstants.NOT_AVAILABLE);
			bid.setOfferorMobileNo(FarmerConnectConstants.NOT_AVAILABLE);
		}
		return bid;
	}

	private PublishedBid appendDateRelatedFields(PublishedBid bid) {
		
		bid.setDeliveryFromDateInMillis(bid.getDeliveryFromDate().getTime());
		bid.setDeliveryToDateInMillis(bid.getDeliveryToDate().getTime());
		
		long diffInDays = TimeUnit.DAYS.convert(bid.getExpiryDate().getTime() - System.currentTimeMillis(), 
	 											TimeUnit.MILLISECONDS);
		if(diffInDays < 2){
			bid.setExpiresIn(diffInDays == 0 ? TODAY : TOMORROW);
		}else{
			bid.setExpiresIn(String.format(FORMAT_DAYS, diffInDays));
		}
		return bid;
	}
	
	private Map<String, String> getAverageOfferorRatings(String tenantId) throws MongoDBQueryException {
		
		MongoDBAggregationQuery avgRatingQuery = new MongoDBAggregationQuery();
		JSONObject matchQuery = new JSONObject();
		matchQuery.put(CLIENT_ID, tenantId);
		matchQuery.put(STATUS, ACCEPTED);
		matchQuery.put(USERNAME, new JSONObject().put(EXISTS_MONGO, true));
		avgRatingQuery.setMatchInPipeline(matchQuery);
		
		JSONObject groupQuery = new JSONObject();
		groupQuery.put(PRIMARY_KEY, DOLLAR+USERNAME);
		groupQuery.put(RATING, new JSONObject().put(AVG_MONGO, DOLLAR+RATING_INFO+DOT+RATING));
		avgRatingQuery.setGroupByStageInPipeline(groupQuery);
		
		List<JSONObject> avgRatings = MongoQueryFactory.getInstance().aggregate(BIDS_COLLECTION_NAME, avgRatingQuery.getPipeline());
		Map<String, String> offerorRatingMap = new HashMap<>();
		for (JSONObject ratingJSON : avgRatings) {
			String rating = FarmerConnectConstants.RATING_PENDING;
			if(null != ratingJSON.opt(RATING) && JSONObject.NULL != ratingJSON.get(RATING)){
				rating = String.valueOf(Math.round(ratingJSON.getDouble(RATING) * 10.0)/10.0);
			}
			offerorRatingMap.put(ratingJSON.getString(PRIMARY_KEY).toLowerCase(), rating);
		}
		return offerorRatingMap;
	}

	private void appendPagination(MongoDBQuery queryObject, JSONObject pagination) {
		
		if(pagination == null)
			return;
		
		// if pagination is set, use limit and page keys or else return complete data
		if(pagination.has(FarmerConnectConstants.LIMIT) && pagination.has(FarmerConnectConstants.PAGE)){
			int recordsPerPage = pagination.getInt(FarmerConnectConstants.LIMIT);
			queryObject.setLimit(recordsPerPage);
			queryObject.setSkip(recordsPerPage * pagination.getInt(FarmerConnectConstants.PAGE));
		}
	}

	@ReadOnly
	public JSONObject getBidDetailByReferenceId(String bidRefId, int tenantId) throws MongoDBQueryException {
		
		return stitchAdditionalParamsForMobile(getBidDetail(bidRefId, tenantId, new ArrayList<>())); 
	}

	
	// stitches offeror info if applicable and fields required for mobile bid display
	private void stitchOfferorInfoAndFieldsForMobile(List<JSONObject> bids, String tenantId) throws MongoDBQueryException {
		
		if(!isFieldMappedInDataset(COL_USERNAME, tenantId)){
			for (JSONObject bid : bids) {
				stitchAdditionalParamsForMobile(bid);
			}
			return;
		}
		
		Set<String> usernameSet = getUniqueValueSetFromPublishedBids(tenantId, FarmerConnectConstants.USERNAME);
		Map<String, TenantCustomer> customerMap = getCustomerInfo(usernameSet, Integer.parseInt(tenantId));
		Map<String, User> usersMap = getUserInfo(usernameSet, Integer.parseInt(tenantId));
		Map<String, String> offerorRatingMap = getAverageOfferorRatings(tenantId);
		for (JSONObject bid : bids) {
			String username = bid.optString(USERNAME);
			if(StringUtils.isNotEmpty(username)){
				bid.put(RATING, offerorRatingMap.containsKey(username.toLowerCase()) ? offerorRatingMap.get(username.toLowerCase()) : RATING_PENDING);
				bid.put(CURR_BID_RATING, bid.has(RATING_INFO) ? bid.getJSONObject(RATING_INFO).getInt(RATING) : "");
				
				Set<String> userSet = new HashSet<>();
				userSet.add(username);
				if(customerMap.containsKey(username.toLowerCase())){
					TenantCustomer customer = customerMap.get(username.toLowerCase());
					bid.put("offerorName", customer.getFirstName() + " " + customer.getLastName());
					bid.put("offerorMobileNo", CryptoUtils.getPIICryptoService().decryptQuietly(customer.getMobile()));
				}else if(usersMap.containsKey(username.toLowerCase())){
					User user = usersMap.get(username.toLowerCase());
					bid.put("offerorName", user.getFirstName() + " " + user.getLastName());
				}
			}
			stitchAdditionalParamsForMobile(bid);
			
		}
	}

	/*
	 * Stitches following parameters:
	 * 
	 * Latest Bidder Price
	 * Latest Offeror Price
	 * Latest Remarks
	 * Updated By
	 */
	private JSONObject stitchAdditionalParamsForMobile(JSONObject bidDetails) {
		
		JSONArray negLogs = bidDetails.getJSONArray(NEGOTIATION_LOGS);
		iterateNegLogsAndUpdatePrice(negLogs, bidDetails, BIDDER, LATEST_BIDDER_PRICE);
		iterateNegLogsAndUpdatePrice(negLogs, bidDetails, OFFEROR, LATEST_OFFEROR_PRICE);
		bidDetails.put(LATEST_REMARKS, negLogs.getJSONObject(negLogs.length()-1).getString(REMARKS));
		bidDetails.put(UPDATED_BY, negLogs.getJSONObject(negLogs.length()-1).getString(BY));
		return bidDetails;
	}

	private void iterateNegLogsAndUpdatePrice(JSONArray negLogs, JSONObject bidDetails, String updateFor, String keyName) {
		
		bidDetails.put(keyName, "");
		// As first entry in logs is for the published price
		for (int i = negLogs.length()-1; i > 0; i--) {
			JSONObject logEntry = negLogs.getJSONObject(i);
			/*
			 *  Ignore entries having logType field, which is ONLY
			 *  present for Published/Accepted/Rejected Log entries
			 *  And Bidder's Price Could've been entered by Agent
			 */
			if(!logEntry.has(LOG_TYPE) && 
					(BIDDER.equals(updateFor) ? 
					 StringUtils.equalsAny(logEntry.getString(BY), BIDDER, AGENT) : 
					 StringUtils.equals(logEntry.getString(BY), OFFEROR))){
				if(logEntry.has(PRICE))
					bidDetails.put(keyName, logEntry.getDouble(PRICE));
				break;
			}
		}
	}

	@ReadOnly
	public List<JSONObject> getAllMyBids(JSONObject sortBy, JSONArray filters, JSONObject pagination, String tenantId, Integer farmerInternalId) throws MongoDBQueryException {
		
		String customerId = farmerInternalId == null 		 ? 
							AuthContext.getCurrentUserName() : 
							basicDAO.findById(User.class, farmerInternalId).getUsername();
		MongoDBQuery fetchAllBidsQuery = createQueryForCustomerBids(customerId, tenantId);
		
		appendSortCriteria(fetchAllBidsQuery, sortBy, OrderEnum.DESCENDING, UPDATED_DATE, false);
		appendFilters(fetchAllBidsQuery, filters, false);
		appendPagination(fetchAllBidsQuery, pagination);
		
		List<JSONObject> customerBids = MongoQueryFactory.getInstance().find(BIDS_COLLECTION_NAME, fetchAllBidsQuery);
		stitchOfferorInfoAndFieldsForMobile(customerBids, tenantId);
		return customerBids;
	}
	
	private MongoDBQuery createQueryForCustomerBids(String customerId, String tenantId) throws MongoDBQueryException {
		
		MongoDBQuery fetchAllBidsQuery = new MongoDBQuery();
		fetchAllBidsQuery.where(ConditionEnum.eq, CUSTOMER_ID, customerId, tenantId);
		return fetchAllBidsQuery;
	}

	@ReadOnly
	public List<JSONObject> getAllBidsForOfferor(JSONObject sortBy, JSONArray filters, JSONObject pagination, String tenantId, String offerorName) throws MongoDBQueryException {
		
		// Get bids against the logged-in user's uploaded offers, IFF the 'username' field
		// has been mapped in the PriceSheet template
		boolean isUsernameMapped = isFieldMappedInDataset(COL_USERNAME, tenantId);
		MongoDBQuery fetchAllBidsQuery = new MongoDBQuery();
		fetchAllBidsQuery.where(tenantId);
		if(isUsernameMapped){
			fetchAllBidsQuery.and(ConditionEnum.eq, USERNAME, createCaseInsenitiveFilter(offerorName));
		}
		fetchAllBidsQuery.setProjection(ProjectionEnum.EXCLUSION, SHIPMENT_DATE);
		fetchAllBidsQuery.setProjection(ProjectionEnum.EXCLUSION, PRIMARY_KEY);
		
		appendSortCriteria(fetchAllBidsQuery, sortBy, OrderEnum.DESCENDING, UPDATED_DATE, false);
		appendFilters(fetchAllBidsQuery, filters, false);
		appendPagination(fetchAllBidsQuery, pagination);
		
		List<JSONObject> bids = MongoQueryFactory.getInstance().find(BIDS_COLLECTION_NAME, fetchAllBidsQuery);
		
		stitchOfferorInfoAndFieldsForMobile(bids, tenantId);
		Set<String> bidderIds = new HashSet<>();
		for (JSONObject bid : bids) {
			JSONArray negLogs = bid.getJSONArray(NEGOTIATION_LOGS);
			iterateNegLogsAndUpdatePrice(negLogs, bid, BIDDER, LATEST_BIDDER_PRICE);
			iterateNegLogsAndUpdatePrice(negLogs, bid, OFFEROR, LATEST_OFFEROR_PRICE);
			bid.remove(NEGOTIATION_LOGS);
			bidderIds.add(bid.getString(CUSTOMER_ID));
		}
		// TODO:: Once rolesToPublish is moved to app settings, check if it's configured
		return appendApplicableBidderRoles(bids, getUsersRoleInfo(bidderIds, Integer.parseInt(tenantId)));
	}
	
	private Bson createCaseInsenitiveFilter(String fieldValue) {
		
		Document caseInsensitiveCriterion = new Document();
		caseInsensitiveCriterion.put(REGEX_MONGO, CARET + fieldValue + DOLLAR);
		caseInsensitiveCriterion.put(OPTIONS_MONGO, OPT_CASE_INSENSITIVE);
		return caseInsensitiveCriterion;
	}

	/*
	 * Need to append logical AND of rolesToPublish & bidder roles. 
	 */
	private List<JSONObject> appendApplicableBidderRoles(List<JSONObject> bids, Map<String, UserRoleInfo> bidderInfo) {

		for (JSONObject bid : bids) {

			if(null != bid.optString(CUSTOMER_ID) && null != bidderInfo
					.get(bid.optString(CUSTOMER_ID))) {

				bid.put(APPLICABLE_ROLES, 
						getApplicableRoles(bidderInfo
								.get(bid.getString(CUSTOMER_ID))
								.getRoleNames(), 
								bid.optString(ROLES_TO_PUBLISH)));
			}
		}
		return bids;
	}
	
	private JSONObject appendApplicableBidderRoles(JSONObject bid, Map<String, UserRoleInfo> bidderInfo) {
		
		return bid.put(APPLICABLE_ROLES, 
					   getApplicableRoles(bidderInfo.get(bid.getString(CUSTOMER_ID)).getRoleNames(), 
							   			  bid.optString(ROLES_TO_PUBLISH)));
	}

	private String getApplicableRoles(String bidderRolesStr, String publishedRolesStr) {
		
		// Empty rolesToPublish signifies applicable to all
		if(StringUtils.isEmpty(publishedRolesStr)) {
			return bidderRolesStr;
		}
		
		List<String> publishedRoles = Arrays.asList(publishedRolesStr.split(COMMA))
											.stream()
											.map(String::trim)
											.map(String::toLowerCase)
											.collect(Collectors.toList());
		
		return Arrays.stream(bidderRolesStr.split(COMMA))
					 .map(String::trim)
					 .filter(bRole -> publishedRoles.contains(bRole.toLowerCase()))
					 .collect(Collectors.joining(ROLE_SEPARATOR));
	}

	public boolean isFieldMappedInDataset(String fieldKey, String tenantId) {
		
		MongoDBQuery isUsernameMappedQuery = new MongoDBQuery();
		isUsernameMappedQuery.where(createFieldNonEmptyClause(fieldKey), tenantId);
		isUsernameMappedQuery.setLimit(1);
		return !CollectionUtils.isEmpty(AppDataLoader
											.INSTANCE
											.loadData(FarmerConnectConstants.FARMER_CONNECT_APP_ID, 
													 FarmerConnectConstants.TEMPLATE_ID, 
												     tenantId, 
												     null, 
												     isUsernameMappedQuery, 
												     PublishedBid.class));
	}

	private void appendSortCriteria(MongoDBQuery queryObject, JSONObject sortBy, 
			OrderEnum defaultOrder, String sortKey, boolean useColumnIdMap){
		
		if(sortBy == null){
			queryObject.orderBy(defaultOrder, sortKey);
			return;
		}
		
		Iterator<String> iterator = sortBy.keySet().iterator();
		//expecting at most one key in json object 
		if(iterator.hasNext()){
			String key = iterator.next();
			OrderEnum order = FarmerConnectConstants.ASC.equalsIgnoreCase(sortBy.getString(key)) ?
					OrderEnum.ASCENDING : OrderEnum.DESCENDING;
			queryObject.orderBy(order, useColumnIdMap ? COLUMN_ID_MAP.get(key) : key);
		} else
			queryObject.orderBy(defaultOrder, useColumnIdMap ? COLUMN_ID_MAP.get(sortKey) : sortKey);
	}
	
	private void appendFilters(MongoDBQuery queryObject, JSONArray filters, boolean useColumnIdMap) throws MongoDBQueryException{
		
		if(filters == null) 
			return;
		
		for (int i = 0; i < filters.length(); i++) {
			JSONObject filterJSON = filters.getJSONObject(i);
			if(FITER_BASIC.equalsIgnoreCase(filterJSON.getString(TYPE))){
				appendBasicFilter(queryObject, filterJSON, useColumnIdMap);
			}else{
				appendAdvancedFilter(queryObject, filterJSON, useColumnIdMap);
			}
		}
	}

	private void appendAdvancedFilter(MongoDBQuery queryObject, JSONObject filterJSON, boolean useColumnIdMap) throws MongoDBQueryException {
		
		Bson firstFilter = createFilterObject(filterJSON, FIRST_FILTER, useColumnIdMap);
		if(!filterJSON.has(SECOND_FILTER)){
			queryObject.and(firstFilter);
			return;
		}
		
		Bson secondFilter = createFilterObject(filterJSON, SECOND_FILTER, useColumnIdMap);
		queryObject.and("and".equalsIgnoreCase(filterJSON.getString(LOGICAL_OPERATOR)) ? 
						Filters.and(firstFilter, secondFilter):
						Filters.or(firstFilter, secondFilter)); 
	}

	private Bson createFilterObject(JSONObject filterJSON, String filterObjKey, boolean useColumnIdMap) {
		
		JSONObject filterObject = filterJSON.getJSONObject(filterObjKey);
		return 1 == filterJSON.getInt(COLUMN_TYPE) ?
					// Advanced String filters
				    ConditionEnum
				    		.eq
				    		.build(useColumnIdMap ? 
										COLUMN_ID_MAP.get(filterObject.getString(COLUMN_ID)) :
									    filterObject.getString(COLUMN_ID), 
								  getFilterDBObject(filterObject.getString(OPERATOR), 
										  			CommonOperationService
										  				.escapeSpecialCharacters(filterObject.getJSONArray(VALUE).getString(0)), 
										  			useColumnIdMap)) :
					// Advanced Number filters
					ConditionEnum
							.valueOf(MONGO_OPERATOR_MAP.get(filterObject.getString(OPERATOR)))
							.build(useColumnIdMap ? 
										COLUMN_ID_MAP.get(filterObject.getString(COLUMN_ID)) :
										filterObject.getString(COLUMN_ID), 
								  filterObject.getJSONArray(VALUE).get(0));
	}

	private Bson getFilterDBObject(String operator, String pattern, boolean useColumnIdMap) {
		
		BasicDBObject dbObj = new BasicDBObject();
		switch (operator) {
		case "contains":
			dbObj.put(REGEX_MONGO, pattern);
			dbObj.put(OPTIONS_MONGO, "i");
			return dbObj;
		case "notContains":
			dbObj.put(REGEX_MONGO, "^((?!"+pattern+").)*$");
			dbObj.put(OPTIONS_MONGO, "i");
			return dbObj;
		case "startsWith":
			dbObj.put(REGEX_MONGO, "^"+pattern);
			dbObj.put(OPTIONS_MONGO, "i");
			return dbObj;
		case "endsWith":
			dbObj.put(REGEX_MONGO, pattern+"$");
			dbObj.put(OPTIONS_MONGO, "i");
			return dbObj;
		case "equal":
		case "equals":
			dbObj.put(EQ_MONGO, pattern);
			return dbObj;
		case "notEquals":
			dbObj.put(NE_MONGO, pattern);
			return dbObj;
		case "isBlank":
			// Published Bids: useColumnIdMap == true
			// Customer Bids: useColumnIdMap == false
			
			/* Workaround for PublishedBids */
			/* Since if a field is not mapped in the Dataset, the key itself 
			 * is removed from the corresponding Document in CollectionData*/
			dbObj.put(EXISTS_MONGO, !useColumnIdMap);
			
			if(!useColumnIdMap)
				dbObj.put(EQ_MONGO, "");
			
			return dbObj;
		case "isNotBlank":
			dbObj.put(EXISTS_MONGO, true);
			dbObj.put(NE_MONGO, "");
			return dbObj;
		default:
			throw new IllegalArgumentException("Operator not recognized!");
		}
	}

	private void appendBasicFilter(MongoDBQuery queryObject, JSONObject filterJSON, boolean useColumnIdMap) throws MongoDBQueryException {
		
		// String Basic Filters 
		queryObject.and(ConditionEnum.valueOf(filterJSON.getString(OPERATOR)), 
						(useColumnIdMap ? 
								COLUMN_ID_MAP.get(filterJSON.getString(COLUMN_ID)) :
								filterJSON.getString(COLUMN_ID)), 
								commonUtilService.getList(filterJSON.getJSONArray(VALUE)));
	}
	
	public void statusUpdated(String status, String refId,int clientId) {
		if (ACCEPTED.equals(status)) {

			try {
				JSONObject customerBid = this.getBidDetailsFromDB(refId, String.valueOf(clientId), new ArrayList<>());

				ContractDetails newContract = new ContractDetails();
				
				newContract.setPrice("600");
				newContract.setProduct(customerBid.getString("product"));
				newContract.setQuality(customerBid.getString("quality"));
				newContract.setQuantity(String.valueOf(customerBid.getInt("quantity")));
				newContract.setRefId(refId);
				newContract.setBidId(customerBid.getString("bidId"));
				newContract.setCustomerId(customerBid.getString(CUSTOMER_ID));
				newContract.setUserId(customerBid.getString(BIDDER));
				newContract.setClientId(clientId);

				//contractService.createSmartContract(newContract);
				
				asyncBlockchainRequest.createSmartContract(newContract.toString());

			} catch (MongoDBQueryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@ReadOnly
	public JSONObject getBidLogs(String refId, int tenantId) throws MongoDBQueryException{
		
		List<String> inclusionFields = new ArrayList<>();
		inclusionFields.add(NEGOTIATION_LOGS);
		inclusionFields.add(CUSTOMER_ID);
		inclusionFields.add(PRICE_UNIT);
		return getBidDetail(refId, tenantId, inclusionFields);
	}
	
	private JSONObject getBidDetail(String refId, int tenantId, List<String> projectionInclusions) throws MongoDBQueryException{
		
		TenantCustomer customer = null;
		User user = null;
		JSONObject queryResponse = getBidDetailsFromDB(refId, String.valueOf(tenantId), projectionInclusions);
		JSONArray bidLogs = queryResponse.getJSONArray(NEGOTIATION_LOGS);
		
		String customerId = queryResponse.getString(CUSTOMER_ID);
		Set<String> customerIds = new HashSet<>();
		customerIds.add(customerId);
		//fetch farmer information and append it to response
		customer = getCustomerInfo(customerIds, tenantId).get(customerId.toLowerCase());
		if(customer != null){
			queryResponse.put("phoneNumber", CryptoUtils.getPIICryptoService().decryptQuietly(customer.getMobile()));
			queryResponse.put("name", customer.getFirstName() + " " + customer.getLastName());
			queryResponse.put("email", AESEncryptor.decrypt(customer.getEmail()));
		}
		// adding below code as email is coming undefined in UI and customer obj is null.
		else {
			queryResponse.put("email", customerId);
		}
		
		Set<String> traders = new HashSet<>();
		//iterate over bid logs to get all trader ids
		// Avoiding first entry as it's system generated
		for(int i = 1; i < bidLogs.length(); i++){
			JSONObject log = bidLogs.getJSONObject(i);
			if(StringUtils.equalsAny(log.getString(BY), OFFEROR, AGENT)){
				// Adding agentId also along with traderIds to retrieve info from db				
				traders.add(log.getString(USER_ID_CAMEL_CASE));
			}
		}
		
		Map<String, User> tradersMap =  new HashMap<>();
		if(!traders.isEmpty())
			tradersMap = getUserInfo(traders, tenantId);
		
		if(queryResponse.has(AGENT_ID) && queryResponse.get(AGENT_ID) != null){
			User agent = tradersMap.get(queryResponse.getString(AGENT_ID).toLowerCase());
			queryResponse.put("agentName", agent.getFirstName()+" "+agent.getLastName());
			queryResponse.put("agentMailId", agent.getDecryptedEmail());
		}
		
		String name = "Unknown User";
		//append name of all traders
		// Avoiding first entry as it's system generated
		for(int i = 1; i < bidLogs.length(); i++){
			JSONObject log = bidLogs.getJSONObject(i);
			if(StringUtils.equalsAny(log.getString(BY), OFFEROR, AGENT)){
				// if user is trader but somehow its not active or deleted
				if(tradersMap.containsKey(log.getString(USER_ID_CAMEL_CASE).toLowerCase())){
					user = tradersMap.get(log.getString(USER_ID_CAMEL_CASE).toLowerCase());
					name = user.getFirstName() + " " + user.getLastName();
				}
			} else if(customer != null){
				name = customer.getFirstName() + " " + customer.getLastName();
			}
			log.put("name", name);
		}
		
		queryResponse.put(NEGOTIATION_LOGS, bidLogs);
		return appendApplicableBidderRoles(queryResponse, getUsersRoleInfo(customerIds, tenantId));
	}
	
	private JSONObject getBidDetailsFromDB(String refId, String tenantId, 
			List<String> projectionInclusions) throws MongoDBQueryException {
		
		MongoDBQuery queryObject = new MongoDBQuery().where(ConditionEnum.eq, "refId", refId, tenantId);
		//Add all inclusion fields
		for(String inclusionField : projectionInclusions){
			queryObject.setProjection(ProjectionEnum.INCLUSION, inclusionField);
		}
		return MongoQueryFactory.getInstance().
				findOne(BIDS_COLLECTION_NAME, queryObject);
	}

	// Caution: returns toLowerCase() of usernames as keys
	private Map<String, User> getUserInfo(Set<String> userIds, int tenantId){
		Map<String, User> customerMap = new HashMap<>();
		if(CollectionUtils.isEmpty(userIds)){
			return customerMap;
		}
		
		NamedQueryParameters parameters = new NamedQueryParameters("getAllActiveUsersByUserName");
		parameters.addParameter("username", userIds);
		parameters.addParameter("clientId", tenantId);
		List<User> users = basicDAO.findAllByNamedQuery(parameters);
		for(User tc : users){
			customerMap.put(tc.getUsername().toLowerCase(), tc);
		}
		return customerMap;
	}

	private Map<String, UserRoleInfo> getUsersRoleInfo(Set<String> userIds, int tenantId){
		
		if(CollectionUtils.isEmpty(userIds)){
			return Collections.emptyMap();
		}
		
		Map<String, UserRoleInfo> userRoleInfoMap = new HashMap<>();
		NamedQueryParameters parameters = new NamedQueryParameters("findUserAndRoleInfoByUsernames");
		parameters.addParameter("usernames", userIds);
		parameters.addParameter("clientId", tenantId);
		List<Object[]> resultSet = basicDAO.findAllByNamedQuery(parameters);
		for(Object[] row : resultSet){
			userRoleInfoMap.put(String.valueOf(row[4]), 
								new UserRoleInfo((Integer)row[0], (String)row[1], (String)row[2], 
												 (Integer)row[3], (String)row[4], (String)row[5], 
												 (String)row[6], (Character)row[7]));
		}
		return userRoleInfoMap;
	}
	
	// Caution: returns toLowerCase() of usernames as keys
	private Map<String, TenantCustomer> getCustomerInfo(Set<String> customerIdSet, int tenantId){
		
		Map<String, TenantCustomer> customerMap = new HashMap<>();
		try {
			List<String> customerIds = new ArrayList<String>(customerIdSet);
			MongoDBQuery queryObject = new MongoDBQuery();
			queryObject.where(ConditionEnum.in, "username", customerIds, String.valueOf(tenantId));
			queryObject.and(ConditionEnum.eq,SmartAPPPlatformConstants.IS_DELETE, 0);
			List<TenantCustomer> tenantCustomers = MongoQueryFactory.getInstance().find(SmartAPPPlatformConstants.TENANT_CUSTOMER_LIST,
					queryObject, TenantCustomer.class);
			for(TenantCustomer tc : tenantCustomers){
				customerMap.put(tc.getUsername().toLowerCase(), tc);
			}
		} catch (MongoDBQueryException e) {
			LOG.error(Logger.EVENT_FAILURE, e.getMessage(), e);
		}
		return customerMap;
	}


	/**
	 * 
	 * @param inputBid
	 * @param clientId
	 * @param bidPlacerId - Farmer/Agent Username
	 * @param updatedByRoleType - FARMER or AGENT
	 * @param headers 
	 * @param collectionUriBuilder 
	 * @throws MongoDBQueryException
	 */
	@ReadOnly
	@AuditAcitivity(entity = @Entity(name = ModuleEnum.BIDDER, isAuditable = false), action = AuditAction.CREATE)
	public void createBid(CustomerBid inputBid, int clientId, String bidPlacerId, String updatedByRoleType, 
			Integer bidderId, BidConversionEntity bidConversionEntity) throws Exception {
		
		String tenantId = String.valueOf(clientId);
		PublishedBid bidDetails = loadPublishedBidFromDataset(inputBid.getBidId(), tenantId);
		if(bidConversionEntity.getGeneralSettings().isBidQuantityLocked()) {
			Double publishedQuantity = bidDetails.getQuantity();
			if(publishedQuantity <= 0) {
				throw new FunctionalException(FarmerConnectConstants.PUBLISHED_BID_SETUP_ISSUE);
			}
			if(!publishedQuantity.equals(Double.parseDouble(inputBid.getQuantity().replace(",", ".")))) {
				throw new FunctionalException(FarmerConnectConstants.QUANTITY_LOCKED);
			}
		}
		Integer sequenceId = commonOperationService.getNextCreateSequence(BIDS_COLLECTION_NAME);
		String refId = PREFIX_FOR_REF_ID + sequenceId;
		
		setBidFieldValues(inputBid, bidDetails, refId, tenantId, bidPlacerId, updatedByRoleType, bidderId);
		MongoDBQuery queryObject = new MongoDBQuery();
		queryObject.insertToDocument(inputBid.toJSON());
		MongoQueryFactory.getInstance().insertOne(BIDS_COLLECTION_NAME, queryObject);
		produceNotificationIfApplicable(refId, tenantId, inputBid.getStatus(), 1, updatedByRoleType, true);
		if(ACCEPTED.equals(inputBid.getStatus())) {
			pushBidToContractCollectionIfApplicable(inputBid, bidConversionEntity);
		}
		this.statusUpdated(inputBid.getStatus(),refId,clientId);
	}

	private void pushBidToContractCollectionIfApplicable(CustomerBid inputBid,
			BidConversionEntity bidConversionEntity) {
		try {
			if(bidConversionEntity.getGeneralSettings().isContractIntegrationAllowed()) {
				contractIntegrationService
					.pushRecordToBidToContractCollection(bidConversionEntity, 
														 new JSONArray()
														 		.put(inputBid.toContractDataArray()));
			}
		}catch (Exception e) {
			LOG.error(Logger.EVENT_FAILURE, e.getMessage(), e);
		}
	}

	public PublishedBid loadPublishedBidFromDataset(String bidId, String tenantId) throws FunctionalException {
		
		MongoDBQuery fetchBidDetails = new MongoDBQuery();
		fetchBidDetails.where(ConditionEnum.eq, COL_PTA_REF_NUMBER, bidId, tenantId);
		try {
			fetchBidDetails.and(ConditionEnum.gte, COL_EXPIRY_DATE, new Date());
		} catch (MongoDBQueryException e) {
			LOG.error(Logger.EVENT_FAILURE, "Error during fetching bid " + bidId);
		}
		List<PublishedBid> bids = AppDataLoader
										.INSTANCE
										.loadData(FARMER_CONNECT_APP_ID, TEMPLATE_ID,
												  tenantId, null, 
												  fetchBidDetails, PublishedBid.class);
		if(CollectionUtils.isEmpty(bids)) {
			//throw new FunctionalException(FarmerConnectConstants"Invalid or expired Bid ID!");
		}
		return bids.get(0);
	}

	// Fields expected from client
	// Mandatory: bidId, status, shipmentDateInMillis, quantity(One from published bid is only indicative)
	// Optional: remarks
	private void setBidFieldValues(CustomerBid inputBid, PublishedBid bidDetails, String refId, String tenantId, 
			String bidPlacerId, String updatedBy, Integer bidderId) throws MongoDBQueryException {
		
		long updatedDate = System.currentTimeMillis();
		
		inputBid.setPublishedPrice(bidDetails.getPublishedPrice());
		inputBid.setProduct(bidDetails.getProduct());
		inputBid.setLocation(bidDetails.getLocation());
		inputBid.setQuality(bidDetails.getQuality());
		inputBid.setCropYear(bidDetails.getCropYear());
		inputBid.setPriceUnit(bidDetails.getPriceUnit());
		inputBid.setIncoTerm(bidDetails.getIncoTerm());
		inputBid.setRefId(refId);
		inputBid.setClientId(tenantId);
		inputBid.setUpdatedDate(updatedDate);
		inputBid.setBidUpdated(true);
		inputBid.setVersion(1);
		inputBid.setUsername(bidDetails.getUsername());
		inputBid.setRolesToPublish(bidDetails.getRolesToPublish());
		
		if(StringUtils.isEmpty(bidDetails.getOfferType())){
			inputBid.setOfferType(DEFAULT_OFFER_TYPE);
		}else {
			inputBid.setOfferType(bidDetails.getOfferType());
		}
		
		// If quantityUnit is part of the published bid, 
		// it shouldn't be taken from the client  
		if(StringUtils.isNotEmpty(bidDetails.getQuantityUnit())){
			inputBid.setQuantityUnit(bidDetails.getQuantityUnit());
		}
		
		if(AGENT.equals(updatedBy)){
			inputBid.setAgentId(bidPlacerId);
			inputBid.setCustomerId(basicDAO.findById(User.class, bidderId).getUsername());
		}else{
			// Bid By Bidder
			inputBid.setCustomerId(bidPlacerId);
		}
		// As the bidder/agent can only Accept or Counter at the point of creating the bid
		inputBid.setPendingOn(ACCEPTED.equalsIgnoreCase(inputBid.getStatus()) ? PENDING_ON_NONE : OFFEROR);
		inputBid.setNegotiationLogs(constructNegLogs(inputBid, updatedBy, bidPlacerId, updatedDate));
	}

	private JSONArray constructNegLogs(CustomerBid inputBid, String updatedBy, String bidPlacerId, long updatedDate) {
		
		JSONArray negotiationLogs = new JSONArray()
											.put(new JSONObject()
													.put(BY, OFFEROR)
													.put(REMARKS, PUBLISHED_BID_PRICE_REMARKS)
													.put(PRICE, inputBid.getPublishedPrice())
													.put(LOG_TYPE, LogType.PUBLISHED_PRICE.getValue()));
		
		JSONObject currentLogEntry = new JSONObject()
											.put(BY, updatedBy)
											.put(USER_ID_CAMEL_CASE, bidPlacerId)
											.put(REMARKS, StringUtils.isEmpty(inputBid.getRemarks()) ? EMPTY_STR : inputBid.getRemarks().trim())
											.put(DATE, updatedDate);
		
		if(IN_PROGRESS.equalsIgnoreCase(inputBid.getStatus())){
			currentLogEntry.put(PRICE, Double.parseDouble(inputBid.getPrice().replace(",", ".")));
		}else{
			// Accepted case
			currentLogEntry.put(LOG_TYPE, LogType.ACCEPTED.getValue());
		}
		return negotiationLogs.put(currentLogEntry);
	}
	@AuditAcitivity(entity = @Entity(name = ModuleEnum.BIDDER, isAuditable = false), action = AuditAction.UPDATE)
	public UpdateResult updateBid(String refId, String updatedByRoleType, String updatedById, int tenantId, 
			JSONObject bidUpdateParams, BidConversionEntity bidConversionEntity) throws MongoDBQueryException {
		
		long updatedDate = System.currentTimeMillis();
		String tenantIdStr = String.valueOf(tenantId);
		String updatedStatus = bidUpdateParams.getString(STATUS);
		JSONObject negLogEntry = new JSONObject()
										.put(BY, updatedByRoleType)
										.put(USER_ID_CAMEL_CASE, updatedById)
										.put(REMARKS, bidUpdateParams.has(REMARKS) ? bidUpdateParams.getString(REMARKS).trim() : EMPTY_STR)
										.put(DATE, updatedDate);
		
		if(IN_PROGRESS.equalsIgnoreCase(updatedStatus)){
			negLogEntry.put(PRICE, Double.parseDouble(bidUpdateParams.get(PRICE).toString().replace(",", ".")));
		}else{
			negLogEntry.put(LOG_TYPE, ACCEPTED.equalsIgnoreCase(updatedStatus) ? LogType.ACCEPTED.getValue() : LogType.REJECTED.getValue());
		}
		
		MongoDBQuery updateBidQuery = new MongoDBQuery();
		updateBidQuery.where(ConditionEnum.eq, REF_ID, refId, tenantIdStr);
		updateBidQuery.and(ConditionEnum.eq, STATUS, IN_PROGRESS);
		updateBidQuery.and(ConditionEnum.eq, PENDING_ON, AGENT.equals(updatedByRoleType) ? BIDDER : updatedByRoleType);

		// Validation for the user updating: If bid is getting updated by
		if(BIDDER.equals(updatedByRoleType)) {
			// 1. 'BIDDER': Same guy should've created the bid
			updateBidQuery.and(ConditionEnum.eq, CUSTOMER_ID, updatedById);
		}else if(OFFEROR.equals(updatedByRoleType) && isFieldMappedInDataset(COL_USERNAME, tenantIdStr)) {
			// 2. 'OFFEROR' & Published Bids are per user:
			// Same guy who had published the bid
			updateBidQuery.and(ConditionEnum.eq, USERNAME, createCaseInsenitiveFilter(updatedById));
		}
		updateBidQuery.buildUpdateExpression(set, STATUS, updatedStatus);
		updateBidQuery.buildUpdateExpression(set, PENDING_ON, bidUpdateParams.getString(PENDING_ON));
		updateBidQuery.buildUpdateExpression(set, UPDATED_DATE, updatedDate);
		updateBidQuery.buildUpdateExpression(inc, VERSION, 1);
		updateBidQuery.buildUpdateExpression(set, BID_UPDATED, true);
		updateBidQuery.buildUpdateExpression(addToSet, NEGOTIATION_LOGS, negLogEntry);
		if(AGENT.equals(updatedByRoleType)){
			updateBidQuery.buildUpdateExpression(set, AGENT_ID, updatedById);
		}
		UpdateResult updateResult =	MongoQueryFactory.getInstance().updateOne(BIDS_COLLECTION_NAME, updateBidQuery);
		produceNotificationIfApplicable(refId, tenantIdStr, updatedStatus, updateResult.getModifiedCount(), updatedByRoleType, false);
		if(ACCEPTED.equals(updatedStatus)) {
			pushBidToContractCollectionIfApplicable(CustomerBid
														.convertToCustomerBid(
																getBidDetailsFromDB(refId, 
																					tenantIdStr, 
																					Collections.emptyList())), 
														bidConversionEntity);
		}
		return updateResult;
	}

	private void produceNotificationIfApplicable(String refId, String tenantId, String updatedStatus, 
			long updateCount, String updatedByRoleType, boolean isNewBid) throws JSONException, MongoDBQueryException {
		
		if(updateCount > 0) {
			
			JSONObject payload = new JSONObject();
			payload.put(FarmerConnectConstants.TITLE, formatTitle(updatedStatus, isNewBid));
			payload.put(FarmerConnectConstants.BODY, formatBody(refId, updatedStatus, isNewBid));
			payload.put(FarmerConnectConstants.DATA, new JSONObject()
															.put(FarmerConnectConstants.TARGET, FARMER_CONNECT_APP_ID + "/bidDetails/"+refId));
			
			JSONObject bidDetails = getBidDetailsFromDB(refId, tenantId, Arrays.asList(CUSTOMER_ID, USERNAME)); 
			String offerorUsername = bidDetails.optString(USERNAME);
			String bidderUsername = bidDetails.getString(CUSTOMER_ID);
			if(AGENT.equals(updatedByRoleType)) {
				notificationService.produceNotification(bidderUsername, tenantId, payload);
				if(StringUtils.isNotBlank(offerorUsername))
					notificationService.produceNotification(offerorUsername, tenantId, payload);
				return;
			}
			String targetUsername;
			if(OFFEROR.equals(updatedByRoleType)) {
				targetUsername = bidderUsername;
			}else if(BIDDER.equals(updatedByRoleType) && StringUtils.isNotBlank(offerorUsername)){
				targetUsername = offerorUsername;
			}else {
				return;
			}
			if(StringUtils.isNotBlank(targetUsername))
				notificationService.produceNotification(targetUsername, tenantId, payload);
		}
	}

	public List<String> updateBids(List<String> refIds, String currentUserType, String currentUserName, int tenantId,
			JSONObject bidUpdateParams, BidConversionEntity bidConversionEntity) throws MongoDBQueryException {
		
		List<String> unupdatedRefIds = new ArrayList<>();
		for (String refId : refIds) {
			if(updateBid(refId, currentUserType, currentUserName, tenantId, bidUpdateParams, bidConversionEntity).getModifiedCount() == 0){
				unupdatedRefIds.add(refId);
			}
		}
		return unupdatedRefIds;
	}
	
	public List<String> cancelBids(List<String> refIds, Map<String, Object> cancellationInfo, 
			String currentUserName, String tenantId) throws MongoDBQueryException, FunctionalException {
		
		validateQueryForCustomerBidsFromRefIds(currentUserName, tenantId, refIds);
		List<String> unupdatedRefIds = new ArrayList<>();
		for (String refId : refIds) {
			if(cancelBid(refId, cancellationInfo, currentUserName, tenantId).getModifiedCount() == 0){
				unupdatedRefIds.add(refId);
			}
		}
		return unupdatedRefIds;
	}

	public JSONArray getUniqueValueArrayFromPublishedBids(String tenantId, String fieldName) throws MongoDBQueryException {
		
		MongoCursor<String> cursor = fetchUniqueValuesFromAppDataset(tenantId, fieldName);
		JSONArray uniqueValues = new JSONArray();
		while (cursor.hasNext()) {
			uniqueValues.put(cursor.next());
		}
		
		return uniqueValues;
	}
	
	public Set<String> getUniqueValueSetFromPublishedBids(String tenantId, String fieldName) throws MongoDBQueryException {
		
		MongoCursor<String> cursor = fetchUniqueValuesFromAppDataset(tenantId, fieldName);
		Set<String> uniqueValues = new HashSet<>();
		while (cursor.hasNext()) {
			uniqueValues.add(cursor.next());
		}
		return uniqueValues;
	}
	
	private MongoCursor<String> fetchUniqueValuesFromAppDataset(String tenantId, String fieldName) throws MongoDBQueryException {
		
		List<String> collectionIds = new ArrayList<>();
		for (JSONObject collectionJson : getMappedCollectionIds(tenantId, FarmerConnectConstants.TEMPLATE_ID)) {
			collectionIds.add(collectionJson.getString(PRIMARY_KEY));
		}
		MongoDBQuery fetchUniqueValuesQuery = new MongoDBQuery();
		fetchUniqueValuesQuery.where(ConditionEnum.in, CollectionPlatformConstants.COLLECTION_ID, collectionIds, tenantId);
		fetchUniqueValuesQuery.and(ConditionEnum.gte, COL_EXPIRY_DATE, new Date());
		fetchUniqueValuesQuery.setProjection(ProjectionEnum.INCLUSION, COLUMN_ID_MAP.get(fieldName));
		fetchUniqueValuesQuery.setDistinctField(COLUMN_ID_MAP.get(fieldName));
		
		return MongoQueryFactory.getInstance().distinct(SmartAPPPlatformConstants.COLLECTION_DATA, fetchUniqueValuesQuery, String.class);
	}

	
	
	private List<JSONObject> getMappedCollectionIds(String tenantId, String templateId) throws MongoDBQueryException{
		
		List<String> allowedStatuses = new ArrayList<>();
		allowedStatuses.add(CollectionStatusConstants.COMPLETED);
		allowedStatuses.add(CollectionStatusConstants.PARTIAL);
		
		MongoDBQuery query = new MongoDBQuery();
		query.where(ConditionEnum.eq, CollectionPlatformConstants.APP_TEMPLATE_ID, templateId, tenantId);
		query.and(ConditionEnum.eq, CollectionPlatformConstants.COLLECTION_TYPE, CollectionPlatformConstants.DATASET);
		query.and(ConditionEnum.in, CollectionPlatformConstants.STATUS, allowedStatuses);
		
		query.setProjection(ProjectionEnum.INCLUSION, PRIMARY_KEY);
		
		return MongoQueryFactory.getInstance().find(SmartAPPPlatformConstants.COLLECTION_LIST, query);
	}

	@ReadOnly
	public JSONArray getCustomerBidValue(String tenantId, String username, String fieldName, 
			String customerId, Integer farmerId) throws MongoDBQueryException {
		
		MongoDBQuery query = createQueryForCustomerBids(StringUtils.isEmpty(customerId) 						? 
														basicDAO.findById(User.class, farmerId).getUsername()   :
														customerId, tenantId);
		if(USERNAME.equals(fieldName)) {
			query.and(ConditionEnum.ne, fieldName, createCaseInsenitiveFilter(username));
		}
		query.setProjection(ProjectionEnum.INCLUSION, fieldName);
		query.setDistinctField(fieldName);
		MongoCursor<String> cursor = MongoQueryFactory.getInstance().distinct(BIDS_COLLECTION_NAME, query, String.class);
		JSONArray uniqueValues = new JSONArray();
		while (cursor.hasNext()) {
			uniqueValues.put(cursor.next());
		}
		return uniqueValues;
	}

	public UpdateResult rateBid(String refId, JSONObject ratingInfo, String tenantId) throws MongoDBQueryException {
		
		MongoDBQuery updateBidQuery = new MongoDBQuery();
		updateBidQuery.where(ConditionEnum.eq, REF_ID, refId, tenantId);
		updateBidQuery.and(ConditionEnum.eq, STATUS, ACCEPTED);
		Bson ratingNotExists = new Document(RATING_INFO, new Document(EXISTS_MONGO, false));
		updateBidQuery.and(ratingNotExists);
		updateBidQuery.buildUpdateExpression(set, RATING_INFO, ratingInfo);
		updateBidQuery.buildUpdateExpression(inc, VERSION, 1);
		updateBidQuery.buildUpdateExpression(set, UPDATED_DATE, System.currentTimeMillis());
		updateBidQuery.buildUpdateExpression(set, BID_UPDATED, true);
		return MongoQueryFactory.getInstance().updateOne(BIDS_COLLECTION_NAME, updateBidQuery);
	}
	
	private Bson createFieldNonEmptyClause(String fieldKey){
		
		Document fieldNonEmptyClause = new Document();
		fieldNonEmptyClause.put(fieldKey, MONGO_EXISTS_AND_NON_EMPTY_CLAUSE);
		return fieldNonEmptyClause;
	}

	public UpdateResult cancelBid(String refId, Map<String, Object> cancellationInfo, String userName, String tenantId) throws MongoDBQueryException {
		
		long currDate = System.currentTimeMillis();
		JSONObject negLogEntry = new JSONObject()
										.put(BY, OFFEROR)
										.put(USER_ID_CAMEL_CASE, userName)
										.put(REMARKS, cancellationInfo.getOrDefault(REMARKS, EMPTY_STR))
										.put(DATE, currDate)
										.put(LOG_TYPE, LogType.CANCELLED.getValue());
		
		MongoDBQuery updateBidQuery = new MongoDBQuery();
		updateBidQuery.where(ConditionEnum.eq, REF_ID, refId, tenantId);
		updateBidQuery.and(ConditionEnum.eq, STATUS, ACCEPTED);
		updateBidQuery.buildUpdateExpression(set, STATUS, CANCELLED);
		updateBidQuery.buildUpdateExpression(inc, VERSION, 1);
		updateBidQuery.buildUpdateExpression(set, BID_UPDATED, true);
		updateBidQuery.buildUpdateExpression(set, UPDATED_DATE, currDate);
		updateBidQuery.buildUpdateExpression(addToSet, NEGOTIATION_LOGS, negLogEntry);
		UpdateResult updateResult =	MongoQueryFactory.getInstance().updateOne(BIDS_COLLECTION_NAME, updateBidQuery);
		produceNotificationIfApplicable(refId, tenantId, CANCELLED, updateResult.getModifiedCount(), OFFEROR, false);
		return updateResult;
	}
	
	private String formatTitle(String updatedStatus, boolean isNewBid) {
		
		if(isNewBid)
			return "Bid Placed";
			
		return "Bid " + getBidActionPerformed(updatedStatus);
	}

	private String formatBody(String refId, String updatedStatus, boolean isNewBid) {
		
		if(isNewBid)
			return "A new " + refId + " has been placed";
		
		return "The " + refId + " has been " + getBidActionPerformed(updatedStatus);
	}

	private String getBidActionPerformed(String updatedStatus) {
		
		return (IN_PROGRESS.equals(updatedStatus) ? FarmerConnectConstants.COUNTERED: updatedStatus.toLowerCase());
	}
	
	private void validateQueryForCustomerBidsFromRefIds(String currentUserName, String tenantId, List<String> refIds)
			throws MongoDBQueryException, FunctionalException {

		MongoDBQuery fetchAllBidsQuery = new MongoDBQuery();
		fetchAllBidsQuery.where(ConditionEnum.eq, USERNAME, currentUserName, tenantId);
		fetchAllBidsQuery.and(ConditionEnum.in, REF_ID, refIds);
		long countRefId = MongoQueryFactory.getInstance().count(BIDS_COLLECTION_NAME, fetchAllBidsQuery);
		
		if(countRefId != refIds.size())
			throw new FunctionalException("one or more bids doesn't associate with current login user.");
		
	}

}
