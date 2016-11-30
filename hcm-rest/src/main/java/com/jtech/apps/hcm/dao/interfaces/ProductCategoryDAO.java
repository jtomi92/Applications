package com.jtech.apps.hcm.dao.interfaces;

import java.util.List;
import com.jtech.apps.hcm.model.ProductCategory;
import com.jtech.apps.hcm.model.setting.InputSetting;
import com.jtech.apps.hcm.model.setting.RelaySetting;
import com.jtech.apps.hcm.model.setting.Setting;

public interface ProductCategoryDAO {


	public ProductCategory getProductCategoryBy(String entity, String value);
	public List<ProductCategory> getProductCategories();
	public List<Setting> getSettings(Integer productId);
	public List<InputSetting> getInputSettings(Integer productId, Integer settingId);
	public List<RelaySetting> getRelaySettings(Integer productId, Integer settingId);
	
	public int updateProductCategory(ProductCategory pc);
	public int updateSetting(Setting st, Integer productId);
	public int updateRelaySetting(RelaySetting rs, Integer productId, Integer settingId);
	public int updateInputSetting(InputSetting is, Integer productId, Integer settingId);
	
	public int addProductCategory(ProductCategory productCategory);
	public int addSetting(Setting setting, Integer productId);
	public int addRelaySetting(RelaySetting relaySetting, Integer productId, Integer settingId);
	public int addInputSetting(InputSetting inputSetting, Integer productId, Integer settingId);
	public ProductCategory getTestData();
	
	
	
}
