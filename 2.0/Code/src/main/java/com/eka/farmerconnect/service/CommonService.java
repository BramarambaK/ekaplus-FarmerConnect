package com.eka.farmerconnect.service;

import static com.eka.farmerconnect.constants.CommonConstants.CLIENTID;
import static com.eka.farmerconnect.constants.CommonConstants.CLIENT_ID;
import static com.eka.farmerconnect.constants.CommonConstants.COUNTER_COLLECTION;
import static com.eka.farmerconnect.constants.CommonConstants.DEVICE_ID;
import static com.eka.farmerconnect.constants.CommonConstants.EMPTY_STR;
import static com.eka.farmerconnect.constants.CommonConstants.MASTER_DATA_CLIENT_ID;
import static com.eka.farmerconnect.constants.CommonConstants.SEQUENCE;
import static com.eka.farmerconnect.constants.CommonConstants.TENANT_INFO;
import static com.eka.farmerconnect.constants.CommonConstants.USER_INFO_FILTER;
import static com.eka.farmerconnect.constants.CommonConstants.USER_INFO_URI;
import static com.eka.farmerconnect.constants.CommonConstants._ID;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.ACCEPTED;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.AGENT;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.AVG_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.BIDDER;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.BIDS_COLLECTION_NAME;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.BY;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.COLUMN_ID;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.COLUMN_TYPE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.CURR_BID_RATING;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.DOLLAR;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.DOT;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.EQ_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.EXISTS_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.FIRST_FILTER;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.FITER_BASIC;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.LATEST_BIDDER_PRICE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.LATEST_OFFEROR_PRICE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.LATEST_REMARKS;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.LOGICAL_OPERATOR;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.LOG_TYPE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.NEGOTIATION_LOGS;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.NE_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OFFEROR;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OFFERS_COLLECTION_NAME;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OPERATOR;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OPTIONS_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.PRICE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.PRIMARY_KEY;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.RATING;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.RATING_INFO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.REGEX_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.REMARKS;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.SECOND_FILTER;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.STATUS;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.TYPE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.UPDATED_BY;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.USERNAME;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.VALUE;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.RATING_PENDING;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.eka.db.mongo.MongoDBAggregationQuery;
import com.eka.db.mongo.MongoDBQuery;
import com.eka.db.mongo.MongoDBQueryException;
import com.eka.db.mongo.MongoQueryFactory;
import com.eka.db.mongo.constants.ConditionEnum;
import com.eka.db.mongo.constants.OrderEnum;
import com.eka.db.mongo.constants.ProjectionEnum;
import com.eka.db.mongo.constants.UpdateOperatorEnum;
import com.eka.farmerconnect.constants.CommonConstants;
import com.eka.farmerconnect.constants.FarmerConnectConstants;
import com.eka.farmerconnect.entity.TenantCustomer;
import com.eka.farmerconnect.entity.User;
import com.eka.farmerconnect.model.UserRoleInfo;
import com.eka.farmerconnect.repository.TenantCustomerRepository;
import com.eka.farmerconnect.repository.UserRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;


