package com.jtech.apps.hcm.dao.mapper;

import java.util.Map;

import com.jtech.apps.hcm.model.UserProfile;

public class UserProfileMapper {

	public UserProfile mapUserProfile(Map<String, Object> row) {
		UserProfile userProfile = new UserProfile();
		userProfile.setUserId((Integer) row.get("USER_ID"));
		userProfile.setUserName((String) row.get("USER_NAME"));
		userProfile.setGroupName((String)row.get("GROUP_NAME"));
		userProfile.setPassword((String) row.get("USER_PASSWORD"));
		userProfile.setFirstName((String) row.get("FIRST_NAME"));
		userProfile.setLastName((String) row.get("LAST_NAME"));
		userProfile.setPhoneNumber((String)row.get("PHONE_NUMBER"));
		userProfile.setAddress((String) row.get("ADDRESS"));
		userProfile.setCity((String) row.get("CITY"));
		userProfile.setEnabled(row.get("ENABLED").equals("Y"));
		userProfile.setCreationDate((String) row.get("CREATION_DATE"));
		userProfile.setLastUpdateDate((String) row.get("LAST_UPDATE_DATE"));
		return userProfile;
	}

}
