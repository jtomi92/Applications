package com.jtech.apps.hcm.dao.interfaces;

import java.util.List;

import com.jtech.apps.hcm.model.UserProduct;
import com.jtech.apps.hcm.model.setting.InputSetting;
import com.jtech.apps.hcm.model.setting.ProductControlSetting;
import com.jtech.apps.hcm.model.setting.ProductTriggerSetting;
import com.jtech.apps.hcm.model.setting.ProductUser;
import com.jtech.apps.hcm.model.setting.RelaySetting;
import com.jtech.apps.hcm.model.setting.Setting;

public interface UserProductDAO {

	public int addUserProduct(UserProduct userProduct);
	
	public int addProductUser(ProductUser productUser, String serialNumber);

	public int addUserProductSetting(Setting productSetting, String serialNumber);

	public int addUserProductRelaySetting(RelaySetting relaySettings, String serialNumber, Integer settingId);

	public int addUserProductInputSetting(InputSetting inputSetting, String serialNumber, Integer settingId);

	public int addUserProductControlSetting(ProductControlSetting productControlSetting, String serialNumber,
			Integer settingId, Integer relayId, Integer moduleId);

	public int addUserProductTriggerSetting(ProductTriggerSetting productTriggerSetting, String serialNumber,
			Integer settingId, Integer inputId);

	public List<UserProduct> getUserProducts();

	public List<Setting> getUserProductSettings(String serialNumber);

	public List<RelaySetting> getRelaySettings(String serialNumber, Integer settingId);

	public List<InputSetting> getInputSettings(String serialNumber,  Integer settingId);

	public List<ProductControlSetting> getUserProductControlSettings(String serialNumber, Integer settingId,
			Integer relayId, Integer moduleId);

	public List<ProductTriggerSetting> getProductTriggetSetting(String serialNumber, Integer settingId,
			Integer inputId);

	public int updateUserProduct(UserProduct userProduct);

	public int updateUserProductSetting(Setting setting, String serialNumber);

	public int updateUserProductRelaySetting(RelaySetting relaySettings, String serialNumber, Integer settingId);

	public int updateUserProductInputSetting(InputSetting inputSettings, String serialNumber, Integer settingId);

	public int updateUserProductTriggerSetting(ProductTriggerSetting productTriggerSetting, String serialNumber,
			Integer settingId, Integer inputId);

	public int updateUserProductControlSetting(ProductControlSetting productTriggerSetting, String serialNumber,
			Integer settingId, Integer relayId);
	
	public int deleteUserProductControlSettings(String serialNumber, Integer settingId, Integer relayId, Integer moduleId);
	
	public int deleteUserProductTriggerSettings(String serialNumber, Integer settingId, Integer inputId);
	
}
