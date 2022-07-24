package com.eka.farmerconnect.interceptor;

import static com.eka.farmerconnect.constants.CollectionAPIConstants.DEVICE_ID_HEADER;
import static com.eka.farmerconnect.constants.CommonConstants.CLIENTID;
import static com.eka.farmerconnect.constants.CommonConstants.PERM_CODES;
import static com.eka.farmerconnect.constants.CommonConstants.TENANT_INFO;
import static com.eka.farmerconnect.constants.CommonConstants.USERNAME_CAMELCASE;
import static com.eka.farmerconnect.constants.CommonConstants.USER_INFO_FILTER;
import static com.eka.farmerconnect.constants.CommonConstants.USER_INFO_URI;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import com.eka.farmerconnect.commons.PropertyConsumer;
import com.eka.farmerconnect.model.AuthContext;
import com.eka.farmerconnect.service.CommonService;

@Component
public class AuthenticationInterceptor implements AsyncHandlerInterceptor {
	
	final static Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class); 
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private PropertyConsumer propertyConsumer;
	
	@Value("${eka.farmerconnect.udid}")
	private String farmerConnectUUID;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZATION, request.getHeader(AUTHORIZATION));
		headers.add(DEVICE_ID_HEADER, request.getHeader(DEVICE_ID_HEADER));
		
		String platformUrl = propertyConsumer.getPropertyFromConnect(
				"platform_url", farmerConnectUUID, request,
				request.getHeader(AUTHORIZATION), String.class);
		/*UriComponentsBuilder uriBuilder = commonService.getPlatformUriBuilder(request, USER_INFO_URI)
													   .query(USER_INFO_FILTER);*/
		logger.info("Platform url for authentication :" +platformUrl+USER_INFO_URI);
		Map<String, Object> userInfo = (Map<String, Object>)
										restTemplate.exchange(platformUrl+USER_INFO_URI, 
					 										  HttpMethod.GET, 
					 										  new HttpEntity<>(headers), 
					 										  Object.class)
													.getBody();
		Integer userId  = (Integer)userInfo.get("id");
		Integer tenantId = commonService.getTenantId(userId);
		AuthContext.setCurrentUsername(String.valueOf(userInfo.get(USERNAME_CAMELCASE)));
		AuthContext.setCurrentTenantId(String.valueOf(tenantId));
		AuthContext.setCurrentUserPermCodes((List<String>)userInfo.get(PERM_CODES));
		AuthContext.setCurrentHttpRequest(request);
		return true;
	}

}
