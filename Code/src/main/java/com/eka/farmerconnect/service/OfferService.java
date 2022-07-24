package com.eka.farmerconnect.service;

import static com.eka.farmerconnect.constants.CollectionAPIConstants.APPEND;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.COLL_DATA;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.COLL_DESC;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.COLL_HEADER;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.COLL_NAME;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.DATA_LOAD_OPTIONS;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.DATA_TYPE;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.FIELD_NAME;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.FORMAT;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.FORMAT_JSON;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.COLLECTION_NOT_CONFIGURED;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.COUNTER_COLLECTION_MOBILE_OFFERS;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.PREFIX_FOR_BID_ID;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.eka.core.audit.AuditAction;
import com.eka.core.audit.annotation.AuditAcitivity;
import com.eka.core.audit.annotation.Entity;
import com.eka.db.mongo.MongoDBQueryException;
import com.eka.exceptions.FunctionalException;
import com.eka.farmerconnect.constants.FarmerConnectConstants;
import com.eka.farmerconnect.model.GeneralSettings;
import com.eka.farmerconnect.model.OfferField;
import com.eka.farmerconnect.model.PublishedBid;
import com.eka.farmerconnect.util.HttpClientUtil;
import com.eka.util.ModuleEnum;

import reactor.core.publisher.Mono;

@Service
public class OfferService extends AbstractCommonService{

	@Value("${offer_coll_name}")
	private String offerCollName;
	
	@Value("${offer_coll_desc}")
	private String offerCollDesc;
	
	@Value("#{${offer_field_value_coll_names}}")
	private Map<String, String> offerFieldValueCollNames;
	
	@Value("#{${offer_coll_fields}}")
	private LinkedHashMap<String, String> offerCollFields;
	
	@Autowired
	private HttpClientUtil httpClient;
	
	@Autowired
	private BidService bidService;
	
	private WebClient webClient;
	
	private static JSONObject collectionUpdatePayload = new JSONObject();
	private static Map<String, Object> collectionUnconfigured = new HashMap<>();
	
	@PostConstruct
	private void constructCollectionUploadPayload() {
		
		collectionUpdatePayload 
				= new JSONObject()
						.put(COLL_NAME, offerCollName)
						.put(COLL_DESC, offerCollDesc)
						.put(DATA_LOAD_OPTIONS, APPEND)
						.put(FORMAT, FORMAT_JSON);
		
		JSONArray headers = new JSONArray();
		for (Iterator<String> it = offerCollFields.keySet().iterator(); it.hasNext();) {
			String field = it.next();
			headers.put(new JSONObject()
								.put(FIELD_NAME, field)
								.put(DATA_TYPE, offerCollFields.get(field)));
		}
		collectionUpdatePayload.put(COLL_HEADER, headers);
		
		// Web Client Initialization
		webClient = WebClient
						.builder()
						.clientConnector(new JettyClientHttpConnector())
						.build();
		
		collectionUnconfigured.put(FarmerConnectConstants.DATA, new ArrayList<>());
		collectionUnconfigured.put(FarmerConnectConstants.MSG, COLLECTION_NOT_CONFIGURED);
	}
	
	public void pushRecordToOfferCollection(UriComponentsBuilder uriBuilder, HttpHeaders headers, JSONArray records) {
		
		httpClient.fireHttpRequest( uriBuilder.build().toUri(), 
									HttpMethod.PUT, 
									collectionUpdatePayload
												.put(COLL_DATA, 
													 records)
												.toString(), 
									headers, 
									Object.class);
	}

