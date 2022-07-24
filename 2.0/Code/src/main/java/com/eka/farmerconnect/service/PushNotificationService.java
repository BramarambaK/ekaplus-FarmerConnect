package com.eka.farmerconnect.service;

import static com.eka.farmerconnect.constants.CommonConstants.APP_ID;
import static com.eka.farmerconnect.constants.CommonConstants.CLIENTID;
import static com.eka.farmerconnect.constants.CustomerBidOfferConstants.USERNAME;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.FARMER_CONNECT_APP_ID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.eka.farmerconnect.constants.FarmerConnectConstants;

@Service
public class PushNotificationService {

	private static final Logger logger = ESAPI.getLogger(PushNotificationService.class);
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public void produceNotification(String targetUsername, String tenantId, JSONObject payload) {
		try {
			
		JSONObject bidInfo = new JSONObject(payload.toMap())
									.put(USERNAME, targetUsername)
									.put(CLIENTID, tenantId)
									.put(APP_ID, Integer.parseInt(FARMER_CONNECT_APP_ID));
		
		ProducerRecord<String, String> record = new ProducerRecord<>(FarmerConnectConstants.PUSH_NOTIFICATION_TOPIC, bidInfo.toString());
		ListenableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(record);
		sendResult
			.addCallback(result-> {
							logger.debug(Logger.EVENT_SUCCESS, "Notification sent to queue. Metadata: " + result.getRecordMetadata());
						}, 	error -> {
							logger.error(Logger.EVENT_FAILURE, "Error in sending message to queue for User: " + targetUsername
									  	+ ", Client Id: " + tenantId + " Error: " + error);
						});
		}catch(Exception ex) {
			logger.error(Logger.EVENT_FAILURE, "Error in sending message to queue for User: " + targetUsername + ", Client Id: " + tenantId + " Error: " +  ex.getStackTrace());
		}
	}
}
