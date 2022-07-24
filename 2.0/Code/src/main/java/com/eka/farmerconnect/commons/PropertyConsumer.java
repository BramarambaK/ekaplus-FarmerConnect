package com.eka.farmerconnect.commons;

import java.util.Enumeration;
import java.util.Map;

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
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.eka.farmerconnect.controller.ContractController;
import com.eka.farmerconnect.exception.ConnectException;

@Component
public class PropertyConsumer {

	@Value("${eka_connect_host}")
	private String ekaConnectHost;

	@Autowired
	RestTemplate restTemplate;

	final static  Logger logger = ESAPI.getLogger(PropertyConsumer.class);

	private static final String _PROPERTY_VALUE_KEY = "propertyValue";

	@SuppressWarnings("unchecked")
	public <T> T getPropertyFromConnect(String propertyName, String uid,HttpServletRequest request, String accessToken,Class<T> type) {

		String propertyUri = ekaConnectHost + "/property/" + uid + "/" + propertyName;

		HttpHeaders httpHeaders = getHeaders(request);
		httpHeaders.add("Authorization", accessToken);
		
		T envPropsStr =  null;

		try {

			HttpEntity<Map> entityForPropRequest = new HttpEntity<>(httpHeaders);

			// Call Property API
			logger.info(Logger.EVENT_SUCCESS,"Making a POST call to propertyAPI at endpoint: " + propertyUri + " with request header: "
					+ httpHeaders);
			ResponseEntity<Map> responseEntity = restTemplate.exchange(propertyUri, HttpMethod.GET,
					entityForPropRequest, Map.class);

			if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {

				logger.info(Logger.EVENT_SUCCESS,"Response from property API: " + responseEntity.getBody());

				envPropsStr = (T) responseEntity.getBody().get(_PROPERTY_VALUE_KEY);

			} else {
				logger.error(Logger.EVENT_FAILURE,"PropertyAPI response is not in key-value format.");
				throw new ConnectException("PropertyAPI response is not in key-value format.");
			}

		} catch (HttpStatusCodeException ex) {

			logger.error(Logger.EVENT_FAILURE,"Failed to call Property API: "+ ex.getResponseBodyAsString(),ex);

			throw new ConnectException("Failed to call Property API: " + ex.getResponseBodyAsString());

		}

		return envPropsStr;
	}
	
	public HttpHeaders getHeaders(HttpServletRequest request) {

		HttpHeaders headers = new HttpHeaders();

		Enumeration<?> names = request.getHeaderNames();

		while (names.hasMoreElements()) {

			String name = (String) names.nextElement();
			headers.add(name, request.getHeader(name));
		}
		return headers;

	}

	
	
	public static <T> T getTypeDate(Class<T> type){
		
		String s = "sldfjsldf";
	
		return (T)s;
	}
	
	public static void main(String[] args) {
		System.out.println(getTypeDate(String.class));
	}
}