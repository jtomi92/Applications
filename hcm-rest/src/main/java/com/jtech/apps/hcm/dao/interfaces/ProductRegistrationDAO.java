package com.jtech.apps.hcm.dao.interfaces;

import java.util.List;

import com.jtech.apps.hcm.model.RegisteredProduct;

public interface ProductRegistrationDAO {

	public int registerProduct(String serial);
	public int addRegisteredProduct(RegisteredProduct registeredProduct);
	public int updateRegisteredProduct(RegisteredProduct registeredProduct);
	public RegisteredProduct getRegisteredProductBySerialNumber(String serialNumber);
	public List<RegisteredProduct> getRegisteredProducts();
	public RegisteredProduct getTestData();
}
