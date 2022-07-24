package com.eka.farmerconnect.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import com.eka.farmerconnect.model.Offer;
import com.eka.farmerconnect.service.IPriceSheetService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api/farmerconnect")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceSheetController {

	//private static final Logger logger = LoggerFactory.getLogger(PriceSheetController.class);
	final static  Logger logger = ESAPI.getLogger(PriceSheetController.class);

	
	@Autowired
	private IPriceSheetService priceSheetService;
	
	@PostMapping("/bulkoffers")
	public Object bulkOffers(@RequestBody List<Offer> datasetDetailsList,
			HttpServletRequest request) throws HttpStatusCodeException,
			ParseException, JsonProcessingException {

		logger.info(Logger.EVENT_SUCCESS,"Dataset details is " + datasetDetailsList);
		logger.info(Logger.EVENT_SUCCESS,"Pricesheet creation getting - Initiated");
		ResponseEntity<Object> response = null;

		response = priceSheetService.callBulkOfferAPI(datasetDetailsList, request);

		logger.debug(Logger.EVENT_SUCCESS,"The final response of Pricesheet api is " +response);

		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
	}
	
}