@Service
public class CommonService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TenantCustomerRepository customerRepository;
	
	protected static final Document MONGO_EXISTS_AND_NON_EMPTY_CLAUSE = new Document();
	private static final Map<String, String> MONGO_OPERATOR_MAP = new HashMap<>();
	static {
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
	private RestTemplate restTemplate;
	
	@Value("${isDevDeployment:false}")
	private Boolean isDevDeployment;
	
	@Value("${platform.url:}")
	private String platformUrl;
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> authenticateRequestAndGetUserInfo(HttpServletRequest request) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZATION, request.getHeader(AUTHORIZATION));
		headers.add(DEVICE_ID, request.getHeader(DEVICE_ID));
		UriComponentsBuilder uriBuilder = getPlatformUriBuilder(request, USER_INFO_URI)
													.query(USER_INFO_FILTER);
		
		Map<String, Object> userInfo = (Map<String, Object>)restTemplate.exchange(uriBuilder.build().toUri(), 
				 										  HttpMethod.GET, 
				 										  new HttpEntity<>(headers), 
				 										  Object.class).getBody();
		
		userInfo.put(CLIENTID, ((Map<String, Object>)userInfo.get(TENANT_INFO)).get(CLIENTID));
		return userInfo;
	}
	
	public UriComponentsBuilder getPlatformUriBuilder(HttpServletRequest request, String path) {
		
		if(isDevDeployment) {
			if(StringUtils.isEmpty(platformUrl)) {
				throw new RuntimeException("Please ensure that platform url is configured when isDevDeployment is enabled!");
			}
			return UriComponentsBuilder.fromHttpUrl(platformUrl).path(path);
		}
		return UriComponentsBuilder
					.newInstance()
					.scheme(request.getScheme())
					.host(request.getServerName())
					.port(request.getServerPort())
					.path(path);
	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> List<T> getList(JSONArray array) {

		List<T> list = new ArrayList<>();
		for (int i = 0; i < array.length(); i++)
			list.add((T) array.get(i));
		return list;
	}
	
	public static String escapeSpecialCharacters(String inputStr) {
		
		Pattern specialCharactersRegex = Pattern.compile(CommonConstants.SPECIAL_CHARS_REGEX);
	    return specialCharactersRegex.matcher(inputStr).replaceAll(CommonConstants.MATCHED_STRING_REGEX);
	}
	
	public Integer getNextCreateSequence(String collectionEntityName) throws MongoDBQueryException {

		synchronized (CommonService.class) {

			MongoDBQuery queryObject = new MongoDBQuery();
			queryObject.where(ConditionEnum.eq, _ID, collectionEntityName, MASTER_DATA_CLIENT_ID);
			queryObject.buildUpdateExpression(UpdateOperatorEnum.inc, SEQUENCE, 1);
			JSONObject result = MongoQueryFactory.getInstance()
					.findAndUpdate(COUNTER_COLLECTION, queryObject);

			if (result != null)
				return Integer.parseInt(result.get(SEQUENCE).toString());
			else
				throw new MongoDBQueryException(
						"Sequence Error:: Could not Obtain Sequence number for " + collectionEntityName);
		}

	}
	
	protected void appendSortCriteria(MongoDBQuery queryObject, JSONObject sortBy, 
			OrderEnum defaultOrder, String sortKey){
		
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
			queryObject.orderBy(order, key);
		} else
			queryObject.orderBy(defaultOrder, sortKey);
	}
	
	protected void appendFilters(MongoDBQuery queryObject, JSONArray filters) throws MongoDBQueryException{
		
		if(filters == null) 
			return;
		
		for (int i = 0; i < filters.length(); i++) {
			JSONObject filterJSON = filters.getJSONObject(i);
			if(FITER_BASIC.equalsIgnoreCase(filterJSON.getString(TYPE))){
				appendBasicFilter(queryObject, filterJSON);
			}else{
				appendAdvancedFilter(queryObject, filterJSON);
			}
		}
	}
	
	protected void appendPagination(MongoDBQuery queryObject, JSONObject pagination) {
		
		if(pagination == null)
			return;
		
		// if pagination is set, use limit and page keys or else return complete data
		if(pagination.has(FarmerConnectConstants.LIMIT) && pagination.has(FarmerConnectConstants.PAGE)){
			int recordsPerPage = pagination.getInt(FarmerConnectConstants.LIMIT);
			queryObject.setLimit(recordsPerPage);
			queryObject.setSkip(recordsPerPage * pagination.getInt(FarmerConnectConstants.PAGE));
		}
	}

	protected void appendAdvancedFilter(MongoDBQuery queryObject, JSONObject filterJSON) throws MongoDBQueryException {
		
		Bson firstFilter = createFilterObject(filterJSON, FIRST_FILTER);
		if(!filterJSON.has(SECOND_FILTER)){
			queryObject.and(firstFilter);
			return;
		}
		
		Bson secondFilter = createFilterObject(filterJSON, SECOND_FILTER);
		queryObject.and("and".equalsIgnoreCase(filterJSON.getString(LOGICAL_OPERATOR)) ? 
						Filters.and(firstFilter, secondFilter):
						Filters.or(firstFilter, secondFilter)); 
	}

	protected Bson createFilterObject(JSONObject filterJSON, String filterObjKey) {
		
		JSONObject filterObject = filterJSON.getJSONObject(filterObjKey);
		return 1 == filterJSON.getInt(COLUMN_TYPE) ?
					// Advanced String filters
				    ConditionEnum
				    		.eq
				    		.build(filterObject.getString(COLUMN_ID), 
								  getFilterDBObject(filterObject.getString(OPERATOR), 
										  			CommonService
										  				.escapeSpecialCharacters(filterObject.getJSONArray(VALUE).getString(0)))) :
					// Advanced Number filters
					ConditionEnum
							.valueOf(MONGO_OPERATOR_MAP.get(filterObject.getString(OPERATOR)))
							.build(filterObject.getString(COLUMN_ID), 
								  filterObject.getJSONArray(VALUE).get(0));
	}

	protected Bson getFilterDBObject(String operator, String pattern) {
		
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

	protected void appendBasicFilter(MongoDBQuery queryObject, JSONObject filterJSON) throws MongoDBQueryException {
		
		// String Basic Filters 
		queryObject.and(ConditionEnum.valueOf(filterJSON.getString(OPERATOR)), 
						(filterJSON.getString(COLUMN_ID)), 
						 getList(filterJSON.getJSONArray(VALUE)));
	}
	
	public boolean isUserBasedParadigm(String tenantId) {
		
		// TODO:: READ FROM SETTINGS
		return false;
	}
	
	public Set<String> getUniqueValueSetFromOffers(String tenantId, String fieldName) throws MongoDBQueryException {
		
		MongoCursor<String> cursor = fetchUniqueValuesMongoCollection(OFFERS_COLLECTION_NAME, tenantId, fieldName);
		Set<String> uniqueValues = new HashSet<>();
		while (cursor.hasNext()) {
			uniqueValues.add(cursor.next());
		}
		return uniqueValues;
	}
	
	protected MongoCursor<String> fetchUniqueValuesMongoCollection(String collectionName, String tenantId, String fieldName) throws MongoDBQueryException {
		
		MongoDBQuery fetchUniqueValuesQuery = new MongoDBQuery();
		fetchUniqueValuesQuery.where(tenantId);
		if(OFFERS_COLLECTION_NAME.equals(collectionName))
			fetchUniqueValuesQuery.and(ConditionEnum.gte, CommonConstants.EXPIRY_DATE_IN_MILLIS, System.currentTimeMillis());
		
		fetchUniqueValuesQuery.setProjection(ProjectionEnum.INCLUSION, fieldName);
		fetchUniqueValuesQuery.setDistinctField(fieldName);
		return MongoQueryFactory.getInstance().distinct(collectionName, fetchUniqueValuesQuery, String.class);
	}
	
	// stitches offeror info if applicable and fields required for mobile bid display
	protected void stitchOfferorInfoAndFieldsForMobile(List<JSONObject> bids, String tenantId) throws MongoDBQueryException {
		
		if(!isUserBasedParadigm(tenantId)){
			for (JSONObject bid : bids) {
				stitchAdditionalParamsForMobile(bid);
			}
			return;
		}
		
		Set<String> usernameSet = getUniqueValueSetFromOffers(tenantId, FarmerConnectConstants.USERNAME);
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
					bid.put("offerorMobileNo", customer.getMobile());
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
	protected JSONObject stitchAdditionalParamsForMobile(JSONObject bidDetails) {
		
		JSONArray negLogs = bidDetails.getJSONArray(NEGOTIATION_LOGS);
		iterateNegLogsAndUpdatePrice(negLogs, bidDetails, BIDDER, LATEST_BIDDER_PRICE);
		iterateNegLogsAndUpdatePrice(negLogs, bidDetails, OFFEROR, LATEST_OFFEROR_PRICE);
		bidDetails.put(LATEST_REMARKS, negLogs.getJSONObject(negLogs.length()-1).getString(REMARKS));
		bidDetails.put(UPDATED_BY, negLogs.getJSONObject(negLogs.length()-1).getString(BY));
		return bidDetails;
	}

	protected void iterateNegLogsAndUpdatePrice(JSONArray negLogs, JSONObject bidDetails, String updateFor, String keyName) {
		
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
	
	// Caution: returns toLowerCase() of usernames as keys
	protected Map<String, User> getUserInfo(Set<String> userIds, int tenantId){
		Map<String, User> userMap = new HashMap<>();
		if(CollectionUtils.isEmpty(userIds)){
			return userMap;
		}
		
		List<User> users = userRepository.findActiveUsersByUsername(userIds, tenantId);
		for(User user : users){
			userMap.put(user.getUsername().toLowerCase(), user);
		}
		return userMap;
	}

	
	public Integer getTenantId(Integer userId){
		return userRepository.findTenantIdByUserId(userId);
	}
	protected Map<String, UserRoleInfo> getUsersRoleInfo(Set<String> userIds, int tenantId){
		
		if(CollectionUtils.isEmpty(userIds)){
			return Collections.emptyMap();
		}
		
		Map<String, UserRoleInfo> userRoleInfoMap = new HashMap<>();
		List<Object[]> resultSet = userRepository.findUserAndRoleInfo(userIds, tenantId);
		for(Object[] row : resultSet){
			userRoleInfoMap.put(String.valueOf(row[4]), 
								new UserRoleInfo((Integer)row[0], (String)row[1], (String)row[2], 
												 (Integer)row[3], (String)row[4], (String)row[5], 
												 (String)row[6], (Character)row[7]));
		}
		return userRoleInfoMap;
	}
	
	// Caution: returns toLowerCase() of usernames as keys
	protected Map<String, TenantCustomer> getCustomerInfo(Set<String> customerIds, int tenantId){
		Map<String, TenantCustomer> customerMap = new HashMap<>();
		List<TenantCustomer> tenantCustomers = customerRepository.findActiveCustomersByUsername(customerIds, tenantId);
		for(TenantCustomer tc : tenantCustomers){
			customerMap.put(tc.getUsername().toLowerCase(), tc);
		}
		return customerMap;
	}
	
	protected Map<String, String> getAverageOfferorRatings(String tenantId) throws MongoDBQueryException {
		
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
}
