package com.eka.farmerconnect.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.eka.farmerconnect.contract.validator.ContractValidator;
import com.eka.farmerconnect.controller.ContractController;
import com.eka.farmerconnect.model.BidToContractEntity;
import com.eka.farmerconnect.model.SettingDetails;
import com.eka.farmerconnect.model.StatusCollection;
import com.eka.farmerconnect.property.FarmerConnectContractCreationProperties;
import com.eka.farmerconnect.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Service
public class ContractService implements IContractService {

	//private static final Logger logger = LoggerFactory.getLogger(ContractService.class);
	
	final static  Logger logger = ESAPI.getLogger(ContractController.class);


	@Value("${eka.mdm.endpoint}")
	private String mdmEndpoint;

	@Value("${eka.collection.endpoint}")
	private String collectionEndpoint;

	@Value("${eka.generalsettings.endpoint}")
	private String generalSettingsEndPoint;

	@Value("${eka.authenticate.endpoint}")
	private String authenticateEndPoint;

	@Value("${eka.platform.endpoint}")
	private String platformEndpoint;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ContractValidator contractValidator;

	@Autowired
	CommonUtil commonUtil;

	@Autowired
	private FarmerConnectContractCreationProperties fcContractCreationProperties;

	private static final String SERVICEKEY = "serviceKey";
	private static final String DEPENDSON = "dependsOn";

	private static final String ITEMDETAILS = "itemDetails";
	private static final String PRICING = "pricing";

	private static final String KEY = "key";
	private static final String ITEMNO = "1";
	// hard-coding contract type for now as database column is taking not more then
	// 1 characters.
	// private static String contractType = "P";

	// Need to check on DealType, as to get dealType value it requires constant
	// DealType as dependsOn
	private static final String DEALTYPE = "DealType";

	private static final String STATUS = "status";
	private static final String SUCCESS = "success";
	private static final String FAILED = "failed";
	private static final String NOTAPPLICABLE = "N/A";

	// hardcoding for now untill i get to know the actual values for below.
	private static final String COLLECTIONNAME = "Farmer-Connect-ContractStatus-Collection";
	private static final String FORMAT = "JSON";

	private static final String PURCHASE = "Purchase";
	private static final String SALES = "Sale";
	private static final String PURCHASECONTRACT = "P";
	private static final String SALESCONTRACT = "S";
	private static final String FORWARDSLASH = "/";
	private static final String OFFERTYPEBASIC = "basic";
	private static final String PRIVATE = "private";

	private static final String AUTHORIZATION = "Authorization";
	private static final String ACCEPTENCODING = "accept-encoding";
	private static final String JSON = "json";

	private String exceptionMessage = null;
	private Object exceptionObject = null;

	private static final String SETTINGSAPIFAILED = "Settings API Failed";
	private static final String MDMAPIFAILED = "MDM API Failed";
	private static final String TRMAPIFAILED = "TRM API Failed";
	private static final String PLATFORMAPIFAILED = "Platform API Failed";

