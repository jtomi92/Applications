package com.jtech.apps.hcm.helpers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.jtech.apps.hcm.model.Connection;
import com.jtech.apps.hcm.model.ProductCategory;
import com.jtech.apps.hcm.model.RegisteredProduct;
import com.jtech.apps.hcm.model.UserProduct;
import com.jtech.apps.hcm.model.UserProfile;

public class RestUtils {

	private final String REST_URL = "http://localhost:8081";
	private final String REST_ADD_REGISTERED_PRODUCT = REST_URL + "/registeredproduct/add";
	private final String REST_GET_REGISTERED_PRODUCT_BY_SERIAL_NUMBER = REST_URL + "/registeredproduct/get/{serial}";
	private final String REST_GET_USER_PRODUCT_BY_SERIAL_NUMBER = REST_URL + "/product/get/serial/{serial}";
	private final String REST_GET_PRODUCT_CATEGORY_BY_ID = REST_URL + "/productcategory/get/id/{id}";
	private final String REST_GET_USER_PROFILE_BY_ID = REST_URL + "/userprofile/get/id/{userid}";
	private final String REST_GET_PRODUCT_CATEGORY_BY_NAME = REST_URL + "/productcategory/get/name/{id}";
	private final String REST_UPDATE_CONNECTION = REST_URL + "/connection/update";
	private final String REST_UPDATE_USER_PRODUCT = REST_URL + "/product/update";

	RestTemplate restTemplate = new RestTemplate();
	HttpHeaders headers = new HttpHeaders();
	HttpHeaders requestHeaders = new HttpHeaders();
	HttpEntity<?> httpEntity = new HttpEntity<Object>(requestHeaders);

	public Integer updateConnection(Connection connection) {

		HttpEntity<Object> httpEntity = new HttpEntity<Object>(connection, requestHeaders);
		ResponseEntity<Integer> response = restTemplate.exchange(REST_UPDATE_CONNECTION, HttpMethod.PUT, httpEntity,
				Integer.class);
		return response.getBody();
	}
	
	public Integer updateUserProduct(UserProduct userProduct){
		
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(userProduct, requestHeaders);
		ResponseEntity<Integer> response = restTemplate.exchange(REST_UPDATE_USER_PRODUCT, HttpMethod.PUT, httpEntity,
				Integer.class);
		return response.getBody();
	}
	
	public ProductCategory getProductCategoryByName(String productName) {
		return restTemplate.getForObject(REST_GET_PRODUCT_CATEGORY_BY_NAME, ProductCategory.class, productName);
	}
	
	public Integer addRegisteredProduct(RegisteredProduct registeredProduct){
		
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(registeredProduct, requestHeaders);
		ResponseEntity<Integer> response = restTemplate.exchange(REST_ADD_REGISTERED_PRODUCT, HttpMethod.PUT, httpEntity,
				Integer.class);
		return response.getBody();
	}
	
	public RegisteredProduct getRegisteredProductBySerialNumber(String serialNumber){
		return restTemplate.getForObject(REST_GET_REGISTERED_PRODUCT_BY_SERIAL_NUMBER, RegisteredProduct.class, serialNumber);
	}
	
	public UserProduct getUserProductBySerialNumber(String serialNumber){
		return restTemplate.getForObject(REST_GET_USER_PRODUCT_BY_SERIAL_NUMBER, UserProduct.class, serialNumber);
	}
	
	public UserProfile getUserProfileByUserId(Integer userId){
		return restTemplate.getForObject(REST_GET_USER_PROFILE_BY_ID, UserProfile.class, userId);
	}
	
	public ProductCategory getProductCategoryById(Integer productId){
		return restTemplate.getForObject(REST_GET_PRODUCT_CATEGORY_BY_ID, ProductCategory.class, productId);	
	}
	
}
