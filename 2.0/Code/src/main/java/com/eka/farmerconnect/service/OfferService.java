package com.eka.farmerconnect.service;

import static com.eka.db.mongo.constants.UpdateOperatorEnum.inc;
import static com.eka.db.mongo.constants.UpdateOperatorEnum.set;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.COLL_NAME;
import static com.eka.farmerconnect.constants.CommonConstants.CLIENT_ID;
import static com.eka.farmerconnect.constants.CommonConstants.EXPIRY_DATE_IN_MILLIS;
import static com.eka.farmerconnect.constants.CommonConstants.IS_DELETED;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OFFERS_COLLECTION_NAME;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OFFER_ID;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OPTIONS_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.OPT_CASE_INSENSITIVE;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.REGEX_MONGO;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.ROLES_TO_PUBLISH;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.USERNAME;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.VERSION;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.COLLECTION_NOT_CONFIGURED;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.EXPIRY_DATE;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.FORMAT_DAYS;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.INVALID_BID_ID_FORMAT;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.PREFIX_FOR_OFFER_ID;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.RATING_PENDING;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.REGEX_COMMA;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.ROLES_PATTERN_BEGIN;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.ROLES_PATTERN_END;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.TODAY;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.TOMORROW;
import static com.eka.farmerconnect.controller.FCBaseController.getRequestHeaders;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.eka.core.audit.AuditAction;
import com.eka.core.audit.annotation.AuditAcitivity;
import com.eka.core.audit.annotation.Entity;
import com.eka.db.mongo.MongoDBQuery;
import com.eka.db.mongo.MongoDBQueryException;
import com.eka.db.mongo.MongoQueryFactory;
import com.eka.db.mongo.constants.ConditionEnum;
import com.eka.db.mongo.constants.OrderEnum;
import com.eka.farmerconnect.constants.CollectionAPIConstants;
import com.eka.farmerconnect.constants.CommonConstants;
import com.eka.farmerconnect.constants.CustomerBidOfferConstants;
import com.eka.farmerconnect.constants.FarmerConnectConstants;
import com.eka.farmerconnect.entity.TenantCustomer;
import com.eka.farmerconnect.entity.User;
import com.eka.farmerconnect.exception.FunctionalException;
import com.eka.farmerconnect.model.AuthContext;
import com.eka.farmerconnect.model.GeneralSettings;
import com.eka.farmerconnect.model.Offer;
import com.eka.farmerconnect.model.OfferField;
import com.eka.farmerconnect.model.UserRoleInfo;
import com.eka.farmerconnect.util.HttpClientUtil;
import com.eka.util.ModuleEnum;
import com.mongodb.client.model.Filters;

import reactor.core.publisher.Mono;

@Service
public class OfferService extends CommonService{

	@Value("#{${offer_field_value_coll_names}}")
	private Map<String, String> offerFieldValueCollNames;
	
	@Autowired
	private HttpClientUtil httpClient;
	
	@Autowired
	private CommonService commonService;
	
	private WebClient webClient;
	
	private static Map<String, Object> collectionUnconfigured = new HashMap<>();
	private static List<String> updateFields = Arrays.asList("quantity", "quantityUnit", "publishedPrice", "packingSize", "packingType", "paymentTerms", "cropYear", "priceUnit", "incoTerm", "quality", "location", "expiryDateInMillis", "deliveryFromDateInMillis", "deliveryToDateInMillis");
	
	@PostConstruct
	private void constructCollectionUploadPayload() {
		
		// Web Client Initialization
		webClient = WebClient
						.builder()
						.clientConnector(new JettyClientHttpConnector())
						.build();
		
		collectionUnconfigured.put(FarmerConnectConstants.DATA, new ArrayList<>());
		collectionUnconfigured.put(FarmerConnectConstants.MSG, COLLECTION_NOT_CONFIGURED);
	}
	
