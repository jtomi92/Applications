package com.jtech.apps.hcm.dao.mapper;

import java.util.Map;

import com.jtech.apps.hcm.model.UserProduct;
import com.jtech.apps.hcm.model.setting.InputSetting;
import com.jtech.apps.hcm.model.setting.ProductControlSetting;
import com.jtech.apps.hcm.model.setting.ProductTriggerSetting;
import com.jtech.apps.hcm.model.setting.RelaySetting;
import com.jtech.apps.hcm.model.setting.Setting;

public class UserProductMapper {

	public UserProduct mapUserProduct(Map<String, Object> row) {

		UserProduct userProduct = new UserProduct();
		userProduct.setSerialNumber((String) row.get("SERIAL_NUMBER"));
		userProduct.setName((String) row.get("NAME"));
		userProduct.setPhoneNumber((String) row.get("PHONE_NUMBER"));
		userProduct.setPrimaryHost((String) row.get("HOST1")); 
		userProduct.setPrimaryPort((String) row.get("PORT1"));
		userProduct.setSecondaryHost((String) row.get("HOST2"));
		userProduct.setSecondaryPort((String) row.get("PORT2"));
		userProduct.setEdited(row.get("EDITED").equals("Y"));
		userProduct.setCreationDate((String) row.get("CREATION_DATE"));
		userProduct.setLastUpdateDate((String) row.get("LAST_UPDATE_DATE"));

		return userProduct;
	}

	public Setting mapSetting(Map<String, Object> row) {

		Setting setting = new Setting();
		setting.setSettingId((Integer) row.get("SETTING_ID"));
		setting.setSettingName((String) row.get("SETTING_NAME"));
		setting.setSelected(row.get("SELECTED").toString().equals("Y"));
		setting.setCreationDate((String) row.get("CREATION_DATE"));
		setting.setLastUpdateDate((String) row.get("LAST_UPDATE_DATE"));
		return setting;
	}

	public InputSetting mapInputSetting(Map<String, Object> row) {

		InputSetting inputSetting = new InputSetting();
		inputSetting.setInputId((Integer) row.get("INPUT_ID"));
		inputSetting.setInputName((String) row.get("INPUT_NAME"));
		inputSetting.setStartTimer((String) row.get("START_TIMER"));
		inputSetting.setEndTimer((String) row.get("END_TIMER"));
		inputSetting.setTimerEnabled(row.get("TIMER_ENABLED").toString().equals("Y"));
		inputSetting.setValuePostfix((String) row.get("VALUE_POSTFIX"));
		inputSetting.setSampleRate((String) row.get("SAMPLE_RATE"));
		inputSetting.setCreationDate((String) row.get("CREATION_DATE"));
		inputSetting.setLastUpdateDate((String) row.get("LAST_UPDATE_DATE"));

		return inputSetting;
	}

	public RelaySetting mapRelaySetting(Map<String, Object> row) {

		RelaySetting relaySetting = new RelaySetting();
		relaySetting.setRelayId((Integer) row.get("RELAY_ID"));
		relaySetting.setRelayName((String) row.get("RELAY_NAME"));
		relaySetting.setRelayStatus((String) row.get("RELAY_STATUS"));
		relaySetting.setStartWeekDays((String) row.get("START_WEEKDAYS"));
		relaySetting.setEndWeekDays((String) row.get("END_WEEKDAYS"));
		relaySetting.setStartTimer((String) row.get("START_TIMER"));
		relaySetting.setEndTimer((String) row.get("END_TIMER"));
		relaySetting.setDelay((String) row.get("DELAY"));
		relaySetting.setImpulseMode(row.get("MODE").toString().equals("Y"));
		relaySetting.setRelayEnabled(row.get("RELAY_ENABLED").equals("Y"));
		relaySetting.setTimerEnabled(row.get("TIMER_ENABLED").toString().equals("Y"));
		relaySetting.setDelayEnabled(row.get("DELAY_ENABLED").toString().equals("Y"));
		relaySetting.setCreationDate((String) row.get("CREATION_DATE"));
		relaySetting.setLastUpdateDate((String) row.get("LAST_UPDATE_DATE"));

		return relaySetting;
	}

	public ProductControlSetting mapProductControlSetting(Map<String, Object> row) {

		ProductControlSetting productControlSetting = new ProductControlSetting();
		productControlSetting.setUserId((Integer) row.get("USER_ID"));
		productControlSetting.setAccess(row.get("HAS_ACCESS").equals("Y"));
		productControlSetting.setCallAccess(row.get("CALL_ACCESS").equals("Y"));
		productControlSetting.setCreationDate((String) row.get("CREATION_DATE"));
		productControlSetting.setLastUpdateDate((String) row.get("LAST_UPDATE_DATE"));

		return productControlSetting;
	}

	public ProductTriggerSetting mapProductTriggerSetting(Map<String, Object> row) {

		ProductTriggerSetting productTriggerSetting = new ProductTriggerSetting();
		productTriggerSetting.setTriggerId((Integer) row.get("TRIGGER_ID"));
		productTriggerSetting.setTriggerRelayId((Integer)row.get("TRIGGER_RELAY_ID"));
		productTriggerSetting.setTriggerEnabled(row.get("TRIGGER_ENABLED").equals("Y"));
		productTriggerSetting.setTriggerValue((String)row.get("TRIGGER_VALUE"));
		productTriggerSetting.setTriggerState((String)row.get("TRIGGER_STATE"));
		productTriggerSetting.setTriggerAction((String)row.get("TRIGGER_ACTION"));
		productTriggerSetting.setLastUpdateDate((String)row.get("LAST_UPDATE_DATE"));

		return productTriggerSetting;
	}
}