	public List<Object> getMultipleFieldValues(UriComponentsBuilder uriComponentsBuilder, HttpHeaders headers, List<OfferField> offerFields) {
		
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
													existingHeaders.addAll(headers);
										})
										.retrieve()
										.bodyToMono(Object.class)
										.onErrorReturn(collectionUnconfigured))
						.collect(Collectors.toList());
		
		Mono.zip(monos, mergedResponses -> responses.addAll(Arrays.asList(mergedResponses))).block();
		return responses;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getFieldValues(UriComponentsBuilder uriBuilder, HttpHeaders headers, OfferField offerField) {
		
		URI collectionURI = uriBuilder
								.queryParam(COLL_NAME,
											offerFieldValueCollNames.get(offerField.getId()))
								.build()
								.toUri();
		
		return httpClient.fireHttpRequest(collectionURI,
							  HttpMethod.GET,
							  null,
							  headers, 
							  Map.class).getBody();
	}

	private PublishedBid validateAuthorizationAndGetBidDetails(String bidId, String currentUserName, String tenantId) throws FunctionalException {
		
		PublishedBid bid = bidService.loadPublishedBidFromDataset(bidId, tenantId);
		if(!currentUserName.equalsIgnoreCase(bid.getUsername())) {
			throw new FunctionalException(FarmerConnectConstants.INVALID_BID_ID_MSG);
		}
		return bid;
	}

	@AuditAcitivity(entity = @Entity(name = ModuleEnum.OFFER, isAuditable = false), action = AuditAction.UPDATE)
	public boolean updateOffer(UriComponentsBuilder uriBuilder, HttpHeaders headers, 
			String bidId, PublishedBid updatedBid, String currentUserName, String tenantId) throws FunctionalException {
		
		PublishedBid existingBid = validateAuthorizationAndGetBidDetails(bidId, currentUserName, tenantId);
		JSONArray mergedBidArray = mergeArrays(existingBid.toOfferDataArray(currentUserName), updatedBid.toOfferDataArray(currentUserName,JSONObject.NULL));
		pushRecordToOfferCollection(uriBuilder, headers, new JSONArray().put(mergedBidArray));
		return true;
	}

	private JSONArray mergeArrays(JSONArray baseArray, JSONArray updatedValues) {
		
		for (int index = 1; index < updatedValues.length(); index++) {
			Object element = updatedValues.opt(index);
			if(null != element && JSONObject.NULL != element) {
				baseArray.put(index, element);
			}
		}
		return baseArray;
	}

	@AuditAcitivity(entity = @Entity(name = ModuleEnum.OFFER, isAuditable = false), action = AuditAction.CREATE)
	public String publishOffer(UriComponentsBuilder uriBuilder, HttpHeaders headers,
			PublishedBid offerDetails, String currentUserName, GeneralSettings appSettings) throws MongoDBQueryException {
		
		Integer sequenceId = commonOperationService.getNextCreateSequence(COUNTER_COLLECTION_MOBILE_OFFERS);
		String bidId = PREFIX_FOR_BID_ID + sequenceId;
		offerDetails.setBidId(bidId);
		offerDetails.setRolesToPublish(appSettings.getRolesToPublish());
		pushRecordToOfferCollection(uriBuilder, headers, new JSONArray().put(offerDetails.toOfferDataArray(currentUserName)));
		return bidId;
	}
	@AuditAcitivity(entity = @Entity(name = ModuleEnum.OFFER, isAuditable = false), action = AuditAction.DELETE)
	public boolean deleteOffer(UriComponentsBuilder uriBuilder, HttpHeaders headers,
			String bidId, String currentUserName, String tenantId) throws FunctionalException {
		
		PublishedBid existingBid = validateAuthorizationAndGetBidDetails(bidId, currentUserName, tenantId);
		if(existingBid.getExpiryDate().before(new Date())) {
			// Already expired bid
			return false;
		}
		existingBid.setExpiryDateISOString(FarmerConnectConstants.EXPIRY_DATE_FOR_DELETED_OFFERS);
		pushRecordToOfferCollection(uriBuilder, headers, new JSONArray().put(existingBid.toExpireOfferDataArray(currentUserName,JSONObject.NULL)));
		return true;
	}
}
