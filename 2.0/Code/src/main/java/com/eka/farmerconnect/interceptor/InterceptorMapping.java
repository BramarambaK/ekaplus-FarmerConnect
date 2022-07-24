package com.eka.farmerconnect.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Component
public class InterceptorMapping {
	
	@Value("${fc.api.context}")
	private String contextPath;
	
	@Bean
	public MappedInterceptor getMappedInterceptor(@Autowired AuthenticationInterceptor authenticationInterceptor) {
		
		return new MappedInterceptor(new String[] {contextPath+"/**","/api/farmerconnect/**"},new String[] {"/common/getManifestInfo"}, authenticationInterceptor);
	}
}