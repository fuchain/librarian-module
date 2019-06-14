package com.fpt.edu.constant;

public class Constant {

	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_XML = "application/xml";
	public static final String ITEMS = "items";
	public static final String ID = "id";
	public static final String UPDATE_DATE = "updateDate";
	public static final String CREATE_DATE = "createDate";
	public static final String NAME = "name";
	public static final String REGULAR_ID_EXP = "[{][a-zA-Z]+[}]";
	public static final String GOOGLE_AUTH_API = "https://www.googleapis.com/userinfo/v2/me";
	public static final int PIN_EXPIRED_MINUTE = 5;
	public static final int RANDOM_BOUND = 999999;
	public static final int PHONE_NUMBER = 10;
	public static final String ACTION_ADD_NEW = "ADD";
	public static final String ACTION_UPDATE = "UPDATE";
	public static final String NUM_OF_KEEP_BOOK = "num_of_keeping_book";
	public static final String NUM_OF_RETURNING_BOOK = "num_of_returning_book";
	public static final String NUM_OF_REQUESTING_BOOK = "num_of_requesting_book";
	public static final int DEFAULT_PAGE = 1;
	public static final int DEFAULT_OFFSET = 20;
	public static final String AUTHORITIES_HEADER = "roles";
	public static final String ROLES_LIBRARIAN = "librarian";
	public static final int BIGCHAIN_REQUEST_TIMEOUT = 60;
	public static final String EMPTY_VALUE = "";

	//---------------------------------- THIS IS FOR BIGCHAIN ----------------------------------

	// Asset
	public static final String BC_BOOK_ID = "book_id";

	// Metadata
	public static final String CURRENT_KEEPER = "current_keeper";
	public static final String BOOK_STATUS = "status";
	public static final String TX_TIMESTAMP = "transaction_timestamp";
	public static final String REJECT_COUNT = "reject_count";
	public static final String REJECT_REASON = "reject_reason";
	public static final String IMAGE_HASH = "img_hash";
	public static final int MAX_REJECT_COUNT = 5;
	public static final int MIN_REJECT_COUNT = 0;
}
