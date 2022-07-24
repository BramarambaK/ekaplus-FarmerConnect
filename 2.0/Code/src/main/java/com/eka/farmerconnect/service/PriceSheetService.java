package com.eka.farmerconnect.service;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.owasp.esapi.Logger.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.eka.farmerconnect.commons.CommonValidator;
import com.eka.farmerconnect.controller.PriceSheetController;
import com.eka.farmerconnect.model.Offer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class PriceSheetService implements IPriceSheetService{

	
	final static  Logger logger = ESAPI.getLogger(PriceSheetService.class);

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	CommonValidator validator;
	
	@Value("${eka.authenticate.endpoint}")
	private String authenticateEndPoint;

	@Value("${eka_fc_base64}")
	private String base64;

	@Value("${eka_fc_username}")
	private String username;

	@Value("${eka_fc_password}")
	private String password;
	
	@Value("${eka.bulkoffer.endpoint}")
	private String bulkOffersEndPoint;

	@Value("${eka.offer.endpoint}")
	private String offerEndPoint;



	@Override
	public ResponseEntity<Object> callBulkOfferAPI(List<Offer> datasetDetailsList, HttpServletRequest request) {
		// TODO Auto-generated method stub

		ResponseEntity<Object> response = null;
		Gson gson = new Gson();

		Map payload = getRequestPayload(datasetDetailsList);
		logger.info(Logger.EVENT_SUCCESS,"bulk-offer payload is " + payload);
		//generating access token
		String accessToken = getAccessToken(request, gson);
		HttpHeaders headers = getHeaders(request);
		headers.add("Authorization", validator.cleanData(accessToken));
		HttpEntity<?> requestEntity = new HttpEntity<>(payload,headers);

		logger.info(Logger.EVENT_SUCCESS,"PRICE-SHEET bulk offer End Point : " + offerEndPoint);
		
		try {
			ResponseEntity<String> reponseEntity = restTemplate.exchange(bulkOffersEndPoint, HttpMethod.POST, requestEntity,
					String.class);
			// handle the response;
			logger.info(Logger.EVENT_SUCCESS,"Response For " + reponseEntity);

		} catch (HttpClientErrorException he) {
			logger.error(Logger.EVENT_FAILURE,
					"HttpClientErrorException inside save of contract() while calling general settings API -> "+
					he.getRawStatusCode() + "" + he.getResponseBodyAsString() + he.getResponseHeaders(),he.getCause());
		} catch (HttpStatusCodeException hs) {
			logger.error(Logger.EVENT_FAILURE,"HttpStatusCodeException inside save of contract() while calling general settings API-> "+
					hs.getRawStatusCode() + "" + hs.getResponseBodyAsString() + hs.getResponseHeaders(),hs.getCause());
		} catch (RestClientException ex) {
			logger.error(Logger.EVENT_FAILURE,"RestClientException inside save of contract() while calling general settings API-> ", ex);
		} catch (Exception ex) {
			logger.error(Logger.EVENT_FAILURE,"Exception inside save of contract() -> while calling general settings API", ex);
		}


		logger.info(Logger.EVENT_SUCCESS,"PRICE-SHEET single offer End Point : " + offerEndPoint);

		try {
			ResponseEntity<String> reponseEntity = restTemplate.exchange(offerEndPoint, HttpMethod.POST, requestEntity,
					String.class);
			// handle the response;
			logger.info(Logger.EVENT_SUCCESS,"Response For " + reponseEntity);
		} catch (HttpClientErrorException he) {
			logger.error(Logger.EVENT_FAILURE,"HttpClientErrorException inside save of singleOffer() -> while calling single offer API"+
					he.getRawStatusCode() + "" + he.getResponseBodyAsString() + he.getResponseHeaders(),he.getCause());
		} catch (HttpStatusCodeException hs) {
			logger.error(Logger.EVENT_FAILURE,"HttpStatusCodeException inside save of singleOffer() -> while calling single offer API"+
					hs.getRawStatusCode() + "" + hs.getResponseBodyAsString() + hs.getResponseHeaders(),hs.getCause());

		} catch (RestClientException ex) {
			logger.error(Logger.EVENT_FAILURE,"RestClientException inside save of singleOffer() -> while calling single offer API"+ ex);

		} catch (Exception ex) {
			logger.error(Logger.EVENT_FAILURE,"Exception inside save of singleOffer() -> while calling single offer API"+ ex);

		}

		return response;
	}


	public String getAccessToken(HttpServletRequest request, Gson gson) {

		String accessToken = null;

		ResponseEntity<Object> responseEntity = null;
		HttpHeaders headers = getHeaders(request);
		try {
			// start of calling Property API.

			String authorization = "Basic " + base64;
			headers.add("Authorization", validator.cleanData(authorization));

			JsonObject credentialsObject = new JsonObject();
			credentialsObject.addProperty("userName", validator.cleanData(username));
			credentialsObject.addProperty("pwd", validator.cleanData(password));

			HttpEntity<Object> requestBody = new HttpEntity<Object>(gson.toJson(credentialsObject), headers);

			logger.info(Logger.EVENT_SUCCESS,"Making a POST call to get Access Token at endpoint: " + authenticateEndPoint
					+ " with request payload: " + requestBody);

			responseEntity = restTemplate.exchange(authenticateEndPoint, HttpMethod.POST, requestBody, Object.class);

			if (null != responseEntity) {
				Map responseMap = (LinkedHashMap) responseEntity.getBody();
				Map authToken = (Map) responseMap.get("auth2AccessToken");
				Object accessTokenObj = authToken.get("access_token");
				accessToken = (String) accessTokenObj;
			}

		} catch (HttpClientErrorException he) {
			logger.error(Logger.EVENT_FAILURE,"HttpClientErrorException inside save of contract() -> while calling Authenticate API"+
					he.getRawStatusCode() + "" + he.getResponseBodyAsString() + he.getResponseHeaders(),he.getCause());

		} catch (HttpStatusCodeException hs) {
			logger.error(Logger.EVENT_FAILURE,"HttpStatusCodeException inside save of contract() -> while calling Authenticate API"+
					hs.getRawStatusCode() + "" + hs.getResponseBodyAsString() + hs.getResponseHeaders(),hs.getCause());

		} catch (RestClientException ex) {
			logger.error(Logger.EVENT_FAILURE,"RestClientException inside save of contract() -> while calling Authenticate API", ex);

		} catch (Exception ex) {
			logger.error(Logger.EVENT_FAILURE,"Exception inside save of contract() -> while calling Authenticate API", ex);

		}

		return accessToken;
	}

	
	public Map<String, Object> getRequestPayload(List<Offer> datasetDetailsList){
		
		Map<String, Object> requestPayload = new LinkedHashMap<>();
		
		Map<String, Object> datasetDetailsMap = new LinkedHashMap<>();
		datasetDetailsMap.put("data",datasetDetailsList);
		
		requestPayload.put("mappedCollectionData", null);
		
		requestPayload.put("data", datasetDetailsList);
		
		System.out.println("requestPayload is " + requestPayload);
		
		logger.info(Logger.EVENT_SUCCESS,"requestPayload is " + requestPayload);
		
		return requestPayload;
	}

	public HttpHeaders getHeaders(HttpServletRequest request) {

		HttpHeaders headers = new HttpHeaders();

		Enumeration<?> names = request.getHeaderNames();

		while (names.hasMoreElements()) {

			String name = (String) names.nextElement();
			headers.add(name, validator.cleanData(request.getHeader(name)));
		}
		return headers;

	}


}
