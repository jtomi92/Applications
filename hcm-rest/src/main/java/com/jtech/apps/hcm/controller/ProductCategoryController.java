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

import com.jtech.apps.hcm.model.ProductCategory;
import com.jtech.apps.hcm.service.ProductCategoryService;

@RestController
public class ProductCategoryController {

	@Autowired
	ProductCategoryService productCategoryService;
 
 
	/*@RequestMapping(value = "/hello", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> health() { 
		
		try {
			Socket socket = new Socket("localhost",86);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<String>("hello", HttpStatus.OK);
	}*/
	
	@RequestMapping(value = "/productcategory/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductCategory> addTestData() {

		ProductCategory pc = productCategoryService.getTestData();

		return new ResponseEntity<ProductCategory>(pc, HttpStatus.OK);
	}

	@RequestMapping(value = "/productcategory/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductCategory>> getProductCategories() {

		List<ProductCategory> productCategories = productCategoryService.getProductCategories();

		if (productCategories == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<List<ProductCategory>>(productCategories, HttpStatus.OK);
	}

	@RequestMapping(value = "/productcategory/get/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductCategory> getProductCategoryById(@PathVariable("id") Integer productId) {

		ProductCategory productCategory = productCategoryService.getProductCategoryById(productId);

		if (productCategory == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<ProductCategory>(productCategory, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/productcategory/get/name/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductCategory> getProductCategoryByName(@PathVariable("id") String productName) {

		ProductCategory productCategory = productCategoryService.getProductCategoryByProductName(productName);

		if (productCategory == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<ProductCategory>(productCategory, HttpStatus.OK);
	}

	@RequestMapping(value = "/productcategory/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> updateProductCategory(@RequestBody ProductCategory productCategory) {

		int err = productCategoryService.updateProductCategory(productCategory);
		return new ResponseEntity<Integer>(err,HttpStatus.OK);
	}

	@RequestMapping(value = "/productcategory/add", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> addProductCategory(@RequestBody ProductCategory productCategory) {

		int err = productCategoryService.addProductCategory(productCategory);
		return new ResponseEntity<Integer>(err, HttpStatus.OK);
	}
}
