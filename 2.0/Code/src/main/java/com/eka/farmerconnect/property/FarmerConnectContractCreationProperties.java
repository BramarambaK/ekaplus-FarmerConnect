package com.eka.farmerconnect.property;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eka.farmerconnect.commons.PropertyConsumer;

@Component
public class FarmerConnectContractCreationProperties {

	private String trmEndPoint = "/api/contract/trade";
	
	@Value("${eka.farmerconnect.udid}")
	private String farmerConnectUDID;
	
	@Autowired
	public PropertyConsumer propertyConsumer;
	
	private static final String TRM_HOST_KEY = "eka_ctrm_host";
	
	public String getTRMUrl(HttpServletRequest request,String accessToken) {

		String uploadHost = propertyConsumer.getPropertyFromConnect(TRM_HOST_KEY, farmerConnectUDID,request,accessToken,String.class);

		return uploadHost + trmEndPoint;
	}
	
	
	
}