	@Override
	public ResponseEntity<Object> saveContracts(List<BidToContractEntity> datasetDetailsList,
			HttpServletRequest request) {

		logger.info(Logger.EVENT_SUCCESS,"Contract List Size is " + datasetDetailsList.size());
		ResponseEntity<Object> response = null;
		List<Map<String, Object>> serviceKeyList = null;
		Map<String, Object> serviceKeyMap = null;
		Gson gson = new Gson();
		ResponseEntity<String> settingsResponseEntity = null;
		ResponseEntity<SettingDetails> bidsAndOfferResponseEntity = null;
		
		SettingDetails settingDetails = null;
		SettingDetails bidsAndOfferSettings = null;
		try {
			// getting platform url
			String platformURL = getPlatformURL(request);
			// generating access token
			String accessToken =request.getHeader("Authorization");
			String trmContractCreationEndpoint = fcContractCreationProperties.getTRMUrl(request, accessToken);
			// Getting SettingDetails Object by calling Setting Details API

			String tenantEndPoint = platformURL + FORWARDSLASH + generalSettingsEndPoint;
			HttpHeaders headers = new HttpHeaders();
			headers.set(AUTHORIZATION, accessToken);
			headers.set("requestId",request.getHeader("requestId"));
			//headers.remove(ACCEPTENCODING);
			//headers.add(ACCEPTENCODING, JSON);
			HttpEntity<?> entity = new HttpEntity<>(headers);

			String json = null;
			try {

				logger.info(Logger.EVENT_SUCCESS,"Making a GET call to get general-settings details at endpoint: " + tenantEndPoint
						+ " with request payload: " + entity);

				settingsResponseEntity = restTemplate.exchange(tenantEndPoint, HttpMethod.GET, entity, String.class);
				
				//TODO:uncomment this when bidsAndOffer App is ready and remove the above call.
				/*bidsAndOfferResponseEntity= restTemplate.exchange(tenantEndPoint, HttpMethod.GET, entity, SettingDetails.class);*/

			} catch (HttpClientErrorException he) {
				logger.error(Logger.EVENT_FAILURE,
						"HttpClientErrorException inside save of contract() while calling general settings API -> "+
						he.getRawStatusCode() + "" + he.getResponseBodyAsString() + he.getResponseHeaders(),he.getCause());
				exceptionMessage = SETTINGSAPIFAILED + " " + he.getMessage();
				exceptionObject = he;
			} catch (HttpStatusCodeException hs) {
				logger.error(Logger.EVENT_FAILURE,"HttpStatusCodeException inside save of contract() while calling general settings API-> "+
						hs.getRawStatusCode() + "" + hs.getResponseBodyAsString() + hs.getResponseHeaders(),hs.getCause());
				exceptionMessage = SETTINGSAPIFAILED + " " + hs.getMessage();
				exceptionObject = hs;
			} catch (RestClientException ex) {
				logger.error(Logger.EVENT_FAILURE,"RestClientException inside save of contract() while calling general settings API-> "+ex.getMessage(), ex);
				exceptionMessage = ex.getMessage();
				exceptionObject = ex;
			} catch (Exception ex) {
				logger.error(Logger.EVENT_FAILURE,"Exception inside save of contract() -> while calling general settings API"+ex.getMessage(), ex);
				exceptionMessage = SETTINGSAPIFAILED + " " + ex.getMessage();
				exceptionObject = ex;
			}

			if (null != settingsResponseEntity) {
				json = (String) settingsResponseEntity.getBody();
				settingDetails = new ObjectMapper().readValue(json, SettingDetails.class);
			}
			//TODO:uncomment this when bidsAndOffer App is ready and remove the above call.
			/*if (null != bidsAndOfferResponseEntity) {
				bidsAndOfferSettings = bidsAndOfferResponseEntity.getBody();
			}*/

			for (BidToContractEntity datasetDetails : datasetDetailsList) {
				logger.info(Logger.EVENT_SUCCESS,"All values are " + datasetDetails.toString());
				response = saveContract(datasetDetails, request, serviceKeyList, serviceKeyMap, platformURL, gson,
						accessToken, trmContractCreationEndpoint, settingDetails);
				
				//TODO:uncomment this when bidsAndOffer App is ready and remove the above call.
				/*response = saveContract(datasetDetails, request, serviceKeyList, serviceKeyMap, platformURL, gson,
						accessToken, trmContractCreationEndpoint, bidsAndOfferSettings);*/
			}
		} catch (Exception e) {
			logger.error(Logger.EVENT_FAILURE,"Exception inside saveContracts(-, -) : "+e.getMessage(), e);
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<Object> saveContract(BidToContractEntity bidToContractObject, HttpServletRequest request,
			List<Map<String, Object>> serviceKeyList, Map<String, Object> serviceKeyMap, String platformURL, Gson gson,
			String accessToken, String trmContractCreationEndpoint, SettingDetails settingDetails)
					throws HttpStatusCodeException, HttpClientErrorException, RestClientException, ParseException,
					JsonProcessingException {

		logger.info(Logger.EVENT_SUCCESS,"Save Contract service getting - Initiated");
		List<String> errors = new ArrayList<>();
		Map finalResponseMap = null;
		String statusCollectionEndPoint = platformURL + FORWARDSLASH + collectionEndpoint;

		HttpEntity<Object> requestBody = null;
		HttpHeaders headers = commonUtil.getHeaders(request);
		//headers.add("Authorization", accessToken);

		// Check for null values in BidToContract Dataset
		Errors error = new BeanPropertyBindingResult(bidToContractObject, "bidToContractObject");
		contractValidator.validate(bidToContractObject, error);
		StringBuilder reasonForFailure = new StringBuilder();
		String reasonsForFailure = null;

		if (error.hasErrors()) {

			// 1. Get the list of field errors
			// 2. Get code from field error through stream map
			// 3. choosing the format for collection
			errors.addAll(error.getFieldErrors().stream().map(FieldError::getCode).collect(Collectors.toList()));
			// errors.forEach(reasonForFailure::append);
			reasonsForFailure = String.join(",", errors);
			exceptionObject = NOTAPPLICABLE;

			storeStatusDetailsInCollection(finalResponseMap, bidToContractObject, headers, reasonsForFailure,
					exceptionObject, statusCollectionEndPoint, gson);

		} else {

			Map finalMap = new LinkedHashMap<>();
			Map<String, Object> requestPayload = new HashMap<>();

			BidToContractEntity.TRMPayloadKeyDetails trmPayloadKeyObject = new BidToContractEntity().new TRMPayloadKeyDetails();
			requestPayload = validateContract(serviceKeyList, serviceKeyMap, bidToContractObject, headers, gson,
					finalMap, trmPayloadKeyObject, requestPayload, settingDetails);
			// check if values are matching with MDM
			contractValidator.contractIdNullValidation(requestPayload, errors);

			if (null != errors && errors.size() > 0) {

				reasonsForFailure = String.join(",", errors);
				exceptionObject = NOTAPPLICABLE;

				storeStatusDetailsInCollection(finalResponseMap, bidToContractObject, headers, reasonsForFailure,
						exceptionObject, statusCollectionEndPoint, gson);

			} else {
				finalResponseMap = createContract(settingDetails, bidToContractObject, requestPayload,
						trmPayloadKeyObject, finalResponseMap, trmContractCreationEndpoint, headers, gson, finalMap,
						statusCollectionEndPoint);
			}
		}
		logger.info(Logger.EVENT_SUCCESS,"contract service before calling status collection call");

		logger.info(Logger.EVENT_SUCCESS,"Save Contract service getting - Ended");
		////////////////////////////////////////////////////

		return ResponseEntity.status(HttpStatus.OK).body(finalResponseMap);
	}

	public Map validateContract(List<Map<String, Object>> serviceKeyList, Map<String, Object> serviceKeyMap,
			BidToContractEntity bidToContractObject, HttpHeaders headers, Gson gson, Map finalMap,
			BidToContractEntity.TRMPayloadKeyDetails trmPayloadKeyObject, Map<String, Object> requestPayload,
			SettingDetails settingDetails) {

		HttpEntity<Object> requestBody = null;
		serviceKeyList = new ArrayList<>();
		BidToContractEntity.MDMServiceKeyDetails mdmServiceKeyObject = new BidToContractEntity().new MDMServiceKeyDetails();
		logger.info(Logger.EVENT_SUCCESS,"serviceKeyList after " + serviceKeyList);

		ObjectMapper objMapper = new ObjectMapper();

		// Step1 storing datasets (mapper exception may throw while mapping).
		Map<String, Object> datasetMap = objMapper.convertValue(bidToContractObject, Map.class);

		// step2 storing datsets key with service key as value in fieldsMap
		Map<String, Object> datasetServiceKeyMap = objMapper
				.convertValue(new BidToContractEntity().new MDMServiceKeyDetails(), Map.class);

		// step 3 creating map according to request payload
		Map<String, Object> payloadServiceKeyMap = objMapper
				.convertValue(new BidToContractEntity().new TRMPayloadKeyDetails(), Map.class);

		// getting the service key, to find the key to pass onto depends on key
		// below condition for all non-dependent fields.
		if (null != datasetServiceKeyMap) {
			for (String key : datasetServiceKeyMap.keySet()) {
				// put conditions for which you want or you won't want.
				// put condition to identify depends on and don't add those keys.
				serviceKeyMap = new HashMap<>();
				serviceKeyMap.put(SERVICEKEY, datasetServiceKeyMap.get(key));
				serviceKeyList.add(serviceKeyMap);
			}
		}
		// convert serviceKeyList to String and pass in requestbody
		Map responseMap = null;
		ResponseEntity<Object> responseEntity = null;

		// Adding DealType on 1st MDM Call as it'sdepends on is always constant
		serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getDealType());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(DEALTYPE));
		serviceKeyList.add(serviceKeyMap);
		// start of calling MDM API calls, settings API calls and TRM API calls
		requestBody = new HttpEntity<Object>(gson.toJson(serviceKeyList), headers);
		// 1st MDM Call
		finalMap = callMDMAPI(requestBody, datasetMap, datasetServiceKeyMap, payloadServiceKeyMap, responseMap,
				finalMap);

		// For the fields which are dependent, have to take key and pass to dependent
		// fields
		// as dependsOn in array and form a payload and call the mdm api to get the
		// response

		// Form a second mdm payload to get itemDetails fields.
		// 2nd MDM Call
		serviceKeyList = new ArrayList<>();
		serviceKeyList = getSecondMDMPayload(finalMap, trmPayloadKeyObject, mdmServiceKeyObject);
		requestBody = new HttpEntity<Object>(gson.toJson(serviceKeyList), headers);
		finalMap = callMDMAPI(requestBody, datasetMap, datasetServiceKeyMap, payloadServiceKeyMap, responseMap,
				finalMap);

		// start of 3rd MDM API CALL.
		serviceKeyList = new ArrayList<>();
		serviceKeyList = getThirdMDMPayload(finalMap, trmPayloadKeyObject, mdmServiceKeyObject);
		requestBody = new HttpEntity<Object>(gson.toJson(serviceKeyList), headers);
		finalMap = callMDMAPI(requestBody, datasetMap, datasetServiceKeyMap, payloadServiceKeyMap, responseMap,
				finalMap);

		// start of 4th MDM API CALL.
		serviceKeyList = new ArrayList<>();
		serviceKeyList = getFourthMDMPayload(finalMap, trmPayloadKeyObject, mdmServiceKeyObject);
		requestBody = new HttpEntity<Object>(gson.toJson(serviceKeyList), headers);
		finalMap = callMDMAPI(requestBody, datasetMap, datasetServiceKeyMap, payloadServiceKeyMap, responseMap,
				finalMap);

		// END OF MDM API CALLS.
		logger.info(Logger.EVENT_SUCCESS,"final Map is ---------------------- " + finalMap);

		// from request payload and create one contract
		requestPayload = getRequestPayload(finalMap, bidToContractObject, settingDetails, trmPayloadKeyObject);
		logger.info(Logger.EVENT_SUCCESS,"final request-payload is " + requestPayload);
		return requestPayload;
	}

