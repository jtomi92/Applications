package com.jtech.apps.hcm.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.jtech.apps.hcm.dao.interfaces.UserProductDAO;
import com.jtech.apps.hcm.dao.mapper.UserProductMapper;
import com.jtech.apps.hcm.model.UserProduct;
import com.jtech.apps.hcm.model.setting.InputSetting;
import com.jtech.apps.hcm.model.setting.ProductControlSetting;
import com.jtech.apps.hcm.model.setting.ProductTriggerSetting;
import com.jtech.apps.hcm.model.setting.ProductUser;
import com.jtech.apps.hcm.model.setting.RelaySetting;
import com.jtech.apps.hcm.model.setting.Setting;
import com.jtech.apps.hcm.util.TimeUtil;

@Repository
public class UserProductDAOImpl implements UserProductDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	UserProductMapper mapper = new UserProductMapper();

	private final Logger logger = Logger.getLogger(UserProductDAO.class);

	@Override
	public int addUserProduct(UserProduct userProduct) {

		String sql = "INSERT INTO USER_PRODUCTS ( SERIAL_NUMBER, NAME, PHONE_NUMBER, HOST1, "
				+ "PORT1, HOST2, PORT2, EDITED, CREATION_DATE, LAST_UPDATE_DATE) VALUES ( :SERIAL_NUMBER, :NAME,"
				+ ":PHONE_NUMBER, :HOST1, :PORT1, :HOST2, :PORT2, :EDITED, :CREATION_DATE, :LAST_UPDATE_DATE)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SERIAL_NUMBER", userProduct.getSerialNumber());
		parameters.put("NAME", userProduct.getName());
		parameters.put("PHONE_NUMBER", userProduct.getPhoneNumber());
		parameters.put("HOST1", userProduct.getPrimaryHost());
		parameters.put("PORT1", userProduct.getPrimaryPort());
		parameters.put("HOST2", userProduct.getSecondaryHost());
		parameters.put("PORT2", userProduct.getSecondaryPort());
		parameters.put("EDITED", "Y");
		parameters.put("CREATION_DATE", TimeUtil.getTimeStamp());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());

		for (Setting setting : userProduct.getProductSettings()) {
			addUserProductSetting(setting, userProduct.getSerialNumber());
		}

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		int err = namedParameterJdbcTemplate.update(sql, namedParameters);

		for (ProductUser productUser : userProduct.getProductUsers()) {
			err = addProductUser(productUser, userProduct.getSerialNumber());
		}

		return err;
	}

	public int addProductUser(ProductUser productUser, String serialNumber) {

		String sql = "INSERT INTO USER_PRODUCT_GROUPS (GROUP_ID, USER_ID, PRIVILIGE_ID, SELECTED) VALUES ((SELECT GROUP_ID FROM USER_PRODUCTS WHERE SERIAL_NUMBER = :SERIAL_NUMBER), (SELECT USER_ID FROM USER_PROFILES WHERE USER_NAME = :USER_NAME), (SELECT PRIVILIGE_ID FROM USER_PRODUCT_PRIVILIGES WHERE PRIVILIGE_NAME = :PRIVILIGE_NAME), :SELECTED)";

		logger.info("USERNAME=" + productUser.getUserName() + " (" + productUser.getUserId() + ")");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SERIAL_NUMBER", serialNumber);
		parameters.put("USER_NAME", productUser.getUserName());
		parameters.put("PRIVILIGE_NAME", productUser.getPrivilige());
		parameters.put("SELECTED", productUser.isSelected() ? "Y" : "N");

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	public int deleteProductUsers(String serialNumber) {

		String sql = "DELETE FROM USER_PRODUCT_GROUPS WHERE GROUP_ID = "
				+ "(SELECT GROUP_ID FROM USER_PRODUCTS WHERE SERIAL_NUMBER = :SERIAL_NUMBER)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SERIAL_NUMBER", serialNumber);

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int addUserProductSetting(Setting setting, String serialNumber) {

		String sql = "INSERT INTO USER_PRODUCT_SETTINGS (" + "SETTING_ID," + "SERIAL_NUMBER," + "SETTING_NAME,"
				+ "SELECTED," + "CREATION_DATE," + "LAST_UPDATE_DATE) VALUES " + "(:SETTING_ID," + ":SERIAL_NUMBER,"
				+ ":SETTING_NAME," + ":SELECTED," + ":CREATION_DATE," + ":LAST_UPDATE_DATE)";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SETTING_ID", setting.getSettingId());
		parameters.put("SERIAL_NUMBER", serialNumber);
		parameters.put("SETTING_NAME", setting.getSettingName());
		parameters.put("SELECTED", setting.isSelected() ? "Y" : "N");
		parameters.put("CREATION_DATE", TimeUtil.getTimeStamp());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());

		for (RelaySetting relaySetting : setting.getRelaySettings()) {
			addUserProductRelaySetting(relaySetting, serialNumber, setting.getSettingId());
		}
		for (InputSetting inputSetting : setting.getInputSettings()) {
			addUserProductInputSetting(inputSetting, serialNumber, setting.getSettingId());
		}

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int addUserProductRelaySetting(RelaySetting relaySetting, String serialNumber, Integer settingId) {

		String sql = "INSERT INTO USER_PRODUCT_RELAY_SETTINGS (" + "SERIAL_NUMBER," + "SETTING_ID," + "RELAY_ID,"
				+ "RELAY_NAME, RELAY_STATUS, START_WEEKDAYS, END_WEEKDAYS," + "START_TIMER," + "END_TIMER,"
				+ "DELAY, RELAY_ENABLED, DELAY_ENABLED," + "TIMER_ENABLED, MODE," + "CREATION_DATE,"
				+ "LAST_UPDATE_DATE) VALUES (:SERIAL_NUMBER," + ":SETTING_ID," + ":RELAY_ID,"
				+ ":RELAY_NAME, :RELAY_STATUS, :START_WEEKDAYS, :END_WEEKDAYS," + ":START_TIMER," + ":END_TIMER,"
				+ ":DELAY, " + ":RELAY_ENABLED, :DELAY_ENABLED,"
				+ ":TIMER_ENABLED, :MODE, :CREATION_DATE," + ":LAST_UPDATE_DATE)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SERIAL_NUMBER", serialNumber);
		parameters.put("SETTING_ID", settingId);
		parameters.put("RELAY_ID", relaySetting.getRelayId());
		parameters.put("RELAY_STATUS", relaySetting.getRelayStatus());
		parameters.put("RELAY_NAME", relaySetting.getRelayName());
		parameters.put("START_WEEKDAYS", relaySetting.getStartWeekDays());
		parameters.put("END_WEEKDAYS", relaySetting.getEndWeekDays());
		parameters.put("START_TIMER", relaySetting.getStartTimer());
		parameters.put("END_TIMER", relaySetting.getEndTimer());
		parameters.put("DELAY", relaySetting.getDelay());
		parameters.put("MODE", relaySetting.isImpulseMode() ? "Y" : "N");
		parameters.put("RELAY_ENABLED", relaySetting.isRelayEnabled() ? "Y" : "N");
		parameters.put("DELAY_ENABLED", relaySetting.isDelayEnabled() ? "Y" : "N");
		parameters.put("TIMER_ENABLED", relaySetting.isTimerEnabled() ? "Y" : "N");
		parameters.put("CREATION_DATE", TimeUtil.getTimeStamp());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());

		List<ProductControlSetting> productControlSettings = relaySetting.getProductControlSettings();
		for (ProductControlSetting productControlSetting : productControlSettings) {
			addUserProductControlSetting(productControlSetting, serialNumber, settingId, relaySetting.getRelayId());
		}

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int addUserProductInputSetting(InputSetting inputSetting, String serialNumber, Integer settingId) {

		String sql = "INSERT INTO USER_PRODUCT_INPUT_SETTINGS (" + "SERIAL_NUMBER," + "SETTING_ID," + "INPUT_ID,"
				+ "INPUT_NAME," + "START_TIMER," + "END_TIMER," + "TIMER_ENABLED," + "VALUE_POSTFIX," + "SAMPLE_RATE,"
				+ "CREATION_DATE," + "LAST_UPDATE_DATE) VALUES (" + ":SERIAL_NUMBER," + ":SETTING_ID," + ":INPUT_ID,"
				+ ":INPUT_NAME," + ":START_TIMER," + ":END_TIMER," + ":TIMER_ENABLED," + ":VALUE_POSTFIX,"
				+ ":SAMPLE_RATE," + ":CREATION_DATE," + ":LAST_UPDATE_DATE)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SERIAL_NUMBER", serialNumber);
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

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int addUserProductControlSetting(ProductControlSetting pcs, String serialNumber, Integer settingId,
			Integer relayId) {

		String sql = "INSERT INTO USER_PRODUCT_CONTROL_SETTINGS (" + "SERIAL_NUMBER," + "SETTING_ID," + "RELAY_ID,"
				+ "USER_ID," + "HAS_ACCESS," + "CALL_ACCESS," + "CREATION_DATE," + "LAST_UPDATE_DATE) VALUES ("
				+ ":SERIAL_NUMBER," + ":SETTING_ID," + ":RELAY_ID," + ":USER_ID," + ":HAS_ACCESS," + ":CALL_ACCESS,"
				+ ":CREATION_DATE," + ":LAST_UPDATE_DATE)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SERIAL_NUMBER", serialNumber);
		parameters.put("SETTING_ID", settingId);
		parameters.put("RELAY_ID", relayId);
		parameters.put("USER_ID", pcs.getUserId());
		parameters.put("HAS_ACCESS", pcs.isAccess() ? "Y" : "N");
		parameters.put("CALL_ACCESS", pcs.isCallAccess() ? "Y" : "N");
		parameters.put("CREATION_DATE", TimeUtil.getTimeStamp());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int addUserProductTriggerSetting(ProductTriggerSetting pts, String serialNumber, Integer settingId,
			Integer inputId) {

		String sql = "INSERT INTO USER_PRODUCT_TRIGGER_SETTINGS (" + "SERIAL_NUMBER," + "SETTING_ID," + "INPUT_ID,"
				+ "TRIGGER_ID," + "TRIGGER_RELAY_ID," + "TRIGGER_ENABLED," + "TRIGGER_VALUE," + "TRIGGER_STATE,"
				+ "TRIGGER_ACTION," + "LAST_UPDATE_DATE) VALUES (" + ":SERIAL_NUMBER," + ":SETTING_ID," + ":INPUT_ID,"
				+ ":TRIGGER_ID," + ":TRIGGER_RELAY_ID," + ":TRIGGER_ENABLED," + ":TRIGGER_VALUE," + ":TRIGGER_STATE,"
				+ ":TRIGGER_ACTION," + ":LAST_UPDATE_DATE)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SERIAL_NUMBER", serialNumber);
		parameters.put("SETTING_ID", settingId);
		parameters.put("INPUT_ID", inputId);
		parameters.put("TRIGGER_ID", pts.getTriggerId());
		parameters.put("TRIGGER_RELAY_ID", pts.getTriggerRelayId());
		parameters.put("TRIGGER_ENABLED", pts.isTriggerEnabled() ? "Y" : "N");
		parameters.put("TRIGGER_VALUE", pts.getTriggerValue());
		parameters.put("TRIGGER_STATE", pts.getTriggerState());
		parameters.put("TRIGGER_ACTION", pts.getTriggerAction());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public List<UserProduct> getUserProducts() {

		String sql = "SELECT * FROM USER_PRODUCTS";
		List<UserProduct> userProducts = new LinkedList<UserProduct>();
		List<Map<String, Object>> rows = new LinkedList<Map<String, Object>>();

		rows = jdbcTemplate.queryForList(sql);

		if (rows != null && !rows.isEmpty()) {
			for (Map<String, Object> row : rows) {

				UserProduct userProduct = new UserProduct();
				userProduct = mapper.mapUserProduct(row);

				userProduct.setProductSettings(getUserProductSettings(userProduct.getSerialNumber()));
				userProduct.setProductUsers(getProductUsers(userProduct.getSerialNumber()));

				userProducts.add(userProduct);
			}
		}

		return userProducts;
	}

	public List<ProductUser> getProductUsers(String serialNumber) {

		String sql = "SELECT PR.USER_ID, PR.USER_NAME, UPP.PRIVILIGE_NAME, UPG.SELECTED "
				+ "FROM USER_PRODUCTS UP, USER_PRODUCT_GROUPS UPG, USER_PRODUCT_PRIVILIGES UPP, USER_PROFILES PR "
				+ "WHERE UP.SERIAL_NUMBER = ? " + "AND UPG.GROUP_ID = UP.GROUP_ID "
				+ "AND UPP.PRIVILIGE_ID = UPG.PRIVILIGE_ID " + "AND PR.USER_ID = UPG.USER_ID";

		List<ProductUser> productUsers = new LinkedList<ProductUser>();

		List<Map<String, Object>> rows = new LinkedList<Map<String, Object>>();

		rows = jdbcTemplate.queryForList(sql, serialNumber);

		if (rows != null && !rows.isEmpty()) {
			for (Map<String, Object> row : rows) {

				ProductUser productUser = new ProductUser();
				productUser.setPrivilige((String) row.get("PRIVILIGE_NAME"));
				productUser.setUserName((String) row.get("USER_NAME"));
				productUser.setUserId((Integer) row.get("USER_ID"));
				productUser.setSelected(row.get("SELECTED").equals("Y"));
				productUsers.add(productUser);
			}
		}

		return productUsers;
	}

	@Override
	public List<Setting> getUserProductSettings(String serialNumber) {

		List<Setting> settings = new LinkedList<Setting>();

		String sql = "SELECT * FROM USER_PRODUCT_SETTINGS WHERE SERIAL_NUMBER = ?";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, serialNumber);
		for (Map<String, Object> row : rows) {

			Setting setting = new Setting();
			setting = mapper.mapSetting(row);
			setting.setInputSettings(getInputSettings(serialNumber, setting.getSettingId()));
			setting.setRelaySettings(getRelaySettings(serialNumber, setting.getSettingId()));
			settings.add(setting);

		}
		return settings;
	}

	@Override
	public List<RelaySetting> getRelaySettings(String serialNumber, Integer settingId) {

		List<RelaySetting> relaySettings = new LinkedList<RelaySetting>();
		String sql = "SELECT * FROM USER_PRODUCT_RELAY_SETTINGS WHERE SERIAL_NUMBER = ? AND SETTING_ID = ?";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, serialNumber, settingId);
		for (Map<String, Object> row : rows) {
			RelaySetting relaySetting = new RelaySetting();
			relaySetting = mapper.mapRelaySetting(row);
			relaySetting.setProductControlSettings(
					getUserProductControlSettings(serialNumber, settingId, relaySetting.getRelayId()));
			relaySettings.add(relaySetting);
		}
		return relaySettings;
	}

	@Override
	public List<InputSetting> getInputSettings(String serialNumber, Integer settingId) {

		List<InputSetting> inputSettings = new LinkedList<InputSetting>();
		String sql = "SELECT * FROM USER_PRODUCT_INPUT_SETTINGS WHERE SERIAL_NUMBER = ? AND SETTING_ID = ?";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, serialNumber, settingId);
		for (Map<String, Object> row : rows) {
			InputSetting inputSetting = new InputSetting();
			inputSetting = mapper.mapInputSetting(row);
			inputSetting.setProductTriggerSettings(
					getProductTriggetSetting(serialNumber, settingId, inputSetting.getInputId()));

			inputSettings.add(inputSetting);
		}
		return inputSettings;

	}

	@Override
	public List<ProductControlSetting> getUserProductControlSettings(String serialNumber, Integer settingId,
			Integer relayId) {

		List<ProductControlSetting> productControlSettings = new LinkedList<ProductControlSetting>();
		String sql = "SELECT * FROM USER_PRODUCT_CONTROL_SETTINGS WHERE SERIAL_NUMBER = ? AND SETTING_ID = ? AND RELAY_ID = ?";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, serialNumber, settingId, relayId);
		for (Map<String, Object> row : rows) {
			ProductControlSetting productControlSetting = new ProductControlSetting();
			productControlSetting = mapper.mapProductControlSetting(row);

			productControlSettings.add(productControlSetting);
		}
		return productControlSettings;
	}

	@Override
	public List<ProductTriggerSetting> getProductTriggetSetting(String serialNumber, Integer settingId,
			Integer inputId) {

		List<ProductTriggerSetting> productTriggerSettings = new LinkedList<ProductTriggerSetting>();
		String sql = "SELECT * FROM USER_PRODUCT_TRIGGER_SETTINGS WHERE SERIAL_NUMBER = ? AND SETTING_ID = ? AND INPUT_ID = ?";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, serialNumber, settingId, inputId);
		for (Map<String, Object> row : rows) {
			ProductTriggerSetting productTriggerSetting = new ProductTriggerSetting();
			productTriggerSetting = mapper.mapProductTriggerSetting(row);
			productTriggerSettings.add(productTriggerSetting);
		}
		return productTriggerSettings;
	}

	@Override
	public int updateUserProduct(UserProduct up) {

		String sql = "UPDATE USER_PRODUCTS SET NAME = :NAME, PHONE_NUMBER = :PHONE_NUMBER," + "HOST1 = :HOST1, "
				+ "PORT1 = :PORT1, HOST2 = :HOST2, PORT2 = :PORT2, EDITED = :EDITED, LAST_UPDATE_DATE = :LAST_UPDATE_DATE "
				+ "WHERE SERIAL_NUMBER = :SERIAL_NUMBER";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("NAME", up.getName());
		parameters.put("PHONE_NUMBER", up.getPhoneNumber());
		parameters.put("HOST1", up.getPrimaryHost());
		parameters.put("PORT1", up.getPrimaryPort());
		parameters.put("HOST2", up.getSecondaryHost());
		parameters.put("PORT2", up.getSecondaryPort());
		parameters.put("EDITED", up.isEdited() ? "Y" : "N");
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		parameters.put("SERIAL_NUMBER", up.getSerialNumber());

		deleteProductUsers(up.getSerialNumber());
		for (ProductUser productUser : up.getProductUsers()) {
			addProductUser(productUser, up.getSerialNumber());
		}

		for (Setting setting : up.getProductSettings()) {
			updateUserProductSetting(setting, up.getSerialNumber());
		}

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int updateUserProductSetting(Setting setting, String serialNumber) {

		String sql = "UPDATE USER_PRODUCT_SETTINGS SET SETTING_NAME = :SETTING_NAME, SELECTED = :SELECTED, LAST_UPDATE_DATE = :LAST_UPDATE_DATE WHERE SERIAL_NUMBER = :SERIAL_NUMBER AND SETTING_ID = :SETTING_ID";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SETTING_NAME", setting.getSettingName());
		parameters.put("SELECTED", setting.isSelected() ? "Y" : "N");
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		parameters.put("SERIAL_NUMBER", serialNumber);
		parameters.put("SETTING_ID", setting.getSettingId());

		for (RelaySetting relaySetting : setting.getRelaySettings()) {
			updateUserProductRelaySetting(relaySetting, serialNumber, setting.getSettingId());
		}

		for (InputSetting inputSetting : setting.getInputSettings()) {
			updateUserProductInputSetting(inputSetting, serialNumber, setting.getSettingId());
		}

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int updateUserProductRelaySetting(RelaySetting rs, String serialNumber, Integer settingId) {

		String sql = "UPDATE USER_PRODUCT_RELAY_SETTINGS SET "
				+ "RELAY_NAME = :RELAY_NAME, RELAY_STATUS = :RELAY_STATUS, START_WEEKDAYS = :START_WEEKDAYS, END_WEEKDAYS = :END_WEEKDAYS,"
				+ "START_TIMER = :START_TIMER, " + "END_TIMER = :END_TIMER, " + "DELAY = :DELAY, " + "MODE = :MODE, "
				+ "RELAY_ENABLED = :RELAY_ENABLED, DELAY_ENABLED = :DELAY_ENABLED, "
				+ "TIMER_ENABLED = :TIMER_ENABLED, " + "LAST_UPDATE_DATE = :LAST_UPDATE_DATE "
				+ "WHERE SERIAL_NUMBER = :SERIAL_NUMBER " + "AND SETTING_ID = :SETTING_ID "
				+ "AND RELAY_ID = :RELAY_ID";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("RELAY_NAME", rs.getRelayName());
		parameters.put("RELAY_STATUS", rs.getRelayStatus());
		parameters.put("START_WEEKDAYS", rs.getStartWeekDays());
		parameters.put("END_WEEKDAYS", rs.getEndWeekDays());
		parameters.put("START_TIMER", rs.getStartTimer());
		parameters.put("END_TIMER", rs.getEndTimer());
		parameters.put("DELAY", rs.getDelay());
		parameters.put("MODE", rs.isImpulseMode() ? "Y" : "N");
		parameters.put("RELAY_ENABLED", rs.isRelayEnabled() ? "Y" : "N");
		parameters.put("DELAY_ENABLED", rs.isDelayEnabled() ? "Y" : "N");
		parameters.put("TIMER_ENABLED", rs.isTimerEnabled() ? "Y" : "N");
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		parameters.put("SERIAL_NUMBER", serialNumber);
		parameters.put("SETTING_ID", settingId);
		parameters.put("RELAY_ID", rs.getRelayId());

		List<ProductControlSetting> productControlSettings = rs.getProductControlSettings();

		deleteUserProductControlSettings(serialNumber, settingId, rs.getRelayId());
		for (ProductControlSetting productControlSetting : productControlSettings) {
			addUserProductControlSetting(productControlSetting, serialNumber, settingId, rs.getRelayId());
		}

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);

	}

	@Override
	public int updateUserProductInputSetting(InputSetting is, String serialNumber, Integer settingId) {

		String sql = "UPDATE USER_PRODUCT_INPUT_SETTINGS SET " + "INPUT_NAME = :INPUT_NAME, "
				+ "START_TIMER = :START_TIMER, " + "END_TIMER = :END_TIMER, " + "TIMER_ENABLED = :TIMER_ENABLED, "
				+ "VALUE_POSTFIX = :VALUE_POSTFIX, " + "SAMPLE_RATE = :SAMPLE_RATE, "
				+ "LAST_UPDATE_DATE = :LAST_UPDATE_DATE " + "WHERE SERIAL_NUMBER = :SERIAL_NUMBER "
				+ "AND SETTING_ID = :SETTING_ID " + "AND INPUT_ID = :INPUT_ID";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("INPUT_NAME", is.getInputName());
		parameters.put("START_TIMER", is.getStartTimer());
		parameters.put("END_TIMER", is.getEndTimer());
		parameters.put("TIMER_ENABLED", is.isTimerEnabled() ? "Y" : "N");
		parameters.put("VALUE_POSTFIX", is.getValuePostfix());
		parameters.put("SAMPLE_RATE", is.getSampleRate());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		parameters.put("SERIAL_NUMBER", serialNumber);
		parameters.put("SETTING_ID", settingId);
		parameters.put("INPUT_ID", is.getInputId());

		List<ProductTriggerSetting> productTriggerSettings = is.getProductTriggerSettings();

		deleteUserProductTriggerSettings(serialNumber, settingId, is.getInputId());
		for (ProductTriggerSetting productTriggerSetting : productTriggerSettings) {
			addUserProductTriggerSetting(productTriggerSetting, serialNumber, settingId, is.getInputId());
		}

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int deleteUserProductTriggerSettings(String serialNumber, Integer settingId, Integer inputId) {

		String sql = "DELETE FROM USER_PRODUCT_TRIGGER_SETTINGS WHERE SERIAL_NUMBER = :SERIAL_NUMBER AND"
				+ " SETTING_ID = :SETTING_ID AND INPUT_ID = :INPUT_ID";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SERIAL_NUMBER", serialNumber);
		parameters.put("SETTING_ID", settingId);
		parameters.put("INPUT_ID", inputId);

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int deleteUserProductControlSettings(String serialNumber, Integer settingId, Integer relayId) {

		String sql = "DELETE FROM USER_PRODUCT_CONTROL_SETTINGS WHERE SERIAL_NUMBER = :SERIAL_NUMBER AND SETTING_ID = :SETTING_ID AND  RELAY_ID = :RELAY_ID";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SERIAL_NUMBER", serialNumber);
		parameters.put("SETTING_ID", settingId);
		parameters.put("RELAY_ID", relayId);

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int updateUserProductTriggerSetting(ProductTriggerSetting pts, String serialNumber, Integer settingId,
			Integer inputId) {

		String sql = "UPDATE USER_PRODUCT_TRIGGER_SETTINGS SET " + "TRIGGER_RELAY_ID = :TRIGGER_RELAY_ID,"
				+ "TRIGGER_ENABLED = :TRIGGER_ENABLED," + "TRIGGER_VALUE = :TRIGGER_VALUE,"
				+ "TRIGGER_STATE = :TRIGGER_STATE," + "TRIGGER_ACTION = :TRIGGER_ACTION,"
				+ "LAST_UPDATE_DATE = :LAST_UPDATE_DATE " + "WHERE" + "SERIAL_NUMBER = :SERIAL_NUMBER AND"
				+ "SETTING_ID = :SETTING_ID AND" + "INPUT_ID = :INPUT_ID AND" + "TRIGGER_ID = :TRIGGER_ID";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("TRIGGER_RELAY_ID", pts.getTriggerRelayId());
		parameters.put("TRIGGER_ENABLED", pts.isTriggerEnabled() ? "Y" : "N");
		parameters.put("TRIGGER_VALUE", pts.getTriggerValue());
		parameters.put("TRIGGER_STATE", pts.getTriggerState());
		parameters.put("TRIGGER_ACTION", pts.getTriggerAction());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		parameters.put("SERIAL_NUMBER", serialNumber);
		parameters.put("SETTING_ID", settingId);
		parameters.put("INPUT_ID", inputId);
		parameters.put("TRIGGER_ID", pts.getTriggerId());

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int updateUserProductControlSetting(ProductControlSetting pcs, String serialNumber, Integer settingId,
			Integer relayId) {

		String sql = "UPDATE USER_PRODUCT_CONTROL_SETTINGS SET " + "HAS_ACCESS = :HAS_ACCESS,"
				+ "CALL_ACCESS = :CALL_ACCESS," + "LAST_UPDATE_DATE = :LAST_UPDATE_DATE" + "WHERE"
				+ "SERIAL_NUMBER = :SERIAL_NUMBER AND" + "SETTING_ID = :SETTING_ID AND" + "RELAY_ID = :RELAY_ID AND"
				+ "USER_ID = :USER_ID";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SMS_ACCESS", pcs.isAccess() ? "Y" : "N");
		parameters.put("CALL_ACCESS", pcs.isCallAccess() ? "Y" : "N");
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		parameters.put("SERIAL_NUMBER", serialNumber);
		parameters.put("SETTING_ID", settingId);
		parameters.put("RELAY_ID", relayId);
		parameters.put("USER_ID", pcs.getUserId());

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

}
