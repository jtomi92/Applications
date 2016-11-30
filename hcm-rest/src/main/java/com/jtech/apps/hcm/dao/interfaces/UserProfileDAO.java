package com.jtech.apps.hcm.dao.interfaces;

import java.util.List;

import com.jtech.apps.hcm.model.UserProfile;

public interface UserProfileDAO {

	public List<UserProfile> getUserProfiles();
	public int updateUserProfile(UserProfile userProfile);
	public int addUserProfile(UserProfile userProfile);
	public UserProfile getTestData();
	
}