	public Map createContract(SettingDetails settingDetails, BidToContractEntity bidToContractObject,
			Map<String, Object> requestPayload, BidToContractEntity.TRMPayloadKeyDetails trmPayloadKeyObject,
			Map finalResponseMap, String trmContractCreationEndpoint, HttpHeaders headers, Gson gson, Map finalMap,
			String statusCollectionEndPoint)
					throws HttpClientErrorException, HttpStatusCodeException, RestClientException, ParseException {

		String marketPlaceType = settingDetails.getMarketplaceType();
		String settingsOfferType = settingDetails.getOfferType();
		HttpEntity<Object> requestBody = null;

		try {

			if (marketPlaceType.equals(PRIVATE)) {
				// create one contract based on offer type is equal to purchase or sales.
				String contractType = bidToContractObject.getOfferType();

				// store contract type in single field as db takes single character
				if (contractType.equalsIgnoreCase(PURCHASE)) {
					contractType = PURCHASECONTRACT;
				} else {
					contractType = SALESCONTRACT;
				}
				// if offer type is basic no need of 1) payment terms,2) packing type,
				// 3) packing size,4) deliery from date,5) delivery to date.
				requestPayload.put(trmPayloadKeyObject.getContractType(), contractType);
				if (settingsOfferType.equals(OFFERTYPEBASIC)) {
					// remove packing type,size when you get these fields later
					logger.info(Logger.EVENT_SUCCESS,"final request payload is " + requestPayload);
					// final call to TRM API to create one contract
					requestBody = new HttpEntity<Object>(gson.toJson(requestPayload), headers);
					finalResponseMap = callContractIDAPI(requestBody, trmContractCreationEndpoint, finalResponseMap);
					// call status collection API
					storeStatusDetailsInCollection(finalResponseMap, bidToContractObject, headers, exceptionMessage,
							exceptionObject, statusCollectionEndPoint, gson);
				} else {
					// no need of removing fields, if offer type is advanced
					// create one contract with all the payload
					requestPayload.put(trmPayloadKeyObject.getContractType(), contractType);
					logger.info(Logger.EVENT_SUCCESS,"final request payload is " + requestPayload);
					requestBody = new HttpEntity<Object>(gson.toJson(requestPayload), headers);
					finalResponseMap = callContractIDAPI(requestBody, trmContractCreationEndpoint, finalResponseMap);
					// call status collection API
					storeStatusDetailsInCollection(finalResponseMap, bidToContractObject, headers, exceptionMessage,
							exceptionObject, statusCollectionEndPoint, gson);
				}

			}

			// commented below code
			// need to check requirements if marketPlaceType is other than private.
			// need to create two contracts one purchase and one sales if marketPlaceType is
			// other than private.
			else {
				// need to create two contracts one purchase and one sales
				String purchaseContract = PURCHASECONTRACT;
				String salesContract = SALESCONTRACT;
				// store contract type in single field as db takes single character
				if (settingsOfferType.equals(OFFERTYPEBASIC)) {
					// remove packing type,size when you get these fields later
					// from request payload and create one contract

					requestPayload.put(trmPayloadKeyObject.getContractType(), purchaseContract);
					// get purchase CP Name based on Offer Type
					String purchaseCPName = getPurchaseCPName(bidToContractObject, trmPayloadKeyObject, finalMap);
					requestPayload.put(trmPayloadKeyObject.getCpName(), purchaseCPName);

					// call to TRM API to create purchase contract
					requestBody = new HttpEntity<Object>(gson.toJson(requestPayload), headers);
					finalResponseMap = callContractIDAPI(requestBody, trmContractCreationEndpoint, finalResponseMap);

					// call status collection API
					storeStatusDetailsInCollection(finalResponseMap, bidToContractObject, headers, exceptionMessage,
							exceptionObject, statusCollectionEndPoint, gson);
					// get sales CP Name based on Offer Type
					String salesCPName = getSalesCPName(bidToContractObject, trmPayloadKeyObject, finalMap);
					// create second contract of contract type as sales
					requestPayload.put(trmPayloadKeyObject.getContractType(), salesContract);
					requestPayload.put(trmPayloadKeyObject.getCpName(), salesCPName);

					// call to TRM API to create one contract
					requestBody = new HttpEntity<Object>(gson.toJson(requestPayload), headers);
					finalResponseMap = callContractIDAPI(requestBody, trmContractCreationEndpoint, finalResponseMap);

					// call status collection API
					storeStatusDetailsInCollection(finalResponseMap, bidToContractObject, headers, exceptionMessage,
							exceptionObject, statusCollectionEndPoint, gson);

				} else {
					// no need of removing fields, if offer type is advanced
					// create one contract with all the payload
					requestPayload.put(trmPayloadKeyObject.getContractType(), purchaseContract);
					String purchaseCPName = getPurchaseCPName(bidToContractObject, trmPayloadKeyObject, finalMap);
					requestPayload.put(trmPayloadKeyObject.getCpName(), purchaseCPName);
					// final call to TRM API to create Purchase contract
					requestBody = new HttpEntity<Object>(gson.toJson(requestPayload), headers);
					finalResponseMap = callContractIDAPI(requestBody, trmContractCreationEndpoint, finalResponseMap);

					// call status collection API
					storeStatusDetailsInCollection(finalResponseMap, bidToContractObject, headers, exceptionMessage,
							exceptionObject, statusCollectionEndPoint, gson);

					// create second contract
					String salesCPName = getSalesCPName(bidToContractObject, trmPayloadKeyObject, finalMap);
					requestPayload.put(trmPayloadKeyObject.getContractType(), salesContract);
					requestPayload.put(trmPayloadKeyObject.getCpName(), salesCPName);

					// final call to TRM API to create one contract
					requestBody = new HttpEntity<Object>(gson.toJson(requestPayload), headers);
					finalResponseMap = callContractIDAPI(requestBody, trmContractCreationEndpoint, finalResponseMap);

					// call status collection API
					storeStatusDetailsInCollection(finalResponseMap, bidToContractObject, headers, exceptionMessage,
							exceptionObject, statusCollectionEndPoint, gson);
				}
			}

		} catch (Exception ex) {
			logger.error(Logger.EVENT_FAILURE,"General Exception inside save of contract() ->"+ex.getMessage(), ex);
			exceptionMessage = ex.getMessage();
			exceptionObject = ex;

			// call status collection API
			storeStatusDetailsInCollection(finalResponseMap, bidToContractObject, headers, exceptionMessage,
					exceptionObject, statusCollectionEndPoint, gson);
		}

		return finalResponseMap;
	}

