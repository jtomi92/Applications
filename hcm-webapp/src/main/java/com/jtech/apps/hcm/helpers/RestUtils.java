package com.jtech.apps.hcm.helpers;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.jtech.apps.hcm.model.Connection;
import com.jtech.apps.hcm.model.UserProduct;
import com.jtech.apps.hcm.model.UserProfile;

public class RestUtils {

	private static final String REST_URL = "http://localhost:8081";
	private static final String REST_USER_PROFILE_GET_BY_NAME = REST_URL + "/userprofile/get/name/{username}";
	private static final String REST_UPDATE_USER_PROFILE = REST_URL + "/userprofile/update";
	private static final String REST_USER_PRODUCT_GET_BY_ID = REST_URL + "/product/get/userid/{userid}";
	private static final String REST_USER_PRODUCT_GET_BY_SERIAL_NUMBER = REST_URL + "/product/get/serial/{serial}";
	private static final String REST_REGISTER_USER_PRODUCT = REST_URL + "/product/register/{userid}/{serial}";
	private static final String REST_UPDATE_USER_PRODUCT = REST_URL + "/product/update";
	private static final String REST_ADD_USER_PROFILE = REST_URL + "/userprofile/add";
	private static final String REST_SWITCH_RELAY = REST_URL
			+ "/product/switch/{userid}/{serial}/{moduleid}/{relayid}/{state}";
	private static final String REST_GET_CONNECTIONS = REST_URL + "/connection/get";
	private static final String REST_UPDATE_DEVICE = REST_URL + "/device/update/{userid}/{serial}";
	private static final String REST_RESTART_DEVICE = REST_URL + "/device/restart/{userid}/{serial}";

	RestTemplate restTemplate = new RestTemplate();
	HttpHeaders headers = new HttpHeaders();
	HttpHeaders requestHeaders = new HttpHeaders();
	HttpEntity<?> httpEntity = new HttpEntity<Object>(requestHeaders);

	public List<UserProduct> getUserProductsByUserId(Integer userId) {

		ParameterizedTypeReference<List<UserProduct>> typeRef = new ParameterizedTypeReference<List<UserProduct>>() {
		};
		ResponseEntity<List<UserProduct>> responseEntity = restTemplate.exchange(RestUtils.REST_USER_PRODUCT_GET_BY_ID,
				HttpMethod.GET, httpEntity, typeRef, userId);
		List<UserProduct> userProducts = responseEntity.getBody();

		return userProducts;
	}

	public UserProfile getUserProfileByUserName(String userName) {
		return restTemplate.getForObject(REST_USER_PROFILE_GET_BY_NAME, UserProfile.class, userName);
	}
	
	public Integer updateUserProfile(UserProfile userProfile){
	
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(userProfile, requestHeaders);
		ResponseEntity<Integer> response = restTemplate.exchange(REST_UPDATE_USER_PROFILE, HttpMethod.PUT,
				httpEntity, Integer.class);
		return response.getBody();
	}

	public Integer addUserProfile(UserProfile userProfile) {
		ResponseEntity<Integer> response = restTemplate.exchange(REST_ADD_USER_PROFILE, HttpMethod.PUT,
				httpEntity, Integer.class);
		return response.getBody();
	}

	public Integer registerProduct(String userId, String serialNumber) {
		return restTemplate.getForObject(REST_REGISTER_USER_PRODUCT, Integer.class, userId, serialNumber);
	}

	public UserProduct getUserProductBySerialNumber(String serialNumber) {
		return restTemplate.getForObject(REST_USER_PRODUCT_GET_BY_SERIAL_NUMBER, UserProduct.class,
				serialNumber);
	}

	public Integer updateUserProduct(UserProduct userProduct) {
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(userProduct, requestHeaders);
		ResponseEntity<Integer> response = restTemplate.exchange(REST_UPDATE_USER_PRODUCT, HttpMethod.PUT,
				httpEntity, Integer.class);
		return response.getBody();
	}

	public Integer onSwitchRelay(Integer userId, String serialNumber, String moduleId, String relayId, String status) {
		return restTemplate.getForObject(REST_SWITCH_RELAY, Integer.class, userId, serialNumber, moduleId,
				relayId, status);
	}

	public Integer onUpdate(Integer userId, String serialNumber) {
		return restTemplate.getForObject(REST_UPDATE_DEVICE, Integer.class, userId, serialNumber);
	}

	public Integer onRestart(Integer userId, String serialNumber) {
		return restTemplate.getForObject(REST_RESTART_DEVICE, Integer.class, userId, serialNumber);
	}

	public List<Connection> getConnections() {
		ParameterizedTypeReference<List<Connection>> typeRef = new ParameterizedTypeReference<List<Connection>>() {
		};
		ResponseEntity<List<Connection>> responseEntity = restTemplate.exchange(REST_GET_CONNECTIONS,
				HttpMethod.GET, httpEntity, typeRef);
		return responseEntity.getBody();
	}
}
