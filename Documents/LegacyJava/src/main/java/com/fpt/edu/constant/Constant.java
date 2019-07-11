package com.fpt.edu.constant;

public class Constant {

	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_XML = "application/xml";
	public static final String ITEMS = "items";
	public static final String ID = "id";
	public static final String UPDATE_DATE = "updateDate";
	public static final String CREATE_DATE = "createDate";
	public static final String NAME = "name";
	public static final String IMAGE = "image";
	public static final String SUBJECT_CODE = "code";
	public static final String REGULAR_ID_EXP = "[{][a-zA-Z]+[}]";
	public static final String GOOGLE_AUTH_API = "https://www.googleapis.com/userinfo/v2/me";
	public static final String GOOGLE_BOOK_API = "https://www.googleapis.com/books/v1/volumes";
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
	public static final String ROLES_READER = "reader";
	public static final int BIGCHAIN_REQUEST_TIMEOUT = 60;
	public static final String EMPTY_VALUE = "";
	public static final String ISBN = "isbn";
	public static final String PREVIEW_LINK = "previewLink";
	public static final String DESCRIPTION = "description";
	public static final String IMAGE_THUMBNAIL = "thumbnail";
	public static final String PUBLISHER = "publisher";
	public static final String PUBLISHED_DATE= "publishedDate";
	public static final String AUTHORS= "authors";
	public static final String CATEGORY= "category";
	public static final String DEFAULT_REVIEW_LINK= "DEFAULT_REVIEW_LINK";
	public static final String DEFAULT_IMAGE_LINK= "";
	public static final String DEFAULT_DESCRIPTION= "Description Not Available";
	public static final String DEFAULT_AUTHOR= "default author";
	public static final String DEFAULT_PUBLISHER= "default publisher";
	public static final String DEFAULT_PUBLISH_DATE= "2000-01-01";
	public static final String SUBJECT_CODE_KEY= "subJectCode";
	public static final String[] FPT_EMAIL_PREFIXS = {
		" - K11 FUG HCM", "(SV K12- FUG HCM) "
	};
	public static final int REQUEST_EXPIRED_TIME = 2;
	public static final int MATCHING_EXPIRED_TIME = 2;
	public static final String NOTIFICATION_TYPE_KEEPING = "keeping";
	public static final String NOTIFICATION_TYPE_REQUESTING = "requesting";
	public static final String NOTIFICATION_TYPE_RETURNING = "returning";
	public static final String NOTIFICATION_TYPE_BOOKINSTANCE = "bookinstance";
	public static final String LIBRARIAN_EMAIL = "librarian@fe.edu.vn";

	//---------------------------------- THIS IS FOR BIGCHAIN ----------------------------------

	// Asset
	public static final String BOOK_ID = "book_id";

	// Metadata
	public static final String CURRENT_KEEPER = "current_keeper";
	public static final String BOOK_STATUS = "status";
	public static final String TX_TIMESTAMP = "transaction_timestamp";
	public static final String REJECT_COUNT = "reject_count";
	public static final String REJECT_REASON = "reject_reason";
	public static final String IMAGE_HASH = "img_hash";
	public static final String IMAGE_LINK = "img_link";
	public static final String REJECTOR_EMAIL = "rejector_email";
	public static final int MAX_REJECT_COUNT = 5;
	public static final int MIN_REJECT_COUNT = 0;
}
