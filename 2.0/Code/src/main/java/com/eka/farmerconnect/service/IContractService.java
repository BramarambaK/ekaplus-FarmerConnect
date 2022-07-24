package com.eka.farmerconnect.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import com.eka.farmerconnect.model.BidToContractEntity;
import com.eka.farmerconnect.model.SettingDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

public interface IContractService {

	public ResponseEntity<Object> saveContracts(List<BidToContractEntity> datasetDetailsList,
			HttpServletRequest request);

	public ResponseEntity<Object> saveContract(BidToContractEntity datasetDetails, HttpServletRequest request,
			List<Map<String, Object>> serviceKeyList, Map<String, Object> serviceKeyMap, String platformURL, Gson gson,
			String accessToken,String trmContractCreationEndpoint,SettingDetails settingDetails) throws HttpStatusCodeException, HttpClientErrorException, RestClientException,
			ParseException, JsonProcessingException;

}