	public Map getAllKeys(Map<String, Object> datasetMap, Map<String, Object> datasetServiceKeyMap,
			Map<String, Object> payloadServiceKeyMap, Map responseMap) {

		Map resultMap = new LinkedHashMap<>();
		// looping dataset

		if (null != datasetMap) {
			datasetMap.forEach((k, v) -> {
				String dataSetKey = (String) k;
				String datasetValue = (String) v;

				if (null != datasetServiceKeyMap && null != payloadServiceKeyMap) {
					// checking if datasetservice map contains datasetkey
					if (datasetServiceKeyMap.containsKey(dataSetKey)) {
						// getting value for mdmservicekey
						String mdmServiceKeyValue = (String) datasetServiceKeyMap.get(dataSetKey);
						// getting value for payloadservicekey
						String payloadServiceKeyValue = (String) payloadServiceKeyMap.get(dataSetKey);

						// if mdmresponseMap contains mdmServiceKeyValue
						if (responseMap.containsKey(mdmServiceKeyValue)) {
							List<HashMap> responseMdmList = (ArrayList<HashMap>) responseMap.get(mdmServiceKeyValue);

							if (responseMdmList != null) {
								for (HashMap map : responseMdmList) {
									// if map1 contains datasetValue then get all the keys for that map
									// if the exact value doesn't match below logic will not work
									if (map.containsValue(datasetValue)) {

										resultMap.put(payloadServiceKeyValue, map.get(KEY));

									}
								}
							}
						}
					}
				}
			});
		}
		return resultMap;

	}

