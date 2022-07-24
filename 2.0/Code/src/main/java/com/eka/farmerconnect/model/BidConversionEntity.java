package com.eka.farmerconnect.model;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

public class BidConversionEntity {
	
	private HttpHeaders headers;
	private UriComponentsBuilder uriBuilder;
	private GeneralSettings generalSettings;
	
	public BidConversionEntity(HttpHeaders headers, UriComponentsBuilder uriBuilder, GeneralSettings generalSettings) {
		super();
		this.headers = headers;
		this.uriBuilder = uriBuilder;
		this.generalSettings = generalSettings;
	}

	public BidConversionEntity(GeneralSettings generalSettings) {
		super();
		this.generalSettings = generalSettings;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}

	public UriComponentsBuilder getUriBuilder() {
		return uriBuilder;
	}

	public void setUriBuilder(UriComponentsBuilder uriBuilder) {
		this.uriBuilder = uriBuilder;
	}

	public GeneralSettings getGeneralSettings() {
		return generalSettings;
	}

	public void setGeneralSettings(GeneralSettings generalSettings) {
		this.generalSettings = generalSettings;
	}
}