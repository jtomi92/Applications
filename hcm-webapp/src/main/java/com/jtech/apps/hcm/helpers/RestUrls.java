package com.jtech.apps.hcm.helpers;

public class RestUrls {

	public static final String REST_URL = "http://localhost:8081";
	public static final String REST_USER_PROFILE_GET_BY_NAME = REST_URL + "/userprofile/get/name/{username}";
	public static final String REST_USER_PRODUCT_GET_BY_ID = REST_URL + "/product/get/userid/{userid}";
	public static final String REST_USER_PRODUCT_GET_BY_SERIAL_NUMBER = REST_URL + "/product/get/serial/{serial}";
	public static final String REST_REGISTER_USER_PRODUCT = REST_URL + "/product/register/{userid}/{serial}";
	public static final String REST_UPDATE_USER_PRODUCT = REST_URL + "/product/update";
	public static final String REST_ADD_USER_PROFILE = REST_URL + "/userprofile/add";
	public static final String REST_SWITCH_RELAY = REST_URL + "/product/switch/{userid}/{serial}/{moduleid}/{relayid}/{state}";
	public static final String REST_GET_CONNECTIONS = REST_URL + "/connection/get";
}