	// Method to form a Request Payload
	// keep it now later will optimize to form a payload in a more generalized way
	public Map<String, Object> getRequestPayload(Map finalMap, BidToContractEntity bidToContractObject,
			SettingDetails settingDetails, BidToContractEntity.TRMPayloadKeyDetails trmPayloadKeyObject) {

		List<Map> itemDetails = new ArrayList<>();
		Map<String, Object> requestPayload = new HashMap<>();
		requestPayload.put(ITEMDETAILS, new ArrayList<>());
		Map<String, Object> itemDetailsMap = new HashMap<>();
		Map<String, Object> pricingMap = new HashMap<>();

		// hard-coded contract type fields for now to create contract
		// requestPayload.put(trmPayloadKeyObject.getContractType(), contractType);
		String productSpecs;

		if (null != bidToContractObject) {
			// fields which doesn't require value-to key conversion

			String cropYearID = (String) finalMap.get(trmPayloadKeyObject.getCropYear());

			if (cropYearID != null) {
				productSpecs = bidToContractObject.getProduct() + "," + bidToContractObject.getOrigin() + ","
						+ bidToContractObject.getCropYear() + "," + bidToContractObject.getQuality();
			} else {
				productSpecs = bidToContractObject.getProduct() + "," + bidToContractObject.getOrigin() + ","
						+ bidToContractObject.getQuality();
			}

			itemDetailsMap.put(trmPayloadKeyObject.getProductSpecs(), productSpecs);
			itemDetailsMap.put(trmPayloadKeyObject.getItemNo(), ITEMNO);
			itemDetailsMap.put(trmPayloadKeyObject.getPeriodType(), bidToContractObject.getPeriodType());
			itemDetailsMap.put(trmPayloadKeyObject.getItemQuantity(), bidToContractObject.getItemQuantity());

			// itemDetailsMap.put(trmPayloadKeyObject.getTolerance(),
			// bidToContractObject.getTolerance());
			// Commenting below code now as TRM Version is not upgraded to 9.1.4 in pre-prod

			itemDetailsMap.put(trmPayloadKeyObject.getToleranceMin(), bidToContractObject.getTolerance());
			itemDetailsMap.put(trmPayloadKeyObject.getToleranceMax(), bidToContractObject.getTolerance());
			itemDetailsMap.put(trmPayloadKeyObject.getToleranceType(), bidToContractObject.getToleranceType());
			itemDetailsMap.put(trmPayloadKeyObject.getToleranceLevel(), bidToContractObject.getToleranceLevel());

			// fields which require date conversion
			String deliveryFromDate = bidToContractObject.getDeliveryFromDate();
			if (null != deliveryFromDate) {
				String deliveryFromDateString = commonUtil.getFormattedDate(deliveryFromDate);
				itemDetailsMap.put(trmPayloadKeyObject.getDeliveryFromDate(), deliveryFromDateString);
			}

			String deliveryToDate = bidToContractObject.getDeliveryToDate();
			if (null != deliveryToDate) {
				String deliveryToDateString = commonUtil.getFormattedDate(deliveryToDate);
				itemDetailsMap.put(trmPayloadKeyObject.getDeliveryToDate(), deliveryToDateString);
			}

			String paymentDueDate = bidToContractObject.getPaymentDueDate();
			if (null != paymentDueDate) {
				String paymentDueDateString = commonUtil.getFormattedDate(paymentDueDate);
				itemDetailsMap.put(trmPayloadKeyObject.getPaymentDueDate(), paymentDueDateString);
			}

			String contractIssueDate = bidToContractObject.getContractIssueDate();
			if (null != contractIssueDate) {
				String contractIssueDateString = commonUtil.getFormattedDate(contractIssueDate);
				requestPayload.put(trmPayloadKeyObject.getContractIssueDate(), contractIssueDateString);
			}
			// fields which doesn't require value-to key conversion
			requestPayload.put(trmPayloadKeyObject.getCpContractRefNo(), bidToContractObject.getCpContractRefNo());

			// if offer type is basic no need of 1) payment terms,2) packing type,
			// 3) packing size,4) deliery from date,5) delivery to date.
			String settingsOfferType = settingDetails.getOfferType();
			if (settingsOfferType.equals("basic")) {
				// remove packing type,size when you get these fields later
				itemDetailsMap.remove(trmPayloadKeyObject.getDeliveryFromDate());
				itemDetailsMap.remove(trmPayloadKeyObject.getDeliveryToDate());
			}

		}

		// fields which require value-to key conversion
		if (null != finalMap) {

			requestPayload.put(trmPayloadKeyObject.getTraderName(), finalMap.get(trmPayloadKeyObject.getTraderName()));
			requestPayload.put(trmPayloadKeyObject.getDealType(), finalMap.get(trmPayloadKeyObject.getDealType()));
			requestPayload.put(trmPayloadKeyObject.getCpName(), finalMap.get(trmPayloadKeyObject.getCpName()));
			requestPayload.put(trmPayloadKeyObject.getIncoTerms(), finalMap.get(trmPayloadKeyObject.getIncoTerms()));
			requestPayload.put(trmPayloadKeyObject.getPaymentTerms(),
					finalMap.get(trmPayloadKeyObject.getPaymentTerms()));
			requestPayload.put(trmPayloadKeyObject.getArbitration(),
					finalMap.get(trmPayloadKeyObject.getArbitration()));
			requestPayload.put(trmPayloadKeyObject.getTermsAndConditions(),
					finalMap.get(trmPayloadKeyObject.getTermsAndConditions()));
			// "isOptionalFieldsEnabled": "false",
			// requestPayload.put("isOptionalFieldsEnabled", "false");

			requestPayload.put(trmPayloadKeyObject.getLegalEntity(),
					finalMap.get(trmPayloadKeyObject.getLegalEntity()));
			requestPayload.put(trmPayloadKeyObject.getQualityFinalAt(),
					finalMap.get(trmPayloadKeyObject.getQualityFinalAt()));
			requestPayload.put(trmPayloadKeyObject.getWeightFinalAt(),
					finalMap.get(trmPayloadKeyObject.getWeightFinalAt()));

			itemDetailsMap.put(trmPayloadKeyObject.getTaxScheduleApplicableCountry(),
					finalMap.get(trmPayloadKeyObject.getTaxScheduleApplicableCountry()));
			itemDetailsMap.put(trmPayloadKeyObject.getTaxSchedule(),
					finalMap.get(trmPayloadKeyObject.getTaxSchedule()));
			itemDetailsMap.put(trmPayloadKeyObject.getProduct(), finalMap.get(trmPayloadKeyObject.getProduct()));
			itemDetailsMap.put(trmPayloadKeyObject.getQuality(), finalMap.get(trmPayloadKeyObject.getQuality()));
			itemDetailsMap.put(trmPayloadKeyObject.getItemQuantityUnitId(),
					finalMap.get(trmPayloadKeyObject.getItemQuantityUnitId()));
			itemDetailsMap.put(trmPayloadKeyObject.getStrategy(), finalMap.get(trmPayloadKeyObject.getStrategy()));
			itemDetailsMap.put(trmPayloadKeyObject.getPayInCurrency(),
					finalMap.get(trmPayloadKeyObject.getPayInCurrency()));
			itemDetailsMap.put(trmPayloadKeyObject.getProfitCenter(),
					finalMap.get(trmPayloadKeyObject.getProfitCenter()));
			itemDetailsMap.put(trmPayloadKeyObject.getLoadingType(),
					finalMap.get(trmPayloadKeyObject.getLoadingType()));
			itemDetailsMap.put(trmPayloadKeyObject.getDestinationType(),
					finalMap.get(trmPayloadKeyObject.getDestinationType()));
			itemDetailsMap.put(trmPayloadKeyObject.getLoadingLocation(),
					finalMap.get(trmPayloadKeyObject.getLoadingLocation()));
			itemDetailsMap.put(trmPayloadKeyObject.getDestinationLocation(),
					finalMap.get(trmPayloadKeyObject.getDestinationLocation()));
			itemDetailsMap.put(trmPayloadKeyObject.getLoadingCountry(),
					finalMap.get(trmPayloadKeyObject.getLoadingCountry()));
			itemDetailsMap.put(trmPayloadKeyObject.getDestinationCountry(),
					finalMap.get(trmPayloadKeyObject.getDestinationCountry()));

			itemDetailsMap.put(trmPayloadKeyObject.getOrigin(), finalMap.get(trmPayloadKeyObject.getOrigin()));
			itemDetailsMap.put(trmPayloadKeyObject.getCropYear(), finalMap.get(trmPayloadKeyObject.getCropYear()));
			itemDetailsMap.put(trmPayloadKeyObject.getPackingType(),
					finalMap.get(trmPayloadKeyObject.getPackingType()));
			// itemDetailsMap.put(trmPayloadKeyObject.getPackingSize(),
			// finalMap.get(trmPayloadKeyObject.getPackingType()));

			// pricing fields
			pricingMap.put(trmPayloadKeyObject.getPriceType(), finalMap.get(trmPayloadKeyObject.getPriceType()));
			pricingMap.put(trmPayloadKeyObject.getPriceUnitId(), finalMap.get(trmPayloadKeyObject.getPriceUnitId()));
			pricingMap.put(trmPayloadKeyObject.getPriceDf(), bidToContractObject.getPriceDf());
			
			itemDetailsMap.put(PRICING, pricingMap);
			
			
			//CFC-977
			requestPayload.put(trmPayloadKeyObject.getTotalQtyUnitId(), itemDetailsMap.get(trmPayloadKeyObject
					.getItemQuantityUnitId()));

			((ArrayList) requestPayload.get(ITEMDETAILS)).add(itemDetailsMap);
		}

		return requestPayload;

	}

