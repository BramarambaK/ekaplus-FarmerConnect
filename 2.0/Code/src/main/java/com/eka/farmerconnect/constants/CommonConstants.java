package com.eka.farmerconnect.constants;

public interface CommonConstants {
	
	String CLIENTID = "clientId";
	String USERNAME = "username";
	String USERNAME_CAMELCASE = "userName";
	String APP_ID = "appId";
	String STATUS = "status";
	String REF_ID = "refId";
	String TITLE = "title";
	String BODY = "body";
	String NOTIFICATION = "notification";
	String DATA = "data";
	String TARGET = "target";
	
	String FARMER_CONNECT_TARGET = "22/bidDetails/";
	
	String FCM_ENDPOINT = "https://fcm.googleapis.com/fcm/send";
	String TO = "to";
	String TENANT_FULL_DOMAIN_HEADER = "Tenant-Domain";
	String USER_INFO_URI = "/cac-security/api/userinfo";
	String USER_INFO_FILTER = "filter=all";
	String DEVICE_TOKEN = "deviceToken";
	String DEVICE_TYPE = "deviceType";
	String TENANT_INFO = "tenantInfo";
	String SUCCESS = "Success";
	String FAILURE = "Failure";
	String FCM_RESPONSE_SUCCESS = "success";
	String ERROR = "error";
	String RESULTS = "results";
	String DEVICE_ID = "Device-Id";
	String NOT_REGISTERED = "NotRegistered";
	String INVALID_REGISTRATION = "InvalidRegistration";
	
	String CLIENT_ID = "client_id";         
	String EMPTY_STR = "";         
	String USER_ID_CAMEL_CASE = "userId";
	String PERM_CODES= "permCodes";
	String SEQUENCE = "seq";
	String COUNTER_COLLECTION = "CountersCollection";
	String _ID = "_id";
	String MASTER_DATA_CLIENT_ID = "-1";
	String SPECIAL_CHARS_REGEX = "[{}()\\[\\].+*?^$\\\\|]";
	String MATCHED_STRING_REGEX = "\\\\$0";

	String IS_DELETED = "isDeleted";
	String EXPIRY_DATE_IN_MILLIS = "expiryDateInMillis";
	String DELIVERY_FROM_DATE_IN_MILLIS = "deliveryFromDateInMillis";
	String DELIVERY_TO_DATE_IN_MILLIS = "deliveryToDateInMillis";
}
