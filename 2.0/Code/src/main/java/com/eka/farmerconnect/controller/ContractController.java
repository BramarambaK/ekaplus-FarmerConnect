package com.eka.farmerconnect.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import com.eka.farmerconnect.commons.PropertyConsumer;
import com.eka.farmerconnect.exception.ErrorMessage;
import com.eka.farmerconnect.model.BidToContractEntity;
import com.eka.farmerconnect.service.IContractService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/farmerconnect")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractController {

	//private static final Logger logger = LoggerFactory.getLogger(ContractController.class);
	final static  Logger logger = ESAPI.getLogger(ContractController.class);


	@Autowired
	private IContractService contractService;
	
	@Autowired
	private PropertyConsumer propertyConsumer;
	
	@Value("${eka.farmerconnect.udid}")
	private String farmerConnectUUID;

	// change the input to dataset based payload structure
	@PostMapping("/contract")
	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> createContract(@RequestBody List<Map<String,Object>> datasetDetailsList,
			HttpServletRequest request) throws HttpStatusCodeException,
			ParseException, JsonProcessingException {

		// dataset based pojo
		// get the property-servicekey mapping somewhere? Map<String,String>
		// K-propertyName , V - serviceKey
		// foreach value field get servicekey available map and form mdm api payload
		// convert the mdm api response to map
		logger.info(Logger.EVENT_SUCCESS,"Dataset details is " + datasetDetailsList);
		logger.info(Logger.EVENT_SUCCESS,"Contract creation getting - Initiated");
		ResponseEntity<Object> response = null;
		
		Map<String,String> columnMapping = propertyConsumer.getPropertyFromConnect("bidToContractColumnMapping", farmerConnectUUID,request,request.getHeader("Authorization"),Map.class);
		List<BidToContractEntity> bidToContractList  = massagePayloadToPojo(datasetDetailsList,columnMapping);
		
		response = contractService.saveContracts(bidToContractList, request);

		logger.debug(Logger.EVENT_SUCCESS,"The final response of contract creation api is " + response);

		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleExceptions(Exception exception) {

		ResponseEntity<String> responseEntity = null;

		logger.error(Logger.EVENT_FAILURE,"Error in General Exception contract creation API : " + exception.getMessage(),exception);

		responseEntity = new ResponseEntity<String>(
				"Error in General Exception contract creation API:" + exception.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
		return responseEntity;
	}

	@ExceptionHandler(value = { HttpStatusCodeException.class })
	public final ResponseEntity<ErrorMessage> handleHttpStatusCodeException(HttpStatusCodeException ex) {

		logger.error(Logger.EVENT_FAILURE,"HttpStatusCodeException inside contract creation API() -> "+ex.getMessage(), ex);

		ErrorMessage em = new ErrorMessage();
		HttpStatus status = null;

		status = ex.getStatusCode();
		em.setCode(status.value());
		em.setDescription(ex.getMessage());
		em.setStackTrace(ex.getStackTrace());

		return new ResponseEntity<ErrorMessage>(new ErrorMessage(em.getCode(), em.getDescription(), em.getStackTrace()),
				status);
	}

	@ExceptionHandler(value = { HttpClientErrorException.class })
	public final ResponseEntity<ErrorMessage> handleHttpClientErrorException(HttpClientErrorException exception) {

		logger.error(Logger.EVENT_FAILURE,"HttpClientErrorException inside contract creation API() -> ", exception);

		ErrorMessage em = new ErrorMessage();
		HttpStatus status = null;
		status = exception.getStatusCode();
		em.setCode(status.value());
		em.setDescription(exception.getMessage());
		em.setStackTrace(exception.getStackTrace());

		return new ResponseEntity<ErrorMessage>(new ErrorMessage(em.getCode(), em.getDescription(), em.getStackTrace()),
				status);
	}

	@ExceptionHandler(value = { RestClientException.class })
	public ResponseEntity<String> handleRestClientException(RestClientException exception) {
		ResponseEntity<String> responseEntity = null;

		logger.error(Logger.EVENT_FAILURE,"RestClientException inside contract creation API() -> "+exception.getMessage(), exception);

		responseEntity = new ResponseEntity<String>(
				"RestClientException inside contract creation API() -> " + exception.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
		return responseEntity;
	}

	@ExceptionHandler(value = { ParseException.class })
	public ResponseEntity<String> handleParseException(ParseException exception) {
		ResponseEntity<String> responseEntity = null;

		logger.error(Logger.EVENT_FAILURE,"ParseException inside contract creation API() while coverting to formatted dates-> "+exception.getMessage(), exception);

		responseEntity = new ResponseEntity<String>(
				"RestClientException inside contract creation API() -> while coverting to formatted dates->"
						+ exception.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
		return responseEntity;
	}

	@ExceptionHandler(value = { JsonProcessingException.class })
	public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException exception) {
		ResponseEntity<String> responseEntity = null;

		logger.error(Logger.EVENT_FAILURE,
				"JsonProcessingException inside contract creation API() while mapping to pojo or converting from pogo to map-> "+exception.getMessage(),
				exception);

		responseEntity = new ResponseEntity<String>(
				"RestClientException inside contract creation API() -> while mapping to pojo or converting from pogo to map->"+exception.getMessage()
						+ exception.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
		return responseEntity;
	}

	private List<BidToContractEntity> massagePayloadToPojo(
			List<Map<String, Object>> datasetDetailsList,
			Map<String, String> columnMapping) {
		if (datasetDetailsList == null || datasetDetailsList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		List<BidToContractEntity> pojoList = new ArrayList<>();
		final ObjectMapper mapper = new ObjectMapper(); // jackson's
														// objectmapper

		Set<Entry<String, String>> entrySet = columnMapping.entrySet();

		for (Map<String, Object> data : datasetDetailsList) {

			Iterator<Entry<String, String>> iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> next = iterator.next();
				data.put(next.getKey(), data.remove(next.getValue()));
			}
			pojoList.add(mapper.convertValue(data, BidToContractEntity.class));

		}
		return pojoList;
	}
}
