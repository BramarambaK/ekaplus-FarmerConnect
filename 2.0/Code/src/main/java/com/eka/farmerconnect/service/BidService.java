package com.eka.farmerconnect.service;

import static com.eka.db.mongo.constants.UpdateOperatorEnum.addToSet;
import static com.eka.db.mongo.constants.UpdateOperatorEnum.inc;
import static com.eka.db.mongo.constants.UpdateOperatorEnum.set;
import static com.eka.farmerconnect.constants.CommonConstants.EMPTY_STR;
import static com.eka.farmerconnect.constants.CommonConstants.USER_ID_CAMEL_CASE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.ACCEPTED;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.AGENT;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.AGENT_ID;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.APPLICABLE_ROLES;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.BIDDER;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.BIDS_COLLECTION_NAME;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.BID_UPDATED;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.BY;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.CANCELLED;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.CARET;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.COMMA;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.CUSTOMER_ID;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.DATE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.DOLLAR;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.EXISTS_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.IN_PROGRESS;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.LATEST_BIDDER_PRICE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.LATEST_OFFEROR_PRICE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.LOG_TYPE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.NEGOTIATION_LOGS;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OFFEROR;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OFFERS_COLLECTION_NAME;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OPTIONS_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OPT_CASE_INSENSITIVE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.PENDING_ON;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.PENDING_ON_NONE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.PRICE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.PRICE_UNIT;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.PRIMARY_KEY;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.PUBLISHED_BID_PRICE_REMARKS;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.RATING_INFO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.REF_ID;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.REGEX_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.REMARKS;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.ROLES_TO_PUBLISH;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.ROLE_SEPARATOR;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.SHIPMENT_DATE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.STATUS;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.UPDATED_DATE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.USERNAME;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.VERSION;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.FARMER_CONNECT_APP_ID;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.PREFIX_FOR_REF_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eka.blockchain.contract.model.ContractDetails;
import com.eka.core.audit.AuditAction;
import com.eka.core.audit.annotation.AuditAcitivity;
import com.eka.core.audit.annotation.Entity;
import com.eka.db.hibernate.ReadOnly;
import com.eka.db.mongo.MongoDBQuery;
import com.eka.db.mongo.MongoDBQueryException;
import com.eka.db.mongo.MongoQueryFactory;
import com.eka.db.mongo.constants.ConditionEnum;
import com.eka.db.mongo.constants.OrderEnum;
import com.eka.db.mongo.constants.ProjectionEnum;
import com.eka.farmerconnect.constants.FarmerConnectConstants;
import com.eka.farmerconnect.entity.TenantCustomer;
import com.eka.farmerconnect.entity.User;
import com.eka.farmerconnect.exception.FunctionalException;
import com.eka.farmerconnect.model.AuthContext;
import com.eka.farmerconnect.model.BidConversionEntity;
import com.eka.farmerconnect.model.CustomerBid;
import com.eka.farmerconnect.model.LogType;
import com.eka.farmerconnect.model.UserRoleInfo;
import com.eka.farmerconnect.repository.UserRepository;
import com.eka.util.AESEncryptor;
import com.eka.util.ModuleEnum;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;

@Service
public class BidService extends CommonService{
	
