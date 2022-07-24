package com.eka.farmerconnect.constants;

import static com.eka.farmerconnect.constants.FarmerConnectConstants.PURCHASE;
import static com.eka.farmerconnect.constants.FarmerConnectConstants.SALE;

public class FarmerConnectConstants {

	public static final String PREFIX_FOR_REF_ID = "BID-10000";
	public static final String PREFIX_FOR_BID_ID = "OFR-";
	public static final int DURATION_OF_ACCEPTED_BIDS_LISTING_IN_DAYS = 3;
	
	public static final String PAGE_SIZE = "pageSize";
	public static final String LIMIT = "limit";
	public static final String PAGE = "page";
	public static final String SORTBY = "sortBy";
	public static final String FILTERS = "filters";
	public static final String PAGINATION = "pagination";
	public static final String ASC = "ASC";
	public static final String DESC = "DESC";
	public static final String TEMPLATE_ID = "priceSheet";
	public static final String FARMER_CONNECT_APP_ID = "22";
	
	public static final String OUTPUT_COLL_NAME = "Farmer Connect - Completed Bids Data";
	public static final String OUTPUT_COLL_DESC = "All the Accepted and Rejected Bids Collection";
	public static final String USERNAME = "username";
	public static final String RATING_PENDING = "Pending";
	public static final String TODAY = "Today";
	public static final String FORMAT_DAYS = "%d Days";
	public static final String TOMORROW = "Tomorrow";
	public static final String EXPIRY_DATE = "expiryDate";
	
	public static final String REGEX_COMMA = "\\,";
	public static final String FETCH_CURR_USER_ROLES_URI = "/spring/smartapp/rolesByName";
	public static final String FETCH_APP_GENERAL_SETTINGS_URI = "/spring/smartapp/getAppGeneralSettingsConfig";
	public static final String DATA = "data";
	public static final String NAME = "name";
	
	public static final String DEVICE_ID_HEADER = "Device-Id";
	public static final String DEFAULT_OFFER_TYPE = "Purchase";
	public static final String TITLE = "title";
	public static final String BODY = "body";
	public static final String COUNTERED = "countered";
	public static final String TARGET = "target";
	public static final String PUSH_NOTIFICATION_TOPIC = "PushNotification";
	public static final String EXPIRY_DATE_FOR_DELETED_OFFERS = "1970-01-01T00:00:00.000Z";
	public static final String COUNTER_COLLECTION_MOBILE_OFFERS = "MobileOffers";
	public static final String SUCCESS = "success";
	public static final String MSG = "msg";
	public static final String SALE = "Sale";
	public static final String PURCHASE = "Purchase";
	public static final String NOT_AVAILABLE = "Not Available";
	
	public static final String INVALID_BID_ID_FORMAT = "Invalid(%s)";
	public static final String ROLES_PATTERN_BEGIN = "(^|,\\s?)";
	public static final String ROLES_PATTERN_END = "(,\\s?|$)";
	public static final String PIPE = "|";
	
	// Error/Validation Messages
	public static final String MISSING_MANDATORY_FIELDS = "Mandatory fields can't be empty!";
	public static final String INVALID_BID_ID_MSG = "Invalid or expired Bid ID!";
	public static final String INVALID_DELIVERY_DATE = "Delivery date can't be a past date!";
	public static final String INVALID_EXPIRY_DATE = "Expiry date can't be a past date!";
	public static final String COLLECTION_NOT_CONFIGURED = "Collection is not configured!";
	public static final String PUBLISHED_BID_SETUP_ISSUE = "Published Bid is not setup properly!";
	public static final String QUANTITY_LOCKED = "Quantity Locked: Bidding quantity should be same as published quantity.";
	public static final String INVALID_NUMBER_VALUE = "Only non-negative values are allowed for fields: Published Price and Quantity.";
	public static final String INVALID_OFFER_TYPE = "Unallowed value for Offer Type. Use either " + SALE + " or " + PURCHASE;
	public static final String OFFER_TYPE_UPDATE_ERR = "Offer Type can't be updated!";
	
	private FarmerConnectConstants(){} 
}
