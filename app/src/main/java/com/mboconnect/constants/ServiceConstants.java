package com.mboconnect.constants;

public class ServiceConstants {

	public static final int SERVICE_TIME_OUT                    = 1000 * 30;
	public static String    SERVICE_GET_OPPORTUNITIES           = "/opportunities?query=";
	public static String    SERVICE_GET_FAVOURITE_OPPORTUNITIES = "/favorites?query=";
	public static String    SERVICE_GET_PROFILE                 = "/user/profile/me";
	public static String    SERVICE_GET_OPPORTUNITY_DETAILS     = "/opportunities/%s";
	public static String    SERVICE_GET_MESSAGES                = "/notifications?query=";
	public static String    SERVICE_GET_PREFERENCES             = "/user/preferences";
	public static String    SERVICE_POST_MARK_FAVORITES         = "/favorites";
	public static String    SERVICE_DELETE_MARK_UNFAVORITES     = "/favorites/%s";
	public static String    SERVICE_POST_CREATE_CONVERSATIONS   = "/conversations";
	public static String    SERVICE_POST_CONVERSATION_MESSAGES   = "/conversations/%s/messages";
	public static String    SERVICE_GET_CONVERSATION_MESSAGES   = "/conversations/%s/messages?query=";
	public static String    SERVICE_MARK_MESSAGES_READ          = "/conversations/%s/markRead";
	public static String    SERVICE_GET_CONVERSATION            = "/conversations?opportunity_id=";
	public static String    SERVICE_SEARCH                      = "/opportunities?query=";
	public static String    SERVICE_DELETE_HIDE_OPPORTUNITY     = "/hidden/%s";
	public static String    SERVICE_IMAGE                       = EnvironmentConstants.SERVICE_BASE_URL + "/r?i=";
	public static String	PRODUCTION_CLIENT_SECRET			= "NGViNjlkNzQtYjlhYy00MDUyLWFjZWYtZjU4ODRiYzhmZDJl";
	public static String	PRE_PROD_CLIENT_SECRET				= "OGVjYTdmNmQtY2I2Yi00MTNmLThkZjctOTA0MTRjY2M3MmYz";
	public static String	PRODUCTION_CLIENT_ID				= "Y29ubmVjdC1tb2JpbGU6";
	public static String	PRE_PROD_CLIENT_ID					= "Y29ubmVjdC13ZWI6";


}
