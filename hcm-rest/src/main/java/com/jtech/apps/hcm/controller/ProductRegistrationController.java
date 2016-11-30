package com.jtech.apps.hcm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.jtech.apps.hcm.model.RegisteredProduct;
import com.jtech.apps.hcm.service.ProductRegistrationService;

@RestController
public class ProductRegistrationController {
	@Autowired
	ProductRegistrationService productRegistrationService;

	@RequestMapping(value = "/registeredproduct/test/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisteredProduct> getTestData() {

		RegisteredProduct registeredProduct = productRegistrationService.getTestData();

		if (registeredProduct == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<RegisteredProduct>(registeredProduct, HttpStatus.OK);
	}

	@RequestMapping(value = "/registeredproduct/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RegisteredProduct>> getRegisteredProducts() {

		List<RegisteredProduct> registeredProducts = productRegistrationService.getRegisteredProducts();

		if (registeredProducts == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<List<RegisteredProduct>>(registeredProducts, HttpStatus.OK);
	}

	@RequestMapping(value = "/registeredproduct/get/{serial}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisteredProduct> getRegisteredProductBySerialNumber(
			@PathVariable("serial") String serialNumber) {

		RegisteredProduct registeredProduct = productRegistrationService
				.getRegisteredProductBySerialNumber(serialNumber);
		if (registeredProduct == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<RegisteredProduct>(registeredProduct, HttpStatus.OK);
	}

	@RequestMapping(value = "/registeredproduct/add", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> addRegisteredProduct(@RequestBody RegisteredProduct registeredProduct) {

		int err = productRegistrationService.addRegisteredProduct(registeredProduct);
		return new ResponseEntity<Integer>(err,HttpStatus.OK);
	}

	@RequestMapping(value = "/registeredproduct/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> updateRegisteredProduct(@RequestBody RegisteredProduct registeredProduct) {

		int err = productRegistrationService.updateRegisteredProduct(registeredProduct);
		return new ResponseEntity<Integer>(err, HttpStatus.OK);
	}

}