	public void storeStatusDetailsInCollection(Map finalResponseMap, BidToContractEntity bidToContractObject,
			HttpHeaders headers, String exceptionMessage, Object exceptionObject, String statusCollectionEndPoint,
			Gson gson) throws HttpStatusCodeException, HttpClientErrorException, RestClientException, ParseException {

		logger.info(Logger.EVENT_SUCCESS,"storeStatusDetailsInCollection method initiated ---- ");

		String status = null;
		if (null != finalResponseMap) {
			status = (String) finalResponseMap.get(STATUS);

			if (null != status) {
				status = SUCCESS;
			} else {
				status = FAILED;
			}
		} else {
			status = FAILED;
		}

		StatusCollection statusCollection = new StatusCollection();
		statusCollection.setCollectionName(COLLECTIONNAME);

		List<Object> dataFields = new ArrayList<Object>();
		dataFields.add(bidToContractObject.getCorporate());
		dataFields.add(bidToContractObject.getCpName());
		dataFields.add(bidToContractObject.getOfferType());

		String cpContractRefNo = bidToContractObject.getCpContractRefNo();
		String offerNo = null;
		String bidRefNo = null;

		if (null != cpContractRefNo) {

			int index = cpContractRefNo.indexOf("B");

			if (index != -1) {
				offerNo = cpContractRefNo.substring(0, index - 1);
				bidRefNo = cpContractRefNo.substring(index);
			}
		}

		dataFields.add(offerNo);
		dataFields.add(bidRefNo);

		if (status.equals(SUCCESS)) {
			dataFields.add("Contract created successfully");
			dataFields.add(NOTAPPLICABLE);
			dataFields.add(NOTAPPLICABLE);
		} else {
			dataFields.add("Contract creation failed");
			if (null != exceptionMessage && null != exceptionObject) {
				dataFields.add(exceptionMessage);
				dataFields.add(exceptionObject.toString());
			}
		}

		List<Object> collectionData = new ArrayList<Object>();
		collectionData.add(dataFields);
		statusCollection.setCollectionData(collectionData);
		statusCollection.setFormat(FORMAT);

		ResponseEntity<Object> response = null;

		try {
			HttpEntity<Object> finalrequestBody = new HttpEntity<Object>(gson.toJson(statusCollection), headers);

			logger.info(Logger.EVENT_SUCCESS,"Making a PUT call to update status in collection  details at endpoint: "
					+ statusCollectionEndPoint + " with request payload: " + finalrequestBody);

			response = restTemplate.exchange(statusCollectionEndPoint, HttpMethod.PUT, finalrequestBody, Object.class);

		} catch (HttpClientErrorException he) {
			logger.error(Logger.EVENT_FAILURE,"HttpClientErrorException inside save of contract() while calling status collection API -> "+
					he.getRawStatusCode() + "" + he.getResponseBodyAsString() + he.getResponseHeaders(),he.getCause());
			exceptionMessage = he.getMessage();
			exceptionObject = he;
		} catch (HttpStatusCodeException hs) {
			logger.error(Logger.EVENT_FAILURE,"HttpStatusCodeException inside save of contract() while calling status collection API -> "+
					hs.getRawStatusCode() + "" + hs.getResponseBodyAsString() + hs.getResponseHeaders(),hs.getCause());
			exceptionMessage = hs.getMessage();
			exceptionObject = hs;
		} catch (RestClientException ex) {
			logger.error(Logger.EVENT_FAILURE,"RestClientException inside save of contract() -> while calling status collection API "+ex.getMessage(), ex);
			exceptionMessage = ex.getMessage();
			exceptionObject = ex;
		} catch (Exception ex) {
			logger.error(Logger.EVENT_FAILURE,"Exception inside save of contract() -> while calling status collection API "+ex.getMessage(), ex);
			exceptionMessage = ex.getMessage();
			exceptionObject = ex;
		}

		logger.info(Logger.EVENT_SUCCESS,"response from status collection API is " + response);

	}

