package com.eka.farmerconnect.service;

import static com.eka.farmerconnect.constants.CollectionAPIConstants.APPEND;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.COLL_DATA;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.COLL_DESC;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.COLL_HEADER;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.COLL_NAME;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.DATA_LOAD_OPTIONS;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.DATA_TYPE;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.FIELD_NAME;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.FORMAT;
import static com.eka.farmerconnect.constants.CollectionAPIConstants.FORMAT_JSON;

import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.eka.farmerconnect.model.BidConversionEntity;
import com.eka.farmerconnect.util.HttpClientUtil;

@Service
public class ContractIntegrationService extends AbstractCommonService{

	@Value("${bid_to_contract_coll_name}")
	private String bidToContractCollName;
	
	@Value("${bid_to_contract_coll_desc}")
	private String bidToContractCollDesc;
	
	@Value("#{${bid_to_contract_coll_fields}}")
	private LinkedHashMap<String, String> bidToContractCollFields;
	
	@Autowired
	private HttpClientUtil httpClient;
	
	private static JSONObject bidToContractUpdatePayload = new JSONObject();
	
	@PostConstruct
	private void constructCollectionUploadPayload() {
		
		bidToContractUpdatePayload 
				= new JSONObject()
						.put(COLL_NAME, bidToContractCollName)
						.put(COLL_DESC, bidToContractCollDesc)
						.put(DATA_LOAD_OPTIONS, APPEND)
						.put(FORMAT, FORMAT_JSON);
		
		JSONArray headers = new JSONArray();
		for (Iterator<String> it = bidToContractCollFields.keySet().iterator(); it.hasNext();) {
			String field = it.next();
			headers.put(new JSONObject()
								.put(FIELD_NAME, field)
								.put(DATA_TYPE, bidToContractCollFields.get(field)));
		}
		bidToContractUpdatePayload.put(COLL_HEADER, headers);
	}
	
	public void pushRecordToBidToContractCollection(BidConversionEntity bidConversionEntity, JSONArray records) {
		
		httpClient.fireHttpRequest( bidConversionEntity.getUriBuilder().build().toUri(), 
									HttpMethod.PUT, 
									bidToContractUpdatePayload
												.put(COLL_DATA, 
													 records)
												.toString(), 
									bidConversionEntity.getHeaders(), 
									Object.class);
	}

}