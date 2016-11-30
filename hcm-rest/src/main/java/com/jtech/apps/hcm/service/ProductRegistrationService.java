package com.jtech.apps.hcm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jtech.apps.hcm.dao.interfaces.ProductRegistrationDAO;
import com.jtech.apps.hcm.model.RegisteredProduct;
import com.jtech.apps.hcm.util.TimeUtil;

@Service
public class ProductRegistrationService {

	@Autowired
	ProductRegistrationDAO productRegistrationDAO;

	public int registerProduct(String serial) {
		return productRegistrationDAO.registerProduct(serial);
	}

	public int addRegisteredProduct(RegisteredProduct registeredProduct) {
		List<RegisteredProduct> registeredProducts = getRegisteredProducts();

		if (registeredProduct != null) {
			for (RegisteredProduct rp : registeredProducts) {
				if (rp.getSerialNumber().equals(registeredProduct.getSerialNumber())) {
					return 0;
				}
			}
		}
		return productRegistrationDAO.addRegisteredProduct(registeredProduct);
	}

	public int updateRegisteredProduct(RegisteredProduct registeredProduct) {
		return productRegistrationDAO.updateRegisteredProduct(registeredProduct);
	}

	public RegisteredProduct getRegisteredProductBySerialNumber(String serialNumber) {
		return productRegistrationDAO.getRegisteredProductBySerialNumber(serialNumber);
	}

	public List<RegisteredProduct> getRegisteredProducts() {
		return productRegistrationDAO.getRegisteredProducts();
	}

	public RegisteredProduct getTestData() {
		RegisteredProduct registeredProduct = new RegisteredProduct();
		registeredProduct.setSerialNumber("TEST_SERIAL");
		registeredProduct.setProductId(1);
		registeredProduct.setActivated(false);
		registeredProduct.setRegistered(true);
		registeredProduct.setLastUpdateDate(TimeUtil.getTimeStamp());
		registeredProduct.setCreationDate(TimeUtil.getTimeStamp());
		return registeredProduct;
	}

}