	public List<Map<String, Object>> getSecondMDMPayload(Map finalMap,
			BidToContractEntity.TRMPayloadKeyDetails trmPayloadKeyObject,
			BidToContractEntity.MDMServiceKeyDetails mdmServiceKeyObject) {
		// Form a second mdm payload to get itemDetails fields.
		String dealTypeKey = (String) finalMap.get(trmPayloadKeyObject.getDealType());
		String productKey = (String) finalMap.get(trmPayloadKeyObject.getProduct());
		String payInCurIdKey = (String) finalMap.get(trmPayloadKeyObject.getPayInCurrency());
		String taxScheduleCountryIdKey = (String) finalMap.get(trmPayloadKeyObject.getTaxScheduleApplicableCountry());
		String traderUserIdKey = (String) finalMap.get(trmPayloadKeyObject.getTraderName());
		String incotermIdKey = (String) finalMap.get(trmPayloadKeyObject.getIncoTerms());

		List<Map<String, Object>> serviceKeyList = new ArrayList<>();
		// getting the service key, to find the key to pass onto depends on key
		// put conditions for which you want or you won't want.
		// remove hard-coded service key values
		Map<String, Object> serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getQuality());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(productKey));
		serviceKeyList.add(serviceKeyMap);

		serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getItemQuantityUnitId());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(productKey));

		serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getPriceType());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(productKey));
		serviceKeyList.add(serviceKeyMap);

		serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getTaxSchedule());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(taxScheduleCountryIdKey));
		serviceKeyList.add(serviceKeyMap);

		serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getPriceUnitId());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(productKey, payInCurIdKey));
		serviceKeyList.add(serviceKeyMap);

		serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getLoadingType());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(incotermIdKey));
		serviceKeyList.add(serviceKeyMap);

		serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getProfitCenter());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(traderUserIdKey));
		serviceKeyList.add(serviceKeyMap);

		serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getOrigin());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(productKey));
		serviceKeyList.add(serviceKeyMap);

		serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getCpName());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(dealTypeKey));
		serviceKeyList.add(serviceKeyMap);

		return serviceKeyList;
	}

	public List<Map<String, Object>> getThirdMDMPayload(Map finalMap,
			BidToContractEntity.TRMPayloadKeyDetails trmPayloadKeyObject,
			BidToContractEntity.MDMServiceKeyDetails mdmServiceKeyObject) {
		List<Map<String, Object>> serviceKeyList = new ArrayList<>();

		String dealTypeKey = (String) finalMap.get(trmPayloadKeyObject.getDealType());
		String originationCountryIdKey = (String) finalMap.get(trmPayloadKeyObject.getLoadingCountry());
		String loadingLocationGroupTypeIdKey = (String) finalMap.get(trmPayloadKeyObject.getLoadingType());

		String productKey = (String) finalMap.get(trmPayloadKeyObject.getProduct());
		String originKey = (String) finalMap.get(trmPayloadKeyObject.getOrigin());

		Map<String, Object> serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getPurchaseContractCPName());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(dealTypeKey));
		serviceKeyList.add(serviceKeyMap);

		serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getLoadingLocation());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(originationCountryIdKey, loadingLocationGroupTypeIdKey));
		serviceKeyList.add(serviceKeyMap);

		serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getCropYear());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(productKey, originKey));
		serviceKeyList.add(serviceKeyMap);

		return serviceKeyList;
	}

	public List<Map<String, Object>> getFourthMDMPayload(Map finalMap,
			BidToContractEntity.TRMPayloadKeyDetails trmPayloadKeyObject,
			BidToContractEntity.MDMServiceKeyDetails mdmServiceKeyObject) {
		List<Map<String, Object>> serviceKeyList = new ArrayList<>();
		String dealTypeKey = (String) finalMap.get(trmPayloadKeyObject.getDealType());
		String destinationCountryIdKey = (String) finalMap.get(trmPayloadKeyObject.getDestinationCountry());
		String destinationLocationGroupTypeIdKey = (String) finalMap.get(trmPayloadKeyObject.getLoadingType());
		Map<String, Object> serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getLoadingLocation());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(destinationCountryIdKey, destinationLocationGroupTypeIdKey));
		serviceKeyList.add(serviceKeyMap);

		serviceKeyMap = new HashMap<>();
		serviceKeyMap.put(SERVICEKEY, mdmServiceKeyObject.getSalesContractCPName());
		serviceKeyMap.put(DEPENDSON, Arrays.asList(dealTypeKey));
		serviceKeyList.add(serviceKeyMap);

		return serviceKeyList;

	}
	
	/*public String getAccessToken(HttpServletRequest request, Gson gson) {

		String accessToken = null;

		ResponseEntity<Object> responseEntity = null;
		HttpHeaders headers = commonUtil.getHeaders(request);
		try {
			// start of calling Property API.

			String authorization = "Basic " + base64;
			headers.add("Authorization", authorization);

			JsonObject credentialsObject = new JsonObject();
			credentialsObject.addProperty("userName", username);
			credentialsObject.addProperty("pwd", password);

			HttpEntity<Object> requestBody = new HttpEntity<Object>(gson.toJson(credentialsObject), headers);

			logger.info("Making a POST call to get Access Token at endpoint: " + authenticateEndPoint
					+ " with request payload: " + requestBody);

			responseEntity = restTemplate.exchange(authenticateEndPoint, HttpMethod.POST, requestBody, Object.class);

			if (null != responseEntity) {
				Map responseMap = (LinkedHashMap) responseEntity.getBody();
				Map authToken = (Map) responseMap.get("auth2AccessToken");
				Object accessTokenObj = authToken.get("access_token");
				accessToken = (String) accessTokenObj;
			}

		} catch (HttpClientErrorException he) {
			logger.error("HttpClientErrorException inside save of contract() -> while calling Authenticate API",
					he.getRawStatusCode() + "" + he.getResponseBodyAsString() + he.getResponseHeaders());
			exceptionMessage = he.getMessage();
			exceptionObject = he;
		} catch (HttpStatusCodeException hs) {
			logger.error("HttpStatusCodeException inside save of contract() -> while calling Authenticate API",
					hs.getRawStatusCode() + "" + hs.getResponseBodyAsString() + hs.getResponseHeaders());
			exceptionMessage = hs.getMessage();
			exceptionObject = hs;
		} catch (RestClientException ex) {
			logger.error("RestClientException inside save of contract() -> while calling Authenticate API", ex);
			exceptionMessage = ex.getMessage();
			exceptionObject = ex;
		} catch (Exception ex) {
			logger.error("Exception inside save of contract() -> while calling Authenticate API", ex);
			exceptionMessage = ex.getMessage();
			exceptionObject = ex;
		}

		return accessToken;
	}*/

	public String getPlatformURL(HttpServletRequest request) {
		String platformURL = null;

		ResponseEntity<Object> responseEntity = null;
		HttpHeaders headers = commonUtil.getHeaders(request);

		try {
			// start of calling Property API.

			HttpEntity<Object> requestBody = new HttpEntity<Object>(headers);

			logger.info(Logger.EVENT_SUCCESS,"Making a GET call to get Platform URL at endpoint: " + platformEndpoint
					+ " with request payload: " + requestBody);

			responseEntity = restTemplate.exchange(platformEndpoint, HttpMethod.GET, requestBody, Object.class);

		} catch (HttpClientErrorException he) {
			logger.error(Logger.EVENT_FAILURE,"HttpClientErrorException inside save of contract() -> while calling Platform API"+
					he.getRawStatusCode() + "" + he.getResponseBodyAsString() + he.getResponseHeaders(),he.getCause());
			exceptionMessage = PLATFORMAPIFAILED + " " + he.getMessage();
			exceptionObject = he;
		} catch (HttpStatusCodeException hs) {
			logger.error(Logger.EVENT_FAILURE,"HttpStatusCodeException inside save of contract() -> while calling Platform API"+
					hs.getRawStatusCode() + "" + hs.getResponseBodyAsString() + hs.getResponseHeaders(),hs.getCause());
			exceptionMessage = PLATFORMAPIFAILED + " " + hs.getMessage();
			exceptionObject = hs;
		} catch (RestClientException ex) {
			logger.error(Logger.EVENT_FAILURE,"RestClientException inside save of contract() -> while calling Platform API"+ex.getMessage(), ex);
			exceptionMessage = PLATFORMAPIFAILED + " " + ex.getMessage();
			exceptionObject = ex;
		} catch (Exception ex) {
			logger.error(Logger.EVENT_FAILURE,"Exception inside save of contract() -> while calling Platform API"+ex.getMessage(), ex);
			exceptionMessage = PLATFORMAPIFAILED + " " + ex.getMessage();
			exceptionObject = ex;
		}

		if (null != responseEntity) {
			Map responseMap = (LinkedHashMap) responseEntity.getBody();
			platformURL = (String) responseMap.get("propertyValue");

		}

		return platformURL;
	}

	public Map callMDMAPI(HttpEntity<Object> requestBody, Map<String, Object> datasetMap,
			Map<String, Object> datasetServiceKeyMap, Map<String, Object> payloadServiceKeyMap, Map responseMap,
			Map finalMap) {
		ResponseEntity<Object> responseEntity = null;

		try {
			logger.info(Logger.EVENT_SUCCESS,"Making a POST call to get MDM Data at endpoint: " + mdmEndpoint + " with request payload: "
					+ requestBody);

			responseEntity = restTemplate.exchange(mdmEndpoint, HttpMethod.POST, requestBody, Object.class);
		} catch (HttpClientErrorException he) {
			logger.error(Logger.EVENT_FAILURE,"HttpClientErrorException inside save of contract() -> while calling MDM API"+
					he.getRawStatusCode() + "" + he.getResponseBodyAsString() + he.getResponseHeaders(),he.getCause());
			exceptionMessage = MDMAPIFAILED + " " + he.getMessage();
			exceptionObject = he;
		} catch (HttpStatusCodeException hs) {
			logger.error(Logger.EVENT_FAILURE,"HttpStatusCodeException inside save of contract() -> while calling MDM API"+
					hs.getRawStatusCode() + "" + hs.getResponseBodyAsString() + hs.getResponseHeaders(),hs.getCause());
			exceptionMessage = MDMAPIFAILED + " " + hs.getMessage();
			exceptionObject = hs;
		} catch (RestClientException ex) {
			logger.error(Logger.EVENT_FAILURE,"RestClientException inside save of contract() -> while calling MDM API"+ex.getMessage(), ex);
			exceptionMessage = MDMAPIFAILED + " " + ex.getMessage();
			exceptionObject = ex;
		} catch (Exception ex) {
			logger.error(Logger.EVENT_FAILURE,"Exception inside save of contract() -> while calling MDM API"+ex.getMessage(), ex);
			exceptionMessage = MDMAPIFAILED + " " + ex.getMessage();
			exceptionObject = ex;
		}

		// get the keys if response from mdm is not null
		if (null != responseEntity) {
			responseMap = (LinkedHashMap) responseEntity.getBody();

			// get the response of all key-values by passing request body as serviceKeyList.
			// get the id of each service key and put it in a new map.
			finalMap.putAll(getAllKeys(datasetMap, datasetServiceKeyMap, payloadServiceKeyMap, responseMap));

		}
		return finalMap;
	}

	public Map callContractIDAPI(HttpEntity<Object> requestBody, String trmContractCreationEndpoint,
			Map finalResponseMap) {
		ResponseEntity<Object> responseEntity = null;
		try {

			// Getting the TRM-END Point by Property API

			logger.info(Logger.EVENT_SUCCESS,"Making a POST call to create contract api details at endpoint: " + trmContractCreationEndpoint
					+ " with request payload: " + requestBody);

			responseEntity = restTemplate.exchange(trmContractCreationEndpoint, HttpMethod.POST, requestBody,
					Object.class);

		} catch (HttpClientErrorException he) {
			logger.error(Logger.EVENT_FAILURE,"HttpClientErrorException inside save of contract() while calling TRM create contract API -> "+
					he.getRawStatusCode() + "" + he.getResponseBodyAsString() + he.getResponseHeaders(),he.getCause());
			exceptionMessage = TRMAPIFAILED + " " + he.getMessage();
			exceptionObject = he;
		} catch (HttpStatusCodeException hs) {
			logger.error(Logger.EVENT_FAILURE,"HttpStatusCodeException inside save of contract() while calling TRM create contract API -> "+
					hs.getRawStatusCode() + "" + hs.getResponseBodyAsString() + hs.getResponseHeaders(),hs.getCause());
			exceptionMessage = TRMAPIFAILED + " " + hs.getMessage();
			exceptionObject = hs;
		} catch (RestClientException ex) {
			logger.error(Logger.EVENT_FAILURE,"RestClientException inside save of contract() while calling TRM create contract API -> "+ex.getMessage(), ex);
			exceptionMessage = TRMAPIFAILED + " " + ex.getMessage();
			exceptionObject = ex;
		} catch (Exception ex) {
			logger.error(Logger.EVENT_FAILURE,"Exception inside save of contract() -> while calling TRM create contract API ->"+ex.getMessage(), ex);
			exceptionMessage = TRMAPIFAILED + " " + ex.getMessage();
			exceptionObject = ex;
		}
		if (null != responseEntity) {
			finalResponseMap = (LinkedHashMap) responseEntity.getBody();
		}
		return finalResponseMap;
	}

	public String getPurchaseCPName(BidToContractEntity bidToContractObject,
			BidToContractEntity.TRMPayloadKeyDetails trmPayloadKeyObject, Map finalMap) {

		String cpName = null;

		if (bidToContractObject.getOfferType().equals(PURCHASE)) {
			// bidder cp name
			cpName = (String) finalMap.get(trmPayloadKeyObject.getPurchaseContractCPName());
		} else {
			// offeror cp name
			cpName = (String) finalMap.get(trmPayloadKeyObject.getSalesContractCPName());
		}
		return cpName;
	}

	public String getSalesCPName(BidToContractEntity bidToContractObject,
			BidToContractEntity.TRMPayloadKeyDetails trmPayloadKeyObject, Map finalMap) {

		String cpName = null;

		if (bidToContractObject.getOfferType().equals(PURCHASE)) {
			// offeror cp name
			cpName = (String) finalMap.get(trmPayloadKeyObject.getSalesContractCPName());
		} else {
			// bidder cp name
			cpName = (String) finalMap.get(trmPayloadKeyObject.getPurchaseContractCPName());
			
		}
		return cpName;
	}

}
