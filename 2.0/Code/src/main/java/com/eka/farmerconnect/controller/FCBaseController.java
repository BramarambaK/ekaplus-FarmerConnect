package com.eka.farmerconnect.controller;

import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.VALUE;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.DATA;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.DEVICE_ID_HEADER;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.FETCH_APP_GENERAL_SETTINGS_URI;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.NAME;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.eka.farmerconnect.exception.FunctionalException;
import com.eka.farmerconnect.model.ErrorResponse;
import com.eka.farmerconnect.model.GeneralSettings;
import com.eka.farmerconnect.service.CommonService;
import com.eka.farmerconnect.util.CommonUtil;
import com.eka.farmerconnect.util.FieldInfo;
import com.eka.farmerconnect.util.FieldType;

@RestController
public class FCBaseController {
	
	@Autowired
	private CommonService commonService;
	
	private static final Logger logger = ESAPI.getLogger(FCBaseController.class);
	public static final String SUCCESS_BODY = "{\"success\":true}";
	private static final String getAppGeneralSettingsBody = "{\"appId\":\"22\",\"type\":\"GENERALSETTINGS\"}";
	private static final GeneralSettings defaultSettings = new GeneralSettings("private", "basic", false, false, false, false, false, false, "");
	
	final Map<FieldInfo, Object> fieldsInfoMap = new LinkedHashMap<>();
	final FieldInfo marketplaceType = new FieldInfo(FieldType.TEXTFIELD, "MARKETPLACE_TYPE");   
	final FieldInfo offerType = new FieldInfo(FieldType.TEXTFIELD, "OFFER_TYPE");   
	final FieldInfo allowContract = new FieldInfo(FieldType.CHECKBOX, "ALLOW_CONTRACT_INTEGRATION");   
	final FieldInfo quantityLock = new FieldInfo(FieldType.CHECKBOX, "IS_QUANTITY_LOCKED");   
	final FieldInfo allowCancellation = new FieldInfo(FieldType.CHECKBOX, "ALLOW_BID_CANCELLATION");   
	final FieldInfo allowOfferRating= new FieldInfo(FieldType.CHECKBOX, "ALLOW_OFFER_RATING");   
	final FieldInfo restrictOfferorInfo= new FieldInfo(FieldType.CHECKBOX, "RESTRICT_OFFEROR_INFO");   
	final FieldInfo restrictPersonalInfoSharing= new FieldInfo(FieldType.CHECKBOX, "RESTRICT_PERSONAL_INFO_SHARING");   
	final FieldInfo rolesToPublish = new FieldInfo(FieldType.TEXTFIELD, "ROLES_TO_PUBLISH");
	
	{
		fieldsInfoMap.put(marketplaceType, null);
		fieldsInfoMap.put(offerType, null);
		fieldsInfoMap.put(allowContract, null);
		fieldsInfoMap.put(quantityLock, null);
		fieldsInfoMap.put(allowCancellation, null);
		fieldsInfoMap.put(allowOfferRating, null);
		fieldsInfoMap.put(restrictOfferorInfo, null);
		fieldsInfoMap.put(restrictPersonalInfoSharing, null);
		fieldsInfoMap.put(rolesToPublish, null);
	}
	
	@Autowired
	private RestTemplate restTemplate;
	
	public static HttpHeaders getDefaultHeaders() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE);
		headers.set(ACCEPT, APPLICATION_JSON_UTF8_VALUE);
		return headers;
	}
	
	public static HttpHeaders getRequestHeaders(HttpServletRequest request) {
		
		HttpHeaders headers = getDefaultHeaders();
		headers.set(AUTHORIZATION, request.getHeader(AUTHORIZATION));
		headers.set(DEVICE_ID_HEADER, request.getHeader(DEVICE_ID_HEADER));
		return headers;
	}
	
	@SuppressWarnings("unchecked")
	public GeneralSettings getAppGeneralSettings(HttpServletRequest request) {
		
		try {
			URI appSettingsURI = commonService.getPlatformUriBuilder(request, FETCH_APP_GENERAL_SETTINGS_URI).build().toUri();
			HttpHeaders headers = getRequestHeaders(request);
			Object appSettingsObj = restTemplate.exchange(appSettingsURI, HttpMethod.POST, new HttpEntity<>(getAppGeneralSettingsBody, headers), Object.class).getBody();
			/*
			 *  Sample Response:
			 *  
			 *  {
				    "generalSettingsEntity": "",
				    "data": [
				        [
				            {
				                "name": "ROLES_TO_PUBLISH",
				                "value": "Trader, Roaster, Exporter"
				            }
				        ],
				        [
				            {
				                "name": "IS_QUANTITY_LOCKED",
				                "value": false
				            }
				        ]
				    ],
				    "success": true
				}
			 *  
			 */
			
			List<List<Map<String, Object>>> appSettings = (List<List<Map<String, Object>>>) ((Map<String, Object>)appSettingsObj).get(DATA);
			List<Map<String, Object>> fieldSettings = appSettings.stream().flatMap(List::stream).collect(Collectors.toList());
			fieldsInfoMap
					.keySet()
					.stream()
					.forEach(fieldInfo -> {
							Object value = fieldSettings
												.stream()
												.filter(fieldSetting -> fieldInfo.getName().equals(fieldSetting.get(NAME)))
												.findFirst().get().get(VALUE);
							fieldsInfoMap.put(fieldInfo, value);
					});
			return CommonUtil.parseGeneralSettings(fieldsInfoMap);
		}catch(Exception ex) {
			logger.error(Logger.EVENT_FAILURE, ex.getMessage(), ex);
			return defaultSettings;
		}
	}
	
	@ExceptionHandler({Exception.class})
	private ResponseEntity<ErrorResponse> handleException(Exception e) {
		
		logger.error(Logger.EVENT_FAILURE, e.getMessage(), e);
		if(e instanceof HttpStatusCodeException) {
			HttpStatusCodeException hsce = (HttpStatusCodeException)e;
			String errMsg = StringUtils.isEmpty(hsce.getResponseBodyAsString()) ? hsce.getMessage() : hsce.getResponseBodyAsString(); 
			return new ResponseEntity<>(new ErrorResponse(errMsg), hsce.getStatusCode());
		}
		
		HttpStatus httpStatus = (e instanceof JSONException 					|| 
								 e instanceof FunctionalException				||
								 e instanceof MethodArgumentNotValidException)	? 	BAD_REQUEST : INTERNAL_SERVER_ERROR;
		
		return new ResponseEntity<>(new ErrorResponse(e.getMessage()), httpStatus);
	}
	
}
