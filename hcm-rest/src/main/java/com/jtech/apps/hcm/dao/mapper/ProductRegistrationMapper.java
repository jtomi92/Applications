package com.jtech.apps.hcm.dao.mapper;

import java.util.Map;

import com.jtech.apps.hcm.model.RegisteredProduct;

public class ProductRegistrationMapper {

	public RegisteredProduct mapRegisteredProduct(Map<String, Object> row){
		RegisteredProduct registeredProduct = new RegisteredProduct();
		registeredProduct.setSerialNumber((String)row.get("SERIAL_NUMBER"));
		registeredProduct.setProductId((Integer)row.get("PRODUCT_ID"));
		registeredProduct.setRegistered(row.get("IS_REGISTERED").equals("Y"));
		registeredProduct.setActivated(row.get("IS_ACTIVATED").equals("Y"));
		registeredProduct.setCreationDate((String)row.get("CREATION_DATE"));
		registeredProduct.setLastUpdateDate((String)row.get("LAST_UPDATE_DATE"));
		return registeredProduct;
	}
}
