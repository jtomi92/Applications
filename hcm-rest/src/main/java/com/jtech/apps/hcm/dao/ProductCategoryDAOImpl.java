package com.jtech.apps.hcm.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.jtech.apps.hcm.dao.interfaces.ProductCategoryDAO;
import com.jtech.apps.hcm.dao.mapper.ProductCategoryMapper;
import com.jtech.apps.hcm.model.ProductCategory;
import com.jtech.apps.hcm.model.setting.InputSetting;
import com.jtech.apps.hcm.model.setting.ProductControlSetting;
import com.jtech.apps.hcm.model.setting.ProductTriggerSetting;
import com.jtech.apps.hcm.model.setting.RelaySetting;
import com.jtech.apps.hcm.model.setting.Setting;
import com.jtech.apps.hcm.util.TimeUtil;

@Repository
public class ProductCategoryDAOImpl implements ProductCategoryDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	ProductCategoryMapper mapper = new ProductCategoryMapper();
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public ProductCategory getProductCategoryBy(String entity, String value) {

		List<ProductCategory> productCategories = getProductCategories();
		ProductCategory productCategory = new ProductCategory();

		for (int index = 0; index < productCategories.size(); index++) {
			switch (entity) {
			case "PRODUCT_NAME":
				if (productCategories.get(index).getProductName().equals(value))
					return productCategories.get(index);
				break;
			case "PRODUCT_ID":
				if (productCategories.get(index).getProductId() == Integer.parseInt(value))
					return productCategories.get(index);
			default:
				break;
			}
		}
		return productCategory;
	}

	public List<ProductCategory> getProductCategories() {

		List<ProductCategory> productCategories = new LinkedList<ProductCategory>();
		String sql = "SELECT * FROM PRODUCT_CATEGORIES";

		List<Map<String, Object>> rows = new LinkedList<Map<String, Object>>();

		rows = jdbcTemplate.queryForList(sql);

		if (rows != null && !rows.isEmpty()) {
			for (Map<String, Object> row : rows) {

				ProductCategory productCategory = new ProductCategory();
				productCategory = mapper.mapProductCategory(row);

				productCategory.setSettings(getSettings(productCategory.getProductId()));

				productCategories.add(productCategory);
			}
		}

		return productCategories;
	}

	public List<Setting> getSettings(Integer productId) {

		List<Setting> settings = new LinkedList<Setting>();

		String sql = "SELECT * FROM PRODUCT_DEFAULT_SETTINGS WHERE PRODUCT_ID = ?";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, productId);
		for (Map<String, Object> row : rows) {

			Setting setting = new Setting();
			setting = mapper.mapSetting(row);
			setting.setInputSettings(getInputSettings(productId, setting.getSettingId()));
			setting.setRelaySettings(getRelaySettings(productId, setting.getSettingId()));
			settings.add(setting);

		}
		return settings;

	}

	public List<InputSetting> getInputSettings(Integer productId, Integer settingId) {

		List<InputSetting> inputSettings = new LinkedList<InputSetting>();
		String sql = "SELECT * FROM PRODUCT_DEFAULT_INPUT_SETTINGS WHERE PRODUCT_ID = ? AND SETTING_ID = ?";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, productId, settingId);
		for (Map<String, Object> row : rows) {
			InputSetting inputSetting = new InputSetting();
			inputSetting = mapper.mapInputSetting(row);
			inputSetting.setProductTriggerSettings(new LinkedList<ProductTriggerSetting>());

			inputSettings.add(inputSetting);
		}
		return inputSettings;
	}

	public List<RelaySetting> getRelaySettings(Integer productId, Integer settingId) {

		List<RelaySetting> relaySettings = new LinkedList<RelaySetting>();
		String sql = "SELECT * FROM PRODUCT_DEFAULT_RELAY_SETTINGS WHERE PRODUCT_ID = ? AND SETTING_ID = ?";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, productId, settingId);
		for (Map<String, Object> row : rows) {
			RelaySetting relaySetting = new RelaySetting();
			relaySetting = mapper.mapRelaySetting(row);
			relaySetting.setProductControlSettings(new LinkedList<ProductControlSetting>());

			relaySettings.add(relaySetting);
		}
		return relaySettings;
	}

	public int updateProductCategory(ProductCategory pc) {

		String sql = "UPDATE PRODUCT_CATEGORIES SET " + "PRODUCT_NAME = :PRODUCT_NAME, RELAY_COUNT = :RELAY_COUNT, "
				+ "INPUT_COUNT = :INPUT_COUNT, " + "HOST1 = :HOST1, "
				+ "PORT1 = :PORT1, HOST2 = :HOST2, PORT2 = :PORT2, LAST_UPDATE_DATE = :LAST_UPDATE_DATE "
				+ "WHERE PRODUCT_ID = :PRODUCT_ID";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("PRODUCT_NAME", pc.getProductName());
		parameters.put("RELAY_COUNT", pc.getRelayCount());
		parameters.put("INPUT_COUNT", pc.getInputCount());
		parameters.put("HOST1", pc.getPrimaryHost());
		parameters.put("PORT1", pc.getPrimaryPort());
		parameters.put("HOST2", pc.getSecondaryHost());
		parameters.put("PORT2", pc.getSecondaryPort());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		parameters.put("PRODUCT_ID", pc.getProductId());

		for (int index = 0; index < pc.getSettings().size(); index++) {
			updateSetting(pc.getSettings().get(index), pc.getProductId());
		}

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	public int updateSetting(Setting st, Integer productId) {

		String sql = "UPDATE PRODUCT_DEFAULT_SETTINGS SET SETTING_NAME = :SETTING_NAME, SELECTED = :SELECTED, LAST_UPDATE_DATE = :LAST_UPDATE_DATE WHERE PRODUCT_ID = :PRODUCT_ID AND SETTING_ID = :SETTING_ID";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SETTING_NAME", st.getSettingName());
		parameters.put("SELECTED", st.isSelected() ? "Y" : "N");
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		parameters.put("PRODUCT_ID", productId);
		parameters.put("SETTING_ID", st.getSettingId());

		for (int index = 0; index < st.getRelaySettings().size(); index++) {
			updateRelaySetting(st.getRelaySettings().get(index), productId, st.getSettingId());
		}

		for (int index = 0; index < st.getInputSettings().size(); index++) {
			updateInputSetting(st.getInputSettings().get(index), productId, st.getSettingId());
		}

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	public int updateRelaySetting(RelaySetting rs, Integer productId, Integer settingId) {

		String sql = "UPDATE PRODUCT_DEFAULT_RELAY_SETTINGS SET "
				+ "RELAY_NAME = :RELAY_NAME, RELAY_STATUS = :RELAY_STATUS, START_WEEKDAYS = :START_WEEKDAYS, END_WEEKDAYS = :END_WEEKDAYS,"
				+ "START_TIMER = :START_TIMER, " + "END_TIMER = :END_TIMER, "
				+ "DELAY = :DELAY, RELAY_ENABLED = :RELAY_ENABLED,"
				+ "DELAY_ENABLED = :DELAY_ENABLED, " + "TIMER_ENABLED = :TIMER_ENABLED, MODE = :MODE, "
				+ "LAST_UPDATE_DATE = :LAST_UPDATE_DATE " + "WHERE PRODUCT_ID = :PRODUCT_ID "
				+ "AND SETTING_ID = :SETTING_ID " + "AND RELAY_ID = :RELAY_ID";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("RELAY_NAME", rs.getRelayName());
		parameters.put("RELAY_STATUS", rs.getRelayStatus());
		parameters.put("START_WEEKDAYS", rs.getStartWeekDays());
		parameters.put("END_WEEKDAYS", rs.getEndWeekDays());
		parameters.put("START_TIMER", rs.getStartTimer());
		parameters.put("END_TIMER", rs.getEndTimer());
		parameters.put("DELAY", rs.isRelayEnabled());
		parameters.put("RELAY_ENABLED", rs.isDelayEnabled() ? "Y" : "N");
		parameters.put("DELAY_ENABLED", rs.isDelayEnabled() ? "Y" : "N");
		parameters.put("TIMER_ENABLED", rs.isTimerEnabled() ? "Y" : "N");
		parameters.put("MODE", rs.isImpulseMode() ? "Y" : "N");
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		parameters.put("PRODUCT_ID", productId);
		parameters.put("SETTING_ID", settingId);
		parameters.put("RELAY_ID", rs.getRelayId());

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	public int updateInputSetting(InputSetting is, Integer productId, Integer settingId) {

		String sql = "UPDATE PRODUCT_DEFAULT_INPUT_SETTINGS SET " + "INPUT_NAME = :INPUT_NAME, "
				+ "START_TIMER = :START_TIMER, " + "END_TIMER = :END_TIMER, " + "TIMER_ENABLED = :TIMER_ENABLED, "
				+ "VALUE_POSTFIX = :VALUE_POSTFIX, " + "SAMPLE_RATE = :SAMPLE_RATE, "
				+ "LAST_UPDATE_DATE = :LAST_UPDATE_DATE " + "WHERE PRODUCT_ID = :PRODUCT_ID "
				+ "AND SETTING_ID = :SETTING_ID " + "AND INPUT_ID = :INPUT_ID";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("INPUT_NAME", is.getInputName());
		parameters.put("START_TIMER", is.getStartTimer());
		parameters.put("END_TIMER", is.getEndTimer());
		parameters.put("TIMER_ENABLED", is.isTimerEnabled() ? "Y" : "N");
		parameters.put("VALUE_POSTFIX", is.getValuePostfix());
		parameters.put("SAMPLE_RATE", is.getSampleRate());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		parameters.put("PRODUCT_ID", productId);
		parameters.put("SETTING_ID", settingId);
		parameters.put("INPUT_ID", is.getInputId());

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	public int addProductCategory(ProductCategory productCategory) {

		List<ProductCategory> productCategories = getProductCategories();

		for (int index = 0; index < productCategories.size(); index++) {
			if (productCategories.get(index).getProductId().equals(productCategory.getProductId()))
				return 0;
		}

		String sql = "INSERT INTO PRODUCT_CATEGORIES ( " + "PRODUCT_ID, " + "PRODUCT_NAME," + "RELAY_COUNT,"
				+ "INPUT_COUNT, " + "HOST1, " + "PORT1, HOST2, PORT2, CREATION_DATE, " + "LAST_UPDATE_DATE) "
				+ "VALUES (" + ":PRODUCT_ID," + ":PRODUCT_NAME," + ":RELAY_COUNT," + ":INPUT_COUNT," + ":HOST1, "
				+ ":PORT1, :HOST2, :PORT2," + ":CREATION_DATE," + ":LAST_UPDATE_DATE)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("PRODUCT_ID", productCategory.getProductId());
		parameters.put("PRODUCT_NAME", productCategory.getProductName());
		parameters.put("RELAY_COUNT", productCategory.getRelayCount());
		parameters.put("INPUT_COUNT", productCategory.getInputCount());
		parameters.put("HOST1", productCategory.getPrimaryHost());
		parameters.put("PORT1", productCategory.getPrimaryPort());
		parameters.put("HOST2", productCategory.getSecondaryHost());
		parameters.put("PORT2", productCategory.getSecondaryPort());

		parameters.put("CREATION_DATE", TimeUtil.getTimeStamp());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());

		for (int index = 0; index < productCategory.getSettings().size(); index++) {
			addSetting(productCategory.getSettings().get(index), productCategory.getProductId());
		}

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);

	}

	public int addSetting(Setting setting, Integer productId) {

		String sql = "INSERT INTO PRODUCT_DEFAULT_SETTINGS (" + "SETTING_ID," + "PRODUCT_ID," + "SETTING_NAME,"
				+ "SELECTED," + "CREATION_DATE," + "LAST_UPDATE_DATE) VALUES " + "(:SETTING_ID," + ":PRODUCT_ID,"
				+ ":SETTING_NAME," + ":SELECTED," + ":CREATION_DATE," + ":LAST_UPDATE_DATE)";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SETTING_ID", setting.getSettingId());
		parameters.put("PRODUCT_ID", productId);
		parameters.put("SETTING_NAME", setting.getSettingName());
		parameters.put("SELECTED", setting.isSelected() ? "Y" : "N");
		parameters.put("CREATION_DATE", TimeUtil.getTimeStamp());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());

		for (int index = 0; index < setting.getRelaySettings().size(); index++) {
			addRelaySetting(setting.getRelaySettings().get(index), productId, setting.getSettingId());
		}
		for (int index = 0; index < setting.getInputSettings().size(); index++) {
			addInputSetting(setting.getInputSettings().get(index), productId, setting.getSettingId());
		}

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	public int addRelaySetting(RelaySetting relaySetting, Integer productId, Integer settingId) {
		String sql = "INSERT INTO PRODUCT_DEFAULT_RELAY_SETTINGS (" + "PRODUCT_ID," + "SETTING_ID," + "RELAY_ID,"
				+ "RELAY_NAME, RELAY_STATUS, START_WEEKDAYS, END_WEEKDAYS," + "START_TIMER," + "END_TIMER," + "DELAY, RELAY_ENABLED,"
				+ "DELAY_ENABLED," + "TIMER_ENABLED," + "CREATION_DATE," + "LAST_UPDATE_DATE) VALUES (:PRODUCT_ID,"
				+ ":SETTING_ID," + ":RELAY_ID," + ":RELAY_NAME, :RELAY_STATUS, :START_WEEKDAYS, :END_WEEKDAYS," + ":START_TIMER," + ":END_TIMER,"
				+ ":DELAY, :RELAY_ENABLED," + ":DELAY_ENABLED," + ":TIMER_ENABLED,"
				+ ":CREATION_DATE," + ":LAST_UPDATE_DATE)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("PRODUCT_ID", productId);
		parameters.put("SETTING_ID", settingId);
		parameters.put("RELAY_ID", relaySetting.getRelayId());
		parameters.put("RELAY_NAME", relaySetting.getRelayName());
		parameters.put("RELAY_STATUS", relaySetting.getRelayStatus());
		parameters.put("START_WEEKDAYS", relaySetting.getStartWeekDays());
		parameters.put("END_WEEKDAYS", relaySetting.getEndWeekDays());
		parameters.put("START_TIMER", relaySetting.getStartTimer());
		parameters.put("END_TIMER", relaySetting.getEndTimer());
		parameters.put("DELAY", relaySetting.getDelay());
		parameters.put("RELAY_ENABLED", relaySetting.isRelayEnabled() ? "Y" : "N");
		parameters.put("DELAY_ENABLED", relaySetting.isDelayEnabled() ? "Y" : "N");
		parameters.put("TIMER_ENABLED", relaySetting.isTimerEnabled() ? "Y" : "N");
		parameters.put("CREATION_DATE", TimeUtil.getTimeStamp());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	public int addInputSetting(InputSetting inputSetting, Integer productId, Integer settingId) {

		String sql = "INSERT INTO PRODUCT_DEFAULT_INPUT_SETTINGS (" + "PRODUCT_ID," + "SETTING_ID," + "INPUT_ID,"
				+ "INPUT_NAME," + "START_TIMER," + "END_TIMER," + "TIMER_ENABLED," + "VALUE_POSTFIX," + "SAMPLE_RATE,"
				+ "CREATION_DATE," + "LAST_UPDATE_DATE) VALUES (" + ":PRODUCT_ID," + ":SETTING_ID," + ":INPUT_ID,"
				+ ":INPUT_NAME," + ":START_TIMER," + ":END_TIMER," + ":TIMER_ENABLED," + ":VALUE_POSTFIX,"
				+ ":SAMPLE_RATE," + ":CREATION_DATE," + ":LAST_UPDATE_DATE)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("PRODUCT_ID", productId);
		parameters.put("SETTING_ID", settingId);
		parameters.put("INPUT_ID", inputSetting.getInputId());
		parameters.put("INPUT_NAME", inputSetting.getInputName());
		parameters.put("START_TIMER", inputSetting.getStartTimer());
		parameters.put("END_TIMER", inputSetting.getEndTimer());
		parameters.put("TIMER_ENABLED", inputSetting.isTimerEnabled() ? "Y" : "N");
		parameters.put("VALUE_POSTFIX", inputSetting.getValuePostfix());
		parameters.put("SAMPLE_RATE", inputSetting.getSampleRate());
		parameters.put("CREATION_DATE", TimeUtil.getTimeStamp());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	public ProductCategory getTestData() {
		ProductCategory pc = new ProductCategory();
		pc.setProductId(1);
		pc.setProductName("test product 1");
		pc.setRelayCount(3);
		pc.setInputCount(2);
		pc.setPrimaryHost("jozsa-electronics.com");
		pc.setPrimaryPort("80");
		pc.setSecondaryHost("jozsa2-electronics.com");
		pc.setSecondaryPort("81");
		pc.setCreationDate(TimeUtil.getTimeStamp());
		pc.setLastUpdateDate(TimeUtil.getTimeStamp());

		Setting s1 = new Setting();
		s1.setSettingId(1);
		s1.setSettingName("test setting 1");
		s1.setSelected(true);
		s1.setCreationDate(TimeUtil.getTimeStamp());
		s1.setLastUpdateDate(TimeUtil.getTimeStamp());

		RelaySetting r1 = new RelaySetting();
		r1.setRelayId(1);
		r1.setRelayName("test relay 1");
		r1.setStartTimer("13:20");
		r1.setEndTimer("15:40");
		r1.setDelay("0");
		r1.setDelayEnabled(true);
		r1.setTimerEnabled(false);
		r1.setCreationDate(TimeUtil.getTimeStamp());
		r1.setLastUpdateDate(TimeUtil.getTimeStamp());

		RelaySetting r2 = new RelaySetting();
		r2.setRelayId(2);
		r2.setRelayName("test relay 2");
		r2.setStartTimer("13:20");
		r2.setEndTimer("15:40");
		r2.setDelay("0");
		r2.setDelayEnabled(true);
		r2.setTimerEnabled(false);
		r2.setCreationDate(TimeUtil.getTimeStamp());
		r2.setLastUpdateDate(TimeUtil.getTimeStamp());

		RelaySetting r3 = new RelaySetting();
		r3.setRelayId(3);
		r3.setRelayName("test relay 3");
		r3.setStartTimer("13:20");
		r3.setEndTimer("15:40");
		r3.setDelay("0");
		r3.setDelayEnabled(true);
		r3.setTimerEnabled(false);
		r3.setCreationDate(TimeUtil.getTimeStamp());
		r3.setLastUpdateDate(TimeUtil.getTimeStamp());

		InputSetting i1 = new InputSetting();
		i1.setInputId(1);
		i1.setInputName("test input 1");
		i1.setStartTimer("13:20");
		i1.setEndTimer("15:40");
		i1.setTimerEnabled(true);
		i1.setValuePostfix("Celsius");
		i1.setSampleRate("30");
		i1.setCreationDate(TimeUtil.getTimeStamp());
		i1.setLastUpdateDate(TimeUtil.getTimeStamp());

		InputSetting i2 = new InputSetting();
		i2.setInputId(2);
		i2.setInputName("test input 2");
		i2.setStartTimer("13:20");
		i2.setEndTimer("15:40");
		i2.setTimerEnabled(true);
		i2.setValuePostfix("Celsius");
		i2.setSampleRate("30");
		i2.setCreationDate(TimeUtil.getTimeStamp());
		i2.setLastUpdateDate(TimeUtil.getTimeStamp());

		List<RelaySetting> relaySettings = new LinkedList<RelaySetting>();
		relaySettings.add(r1);
		relaySettings.add(r2);
		relaySettings.add(r3);

		List<InputSetting> inputSettings = new LinkedList<InputSetting>();
		inputSettings.add(i1);
		inputSettings.add(i2);

		s1.setInputSettings(inputSettings);
		s1.setRelaySettings(relaySettings);

		Setting s2 = new Setting();
		s2.setSettingId(2);
		s2.setSettingName("test setting 2");
		s2.setSelected(false);
		s2.setCreationDate(TimeUtil.getTimeStamp());
		s2.setLastUpdateDate(TimeUtil.getTimeStamp());

		s2.setInputSettings(inputSettings);
		s2.setRelaySettings(relaySettings);

		List<Setting> settings = new LinkedList<Setting>();
		settings.add(s1);
		settings.add(s2);

		pc.setSettings(settings);
		return pc;
	}
}
