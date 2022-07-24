package com.eka.farmerconnect.service;

import static com.eka.farmerconnect.constants.CustomerBidsCollectionConstants.USERNAME;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.FARMER_CONNECT_APP_ID;
import static com.eka.service.common.BatchProcessConstants.CLIENTID;
import static com.eka.service.constants.SmartAPPPlatformConstants.APP_ID;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.eka.core.message.KafkaFactory;
import com.eka.farmerconnect.constants.FarmerConnectConstants;

@Service
public class PushNotificationService {

	private static final Logger logger = ESAPI.getLogger(PushNotificationService.class);
	
	private static Producer<String, String> notificationProducer = KafkaFactory.createProducer(producer -> {
		producer.setKeySerializer(StringSerializer.class);
		producer.setValueSerializer(StringSerializer.class);
	});
	
	public void produceNotification(String targetUsername, String tenantId, JSONObject payload) {
		try {
			
		JSONObject bidInfo = new JSONObject(payload.toMap())
									.put(USERNAME, targetUsername)
									.put(CLIENTID, tenantId)
									.put(APP_ID, Integer.parseInt(FARMER_CONNECT_APP_ID));
		
		ProducerRecord<String, String> record = new ProducerRecord<>(FarmerConnectConstants.PUSH_NOTIFICATION_TOPIC, bidInfo.toString());
		notificationProducer.send(record, (metadata, error) -> {
			if(error == null) {
				logger.debug(Logger.EVENT_SUCCESS, "Notification sent to queue. Metadata: " + metadata);
			}else {
				logger.error(Logger.EVENT_FAILURE, "Error in sending message to queue for User: " + targetUsername + ", Client Id: " + tenantId + " Error: " +  error);
			}
		});
		}catch(Exception ex) {
			logger.error(Logger.EVENT_FAILURE, "Error in sending message to queue for User: " + targetUsername + ", Client Id: " + tenantId + " Error: " +  ex.getStackTrace());
		}
	}
}
