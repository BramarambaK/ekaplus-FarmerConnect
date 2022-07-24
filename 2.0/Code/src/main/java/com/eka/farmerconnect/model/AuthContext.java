package com.eka.farmerconnect.model;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class AuthContext {
	
	private String username;
	private String tenantId;
	private List<String> permCodes;
	private HttpServletRequest httpRequest;
	private static ThreadLocal<AuthContext> context = ThreadLocal.withInitial(() -> new AuthContext());
	
	private AuthContext() {
		super();
	}

	public String getUsername() {
		return username;
	}
	
	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public List<String> getPermCodes() {
		return permCodes;
	}

	public void setPermCodes(List<String> permCodes) {
		this.permCodes = permCodes;
	}
	
	public void setHttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public static String getCurrentUsername() {
		return context.get().getUsername();
	}

	public static void setCurrentUsername(String username) {
		context.get().setUsername(username);
	}

	public static String getCurrentTenantId() {
		return context.get().getTenantId();
	}

	public static void setCurrentTenantId(String tenantId) {
		context.get().setTenantId(tenantId);
	}

	public static void setCurrentUserPermCodes(List<String> permCodes) {
		context.get().setPermCodes(permCodes);
	}
	
	public static List<String> getCurrentUserPermCodes() {
		return context.get().getPermCodes();
	}
	
	public static void setCurrentHttpRequest(HttpServletRequest httpRequest) {
		context.get().setHttpRequest(httpRequest);
	}
	
	public static HttpServletRequest getCurrentHttpRequest() {
		return context.get().getHttpRequest();
	}
}