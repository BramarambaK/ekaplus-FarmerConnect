package com.eka.farmerconnect.constants;

public class CustomerBidsCollectionConstants {

	public static final String BIDS_COLLECTION_NAME = "CustomerBids";
	public static final String USER_WALLET_COLLECTION = "UserWallet";
	public static final String BLOCK_CHAIN_DATA_COLLECTION = "BlockChainData";
	
	public static final String STATUS = "status";
	public static final String ACCEPTED = "Accepted";
	public static final String REJECTED = "Rejected";
	public static final String CANCELLED = "Cancelled";
	public static final String IN_PROGRESS = "In-Progress";
	
	public static final String PENDING_ON = "pendingOn";
	public static final String TRADER = "Trader";
	public static final String OFFEROR = "Offeror";
	public static final String OFFEROR_NAME = "offerorName";
	public static final String OFFEROR_MOBILE_NO = "offerorMobileNo";
	public static final String FARMER = "Farmer";
	public static final String BIDDER = "Bidder";
	public static final String AGENT = "Agent";
	public static final String PENDING_ON_NONE = "None";
	
	public static final String CUSTOMER_ID = "customerId";
	public static final String BID_ID = "bidId";
	public static final String REF_ID = "refId";
	public static final String UPDATED_DATE = "updatedDate";
	public static final String BID_UPDATED = "bidUpdated";
	public static final String PRICE_UNIT = "priceUnit";
	public static final String RATING = "rating";

	// Negotiation Logs Entries
	public static final String BY = "by";
	public static final String REMARKS = "remarks";
	public static final String PRICE = "price";
	public static final String DATE = "date";
	
	public static final String PUBLISHED_BID_PRICE_REMARKS = "Published Bid Price";

	public static final String NEGOTIATION_LOGS = "negotiationLogs";

	public static final String LATEST_OFFEROR_PRICE = "latestOfferorPrice";
	public static final String LATEST_BIDDER_PRICE = "latestBidderPrice";
	public static final String LATEST_REMARKS = "latestRemarks";
	public static final String UPDATED_BY = "updatedBy";
	public static final String SHIPMENT_DATE = "shipmentDate";
	public static final String USERNAME = "username";
	public static final String PRIMARY_KEY = "_id";

	/**
	 *   0 - Published Bid Entry
	 *   1 - Accepted Bid Entry
	 *  -1 - Rejected Bid Entry
	 */
	public static final String LOG_TYPE = "logType";

	// Filters related constants
	public static final String FITER_BASIC = "Basic";
	public static final String FIRST_FILTER = "firstFilter";
	public static final String SECOND_FILTER = "secondFilter";
	public static final String LOGICAL_OPERATOR = "logicalOperator";
	public static final String TYPE = "type";
	public static final String COLUMN_ID = "columnId";
	public static final String COLUMN_NAME = "columnName";
	public static final String COLUMN_TYPE = "columnType";
	public static final String OPERATOR = "operator";
	public static final String VALUE = "value";

	// Mongo Related Constants
	public static final String REGEX_MONGO = "$regex";
	public static final String EXISTS_MONGO = "$exists";
	public static final String EQ_MONGO = "$eq";
	public static final String NE_MONGO = "$ne";
	public static final String OPTIONS_MONGO = "$options";
	public static final String CARET = "^";
	public static final String DOLLAR = "$";
	public static final String DOT = ".";
	public static final String OPT_CASE_INSENSITIVE = "i";
	
	public static final String AVG_MONGO = "$avg";

	public static final String COMMA = ",";
	public static final String ROLE_SEPARATOR = ", ";
	public static final String AGENT_ID = "agentId";

	public static final String RATING_INFO = "ratingInfo";
	public static final String CURR_BID_RATING = "currentBidRating";
	public static final String RATED_ON = "ratedOn";
	public static final String VERSION = "version";
	public static final String ROLES_TO_PUBLISH = "rolesToPublish";
	public static final String APPLICABLE_ROLES = "applicableRoles";

	private CustomerBidsCollectionConstants(){}
}
