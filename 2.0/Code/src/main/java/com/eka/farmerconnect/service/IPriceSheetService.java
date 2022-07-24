package com.eka.farmerconnect.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.eka.farmerconnect.model.Offer;

public interface IPriceSheetService {

	public ResponseEntity<Object> callBulkOfferAPI(List<Offer> datasetDetailsList,
			HttpServletRequest request);
	
}