	private static final Logger logger = ESAPI.getLogger(BidService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PushNotificationService notificationService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private OfferService offerService;
	
	@Autowired
	private AsyncBlockchainRequest asyncBlockchainRequest;
	
	@Autowired
	private ContractIntegrationService contractIntegrationService;
	
	@Value("${default_offer_type}")
	private String defaultOfferType;

	@ReadOnly
	public JSONObject getBidDetailByReferenceId(String bidRefId, int tenantId) throws MongoDBQueryException {
		
		return stitchAdditionalParamsForMobile(getBidDetail(bidRefId, tenantId, new ArrayList<>())); 
	}

	@ReadOnly
	public List<JSONObject> getAllMyBids(JSONObject sortBy, JSONArray filters, JSONObject pagination, 
			String tenantId, Integer farmerInternalId) throws MongoDBQueryException {
		
		String customerId = farmerInternalId == null 		 ?
							AuthContext.getCurrentUsername() :
							userRepository.findById(farmerInternalId).get().getUsername();
		MongoDBQuery fetchAllBidsQuery = createQueryForCustomerBids(customerId, tenantId);
		
		appendSortCriteria(fetchAllBidsQuery, sortBy, OrderEnum.DESCENDING, UPDATED_DATE);
		appendFilters(fetchAllBidsQuery, filters);
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
	public List<JSONObject> getAllBidsForOfferor(JSONObject sortBy, JSONArray filters, JSONObject pagination, 
			String tenantId, String offerorName) throws MongoDBQueryException {
		
		// Get bids against the logged-in user's uploaded offers, IFF the 'username' field
		// has been mapped in the PriceSheet template
		boolean isUsernameMapped = isUserBasedParadigm(tenantId);
		MongoDBQuery fetchAllBidsQuery = new MongoDBQuery();
		fetchAllBidsQuery.where(tenantId);
		if(isUsernameMapped){
			fetchAllBidsQuery.and(ConditionEnum.eq, USERNAME, createCaseInsenitiveFilter(offerorName));
		}
		fetchAllBidsQuery.setProjection(ProjectionEnum.EXCLUSION, SHIPMENT_DATE);
		fetchAllBidsQuery.setProjection(ProjectionEnum.EXCLUSION, PRIMARY_KEY);
		
		appendSortCriteria(fetchAllBidsQuery, sortBy, OrderEnum.DESCENDING, UPDATED_DATE);
		appendFilters(fetchAllBidsQuery, filters);
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
			bid.put(APPLICABLE_ROLES, 
					getApplicableRoles(bidderInfo
											.get(bid.getString(CUSTOMER_ID))
											.getRoleNames(), 
									   bid.optString(ROLES_TO_PUBLISH)));
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
				newContract.setBidId(customerBid.getString("offerId"));
				newContract.setCustomerId(customerBid.getString(CUSTOMER_ID));
				newContract.setUserId(customerBid.getString("username"));
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
			queryResponse.put("phoneNumber", customer.getMobile());
			queryResponse.put("name", customer.getFirstName() + " " + customer.getLastName());
			queryResponse.put("email", AESEncryptor.decrypt(customer.getEmail()));
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
			queryResponse.put("agentMailId", AESEncryptor.decrypt(agent.getEmail()));
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
		JSONObject bidDetails = offerService.getOfferById(inputBid.getOfferId(), tenantId);
		if(bidConversionEntity.getGeneralSettings().isBidQuantityLocked()) {
			Double publishedQuantity = bidDetails.optDouble("quantity");
			if(publishedQuantity <= 0) {
				throw new FunctionalException(FarmerConnectConstants.PUBLISHED_BID_SETUP_ISSUE);
			}
			if(!publishedQuantity.equals(Double.parseDouble(inputBid.getQuantity().replace(",", ".")))) {
				throw new FunctionalException(FarmerConnectConstants.QUANTITY_LOCKED);
			}
		}
		Integer sequenceId = commonService.getNextCreateSequence(BIDS_COLLECTION_NAME);
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
			logger.error(Logger.EVENT_FAILURE, e.getMessage(), e);
		}
	}

	// Fields expected from client
	// Mandatory: bidId, status, shipmentDateInMillis, quantity(One from published bid is only indicative)
	// Optional: remarks
	private void setBidFieldValues(CustomerBid inputBid, JSONObject bidDetails, String refId, String tenantId, 
			String bidPlacerId, String updatedBy, Integer bidderId) throws MongoDBQueryException {
		
		long updatedDate = System.currentTimeMillis();
		
		inputBid.setPublishedPrice(bidDetails.optDouble("publishedPrice"));
		inputBid.setProduct(bidDetails.optString("product"));
		inputBid.setLocation(bidDetails.optString("location"));
		inputBid.setQuality(bidDetails.optString("quality"));
		inputBid.setCropYear(bidDetails.optString("cropYear"));
		inputBid.setPriceUnit(bidDetails.optString("priceUnit"));
		inputBid.setIncoTerm(bidDetails.optString("incoTerm"));
		inputBid.setRefId(refId);
		inputBid.setClientId(tenantId);
		inputBid.setUpdatedDate(updatedDate);
		inputBid.setBidUpdated(true);
		inputBid.setVersion(1);
		inputBid.setUsername(bidDetails.optString("username"));
		inputBid.setRolesToPublish(bidDetails.optString("rolesToPublish"));
		
		if(StringUtils.isEmpty(bidDetails.optString("offerType"))){
			inputBid.setOfferType(defaultOfferType);
		}else {
			inputBid.setOfferType(bidDetails.optString("offerType"));
		}
		
		// If quantityUnit is part of the published bid, 
		// it shouldn't be taken from the client  
		if(StringUtils.isNotEmpty(bidDetails.optString("quantityUnit"))){
			inputBid.setQuantityUnit(bidDetails.optString("quantityUnit"));
		}
		
		if(AGENT.equals(updatedBy)){
			inputBid.setAgentId(bidPlacerId);
			inputBid.setCustomerId(userRepository.findById(bidderId).get().getUsername());
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
		}else if(OFFEROR.equals(updatedByRoleType) && isUserBasedParadigm(tenantIdStr)) {
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
		if(ACCEPTED.equals(updatedStatus) && updateResult.getModifiedCount() > 0) {
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
			String currentUserName, String tenantId) throws MongoDBQueryException {
		
		List<String> unupdatedRefIds = new ArrayList<>();
		for (String refId : refIds) {
			if(cancelBid(refId, cancellationInfo, currentUserName, tenantId).getModifiedCount() == 0){
				unupdatedRefIds.add(refId);
			}
		}
		return unupdatedRefIds;
	}

	public JSONArray getUniqueValueArrayFromPublishedBids(String tenantId, String fieldName) throws MongoDBQueryException {
		
		MongoCursor<String> cursor = fetchUniqueValuesMongoCollection(OFFERS_COLLECTION_NAME, tenantId, fieldName);
		JSONArray uniqueValues = new JSONArray();
		while (cursor.hasNext()) {
			uniqueValues.put(cursor.next());
		}
		
		return uniqueValues;
	}
	
	@ReadOnly
	public JSONArray getCustomerBidValue(String tenantId, String username, String fieldName, 
			String customerId, Integer farmerId) throws MongoDBQueryException {
		
		MongoDBQuery query = createQueryForCustomerBids(StringUtils.isEmpty(customerId) 						? 
														userRepository.findById(farmerId).get().getUsername()	:
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

}
