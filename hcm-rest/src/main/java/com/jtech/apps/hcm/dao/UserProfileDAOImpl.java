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

import com.jtech.apps.hcm.dao.interfaces.UserProfileDAO;
import com.jtech.apps.hcm.dao.mapper.UserProfileMapper;
import com.jtech.apps.hcm.model.UserProfile;
import com.jtech.apps.hcm.util.TimeUtil;

@Repository
public class UserProfileDAOImpl implements UserProfileDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	UserProfileMapper mapper = new UserProfileMapper();
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<UserProfile> getUserProfiles() {

		List<UserProfile> userProfiles = new LinkedList<UserProfile>();
		String sql = "SELECT up.*, g.GROUP_NAME FROM USER_PROFILES up, GROUPS g WHERE up.GROUP_ID = g.GROUP_ID;";
		List<Map<String, Object>> rows = new LinkedList<Map<String, Object>>();
		rows = jdbcTemplate.queryForList(sql);

		if (rows != null && !rows.isEmpty()) {
			for (Map<String, Object> row : rows) {
				UserProfile userProfile = new UserProfile();
				userProfile = mapper.mapUserProfile(row);
				userProfiles.add(userProfile);
			}
		}
		return userProfiles;
	}

	@Override
	public int updateUserProfile(UserProfile up) {

		String sql = "UPDATE USER_PROFILES SET "
				+ "USER_PASSWORD = :USER_PASSWORD, GROUP_ID = (SELECT GROUP_ID FROM GROUPS WHERE GROUP_NAME = :GROUP_NAME),"
				+ "PHONE_NUMBER = :PHONE_NUMBER, FIRST_NAME = :FIRST_NAME, LAST_NAME = :LAST_NAME," + "ADDRESS = :ADDRESS, " + "CITY = :CITY, " + "ENABLED = :ENABLED, "
				+ "LAST_UPDATE_DATE = :LAST_UPDATE_DATE " + "WHERE USER_ID = :USER_ID";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("USER_PASSWORD", up.getPassword());
		parameters.put("GROUP_NAME", up.getGroupName());
		parameters.put("PHONE_NUMBER", up.getPhoneNumber());
		parameters.put("FIRST_NAME", up.getFirstName());
		parameters.put("LAST_NAME", up.getLastName());
		parameters.put("ADDRESS", up.getAddress());
		parameters.put("CITY", up.getCity());
		parameters.put("ENABLED", up.isEnabled() ? "Y" : "N");
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		parameters.put("USER_ID", up.getUserId());

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int addUserProfile(UserProfile up) {

		String sql = "INSERT INTO USER_PROFILES (" + "USER_NAME, FIRST_NAME, LAST_NAME, GROUP_ID, " + "USER_PASSWORD, " + "PHONE_NUMBER, "
				+ "ADDRESS, " + "CITY, " + "ENABLED, " + "CREATION_DATE, " + "LAST_UPDATE_DATE) VALUES ("
				+ ":USER_NAME, :FIRST_NAME, :LAST_NAME, (SELECT GROUP_ID FROM GROUPS WHERE GROUP_NAME = :GROUP_NAME)," + ":USER_PASSWORD,"
				+ ":PHONE_NUMBER," + ":ADDRESS," + ":CITY," + ":ENABLED," + ":CREATION_DATE," + ":LAST_UPDATE_DATE)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("USER_NAME", up.getUserName());
		parameters.put("FIRST_NAME", up.getFirstName());
		parameters.put("LAST_NAME", up.getLastName());
		parameters.put("GROUP_NAME", up.getGroupName());
		parameters.put("USER_PASSWORD", up.getPassword());
		parameters.put("PHONE_NUMBER", up.getPhoneNumber());
		parameters.put("ADDRESS", up.getAddress());
		parameters.put("CITY", up.getCity());
		parameters.put("ENABLED", up.isEnabled() ? "Y" : "N");
		parameters.put("CREATION_DATE", TimeUtil.getTimeStamp());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public UserProfile getTestData() {

		UserProfile userProfile = new UserProfile();
		userProfile.setUserId(1);
		userProfile.setUserName("test user");
		userProfile.setPassword("test password");
		userProfile.setGroupName("USER");
		userProfile.setPhoneNumber("test phone number");
		userProfile.setCity("test city");
		userProfile.setAddress("test address");
		userProfile.setEnabled(true);
		userProfile.setCreationDate(TimeUtil.getTimeStamp());
		userProfile.setLastUpdateDate(TimeUtil.getTimeStamp());
		return userProfile;
	}

}
