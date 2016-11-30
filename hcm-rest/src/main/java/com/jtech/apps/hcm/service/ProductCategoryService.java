package com.jtech.apps.hcm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtech.apps.hcm.dao.interfaces.ProductCategoryDAO;
import com.jtech.apps.hcm.model.ProductCategory;
import com.jtech.apps.hcm.model.setting.Setting;

@Service
public class ProductCategoryService {

	@Autowired
	ProductCategoryDAO productCategoryDAO;
 

	public ProductCategory getProductCategoryById(Integer productId) {
		return productCategoryDAO.getProductCategoryBy("PRODUCT_ID", productId.toString());
	}

	public ProductCategory getProductCategoryByProductName(String productName) {
		return productCategoryDAO.getProductCategoryBy("PRODUCT_NAME", productName);
	}

	public List<ProductCategory> getProductCategories() {
		return productCategoryDAO.getProductCategories();
	}

	public Setting getProductSetting(Integer productId) {
		List<Setting> settings = productCategoryDAO.getSettings(productId);
		for (Setting setting : settings) {
			if (setting.isSelected()) {
				return setting;
			}
		}
		return null;
	}

	public ProductCategory getTestData() {
		return productCategoryDAO.getTestData();
	}

	public int updateProductCategory(ProductCategory productCategory) {

		return productCategoryDAO.updateProductCategory(productCategory);
	}

	public int addProductCategory(ProductCategory productCategory) {

		List<ProductCategory> productCategories = getProductCategories();

		for (ProductCategory pc : productCategories) {
			if (pc.getProductName().equals(productCategory.getProductName())) {
				return 0;
			}
		}
		return productCategoryDAO.addProductCategory(productCategory);
	}
}