	public List<Object> getMultipleFieldValues(List<OfferField> offerFields) {
		
		UriComponentsBuilder uriComponentsBuilder = commonService.getPlatformUriBuilder(AuthContext.getCurrentHttpRequest(), CollectionAPIConstants.COLLECTION_URI);
		List<Object> responses = new ArrayList<>();
		List<Mono<Object>> monos 
					= offerFields
						.stream()
						.map(field -> webClient
										.get()
										.uri(uriComponentsBuilder
														.replaceQueryParam(COLL_NAME, 
																			offerFieldValueCollNames.get(field.getId()))
														.build()
														.toUri())
										.headers(existingHeaders -> {
													existingHeaders.addAll(getRequestHeaders(AuthContext.getCurrentHttpRequest()));
										})
										.retrieve()
										.bodyToMono(Object.class)
										.onErrorReturn(collectionUnconfigured))
						.collect(Collectors.toList());
		Mono.zip(monos, mergedResponses -> responses.addAll(Arrays.asList(mergedResponses))).block();
		return responses;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getFieldValues(OfferField offerField) {
		
		URI collectionURI = commonService.getPlatformUriBuilder(AuthContext.getCurrentHttpRequest(), "")
								.queryParam(COLL_NAME,
											offerFieldValueCollNames.get(offerField.getId()))
								.build()
								.toUri();
		
		return httpClient.fireHttpRequest(collectionURI,
							  HttpMethod.GET,
							  null,
							  getRequestHeaders(AuthContext.getCurrentHttpRequest()), 
							  Map.class).getBody();
	}

	private JSONObject validateAuthorizationAndGetOfferDetails(String offerId, String currentUserName, 
			String tenantId) throws FunctionalException{
		
		try {
			JSONObject offer = getOfferById(offerId, tenantId);
			if(!currentUserName.equalsIgnoreCase(offer.optString(CommonConstants.USERNAME))) {
				throw new FunctionalException(FarmerConnectConstants.INVALID_BID_ID_MSG);
			}
			return offer;
		} catch (MongoDBQueryException e) {
			throw new FunctionalException(FarmerConnectConstants.INVALID_BID_ID_MSG);
		}
	}

	@AuditAcitivity(entity = @Entity(name = ModuleEnum.OFFER, isAuditable = false), action = AuditAction.UPDATE)
	public boolean updateOffer(String offerId, Offer updatedOffer, String currentUserName, String tenantId) throws FunctionalException, MongoDBQueryException {
		
		MongoDBQuery updateOfferQuery = new MongoDBQuery();
		updateOfferQuery.where(ConditionEnum.eq, OFFER_ID, offerId, tenantId);
		validateAuthorizationAndGetOfferDetails(offerId, currentUserName, tenantId);
		setUpdateFieldExpressions(updateOfferQuery, updatedOffer);
		return MongoQueryFactory.getInstance().updateOne(OFFERS_COLLECTION_NAME, updateOfferQuery).getModifiedCount() > 0;
	}

	private void setUpdateFieldExpressions(MongoDBQuery updateOfferQuery, Offer updatedOffer) {
		
		JSONObject updatedFields = updatedOffer.toJSON();
		Iterator<String> itr = updatedFields.keys(); 
		while(itr.hasNext()) {
			String field = itr.next();
			if(updateFields.contains(field))
				updateOfferQuery.buildUpdateExpression(set, field, updatedFields.get(field));
		}
		updateOfferQuery.buildUpdateExpression(inc, VERSION, 1);
	}

	@AuditAcitivity(entity = @Entity(name = ModuleEnum.OFFER, isAuditable = false), action = AuditAction.CREATE)
	public String publishOffer(Offer offerDetails, String currentUserName, GeneralSettings appSettings) throws MongoDBQueryException {
		
		Integer sequenceId = commonService.getNextCreateSequence(OFFERS_COLLECTION_NAME);
		String offerId = PREFIX_FOR_OFFER_ID + sequenceId;
		offerDetails.setOfferId(offerId);
		offerDetails.setRolesToPublish(appSettings.getRolesToPublish());
		MongoDBQuery queryObject = new MongoDBQuery();
		JSONObject offerJson = offerDetails.toJSON(currentUserName);
		offerJson.put(IS_DELETED, false);
		offerJson.put(VERSION, 1);
		offerJson.put(CLIENT_ID, AuthContext.getCurrentTenantId());
		queryObject.insertToDocument(offerJson);
		MongoQueryFactory.getInstance().insertOne(OFFERS_COLLECTION_NAME, queryObject);
		return offerId;
	}
	
	@AuditAcitivity(entity = @Entity(name = ModuleEnum.OFFER, isAuditable = false), action = AuditAction.DELETE)
	public boolean deleteOffer(String offerId, String currentUserName, String tenantId) throws FunctionalException, MongoDBQueryException {
		
		JSONObject existingOffer = validateAuthorizationAndGetOfferDetails(offerId, currentUserName, tenantId);
		if(System.currentTimeMillis() >= existingOffer.getLong(EXPIRY_DATE_IN_MILLIS)) {
			// Already expired offer
			return false; 
		}
		MongoDBQuery deleteOfferQuery = new MongoDBQuery();
		deleteOfferQuery.where(ConditionEnum.eq, OFFER_ID, offerId, tenantId);
		deleteOfferQuery.buildUpdateExpression(set, CommonConstants.IS_DELETED, true);
		deleteOfferQuery.buildUpdateExpression(inc, VERSION, 1);
		return MongoQueryFactory.getInstance().updateOne(OFFERS_COLLECTION_NAME, deleteOfferQuery).getModifiedCount() > 0;
	}
	
	public JSONObject getOfferById(String offerId, String tenantId) throws FunctionalException, MongoDBQueryException {
		
		MongoDBQuery fetchBidDetails = new MongoDBQuery();
		fetchBidDetails.where(ConditionEnum.eq, OFFER_ID, offerId, tenantId);
		fetchBidDetails.and(ConditionEnum.eq, IS_DELETED, false);
		fetchBidDetails.and(ConditionEnum.gt, EXPIRY_DATE_IN_MILLIS, System.currentTimeMillis());
		JSONObject offer = MongoQueryFactory.getInstance().findOne(OFFERS_COLLECTION_NAME, fetchBidDetails);
		if(null == offer) {
			throw new FunctionalException(FarmerConnectConstants.INVALID_BID_ID_MSG);
		}
		return offer;
	}
	
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
	public List<Offer> getOffers(JSONObject sortBy, JSONArray filters, JSONObject pagination, 
			String tenantId, GeneralSettings generalSettings) throws MongoDBQueryException {
		
		boolean isUsernameMapped = isUserBasedParadigm(tenantId);
		MongoDBQuery queryObject = new MongoDBQuery();
		queryObject.where(tenantId);
		queryObject.and(ConditionEnum.eq, CommonConstants.IS_DELETED, false);
		queryObject.and(ConditionEnum.gte, CommonConstants.EXPIRY_DATE_IN_MILLIS, System.currentTimeMillis());
		if(isUsernameMapped){
			queryObject.and(createFieldNonEmptyClause(USERNAME));
		}
		if(!StringUtils.isEmpty(generalSettings.getRolesToPublish().trim())) {
			Set<String> currUserSet = new HashSet<>();
			currUserSet.add(AuthContext.getCurrentUsername());
			UserRoleInfo roleInfo = commonService.getUsersRoleInfo(currUserSet, Integer.parseInt(tenantId)).get(AuthContext.getCurrentUsername());
			List<String> userRoleNames = Arrays.asList(roleInfo.getRoleNames().split(","));
			String userRolesRegex = userRoleNames
										.stream()
										.map(String::trim)
										.map(r -> ROLES_PATTERN_BEGIN + r + ROLES_PATTERN_END)
										.collect(Collectors.joining(FarmerConnectConstants.PIPE));
			
			// Final Pattern: (^|,\\s?)roleOne(,\\s?|$)|(^|,\\s?)roleTwo(,\\s?|$)
			Document roleNamesRegexFilter = new Document();
			roleNamesRegexFilter.put(REGEX_MONGO, userRolesRegex);
			roleNamesRegexFilter.put(OPTIONS_MONGO, OPT_CASE_INSENSITIVE);
			Bson noRolesToPublish = ConditionEnum.exists.build(ROLES_TO_PUBLISH, false);
			Bson emptyRolesToPublish = ConditionEnum.eq.build(ROLES_TO_PUBLISH, "");
			Bson inCurrUserRoles = ConditionEnum.eq.build(ROLES_TO_PUBLISH, roleNamesRegexFilter);
			queryObject.and(Filters.or(noRolesToPublish, emptyRolesToPublish, inCurrUserRoles));
		}
		appendSortCriteria(queryObject, sortBy, OrderEnum.DESCENDING, EXPIRY_DATE);
		appendFilters(queryObject, filters);
		appendPagination(queryObject, pagination);
		
		// Loads data, as objects of PublisedBid class, according to passed query object
		List<Offer> offers = MongoQueryFactory.getInstance().find(CustomerBidOfferConstants.OFFERS_COLLECTION_NAME, queryObject, Offer.class);  

		// Additional handling for user based published prices
		if(isUsernameMapped){
			return populateOfferorInfo(offers, tenantId);
		}
		// Otherwise, append only expiresIn and shipmentDateInMillis(optional)
		return offers.stream().map(offer -> appendExpirationInfo(offer)).collect(Collectors.toList());
	}
	
	private List<Offer> populateOfferorInfo(List<Offer> offers, String tenantId) throws MongoDBQueryException {
		
		Set<String> usernameSet = getUniqueValueSetFromOffers(tenantId, USERNAME);
		Map<String, TenantCustomer> customerMap = getCustomerInfo(usernameSet, Integer.parseInt(tenantId));
		Map<String, User> usersMap = getUserInfo(usernameSet, Integer.parseInt(tenantId));
		Map<String, String> offerorRatingMap = getAverageOfferorRatings(tenantId);
		return 
			offers
				.stream()
				// Validate bids
				.map(offer -> handleInvalidPublishedPrices(customerMap, usersMap, offer))
				// Need to append the offeror info related fields 
				.map(updatedOffer -> appendDateAndOfferorFields(usernameSet, customerMap, usersMap, offerorRatingMap, updatedOffer))
				.collect(Collectors.toList());
	}
	
	private Offer handleInvalidPublishedPrices(Map<String, TenantCustomer> customerMap, Map<String, User> usersMap,
			Offer offer) {
		
		if(customerMap.containsKey(offer.getUsername().toLowerCase()) ||
				usersMap.containsKey(offer.getUsername().toLowerCase())) {
			return offer;
		}
		
		offer.setOfferId(String.format(INVALID_BID_ID_FORMAT, offer.getOfferId()));
		return offer;
	}

	private List<Offer> filterBidsByRoles(List<Offer> publishedBids, List<String> userRoleNames) {
		
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
	private Offer appendDateAndOfferorFields(Set<String> usernameSet, Map<String, TenantCustomer> customerMap, 
			Map<String, User> usersMap, Map<String, String> offerorRatingMap, Offer offer){
		
		appendExpirationInfo(offer);
		// Offeror Related Fields:
		String rating = offerorRatingMap.get(offer.getUsername().toLowerCase());
		offer.setRating(StringUtils.isEmpty(rating) ? 
					 RATING_PENDING 			  :
					 rating);
		if(customerMap.containsKey(offer.getUsername().toLowerCase())){
			TenantCustomer customer = customerMap.get(offer.getUsername().toLowerCase());
			offer.setOfferorName(customer.getFirstName() + " " + customer.getLastName());
			offer.setOfferorMobileNo(customer.getMobile());
		}else if(usersMap.containsKey(offer.getUsername().toLowerCase())){
			User user = usersMap.get(offer.getUsername().toLowerCase());
			offer.setOfferorName(user.getFirstName() + " " + user.getLastName());
			offer.setOfferorMobileNo(FarmerConnectConstants.NOT_AVAILABLE);
		}else {
			offer.setOfferorName(FarmerConnectConstants.NOT_AVAILABLE);
			offer.setOfferorMobileNo(FarmerConnectConstants.NOT_AVAILABLE);
		}
		return offer;
	}

	private Offer appendExpirationInfo(Offer offer) {
		
		long diffInDays = TimeUnit.DAYS.convert(offer.getExpiryDateInMillis() - System.currentTimeMillis(), 
	 											TimeUnit.MILLISECONDS);
		if(diffInDays < 2){
			offer.setExpiresIn(diffInDays == 0 ? TODAY : TOMORROW);
		}else{
			offer.setExpiresIn(String.format(FORMAT_DAYS, diffInDays));
		}
		return offer;
	}
	
	private Bson createFieldNonEmptyClause(String fieldKey){
		
		Document fieldNonEmptyClause = new Document();
		fieldNonEmptyClause.put(fieldKey, MONGO_EXISTS_AND_NON_EMPTY_CLAUSE);
		return fieldNonEmptyClause;
	}
}
