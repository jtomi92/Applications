package com.jtech.apps.hcm.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.jtech.apps.hcm.dao.interfaces.UserProfileDAO;
import com.jtech.apps.hcm.model.UserProfile;

@Service
public class UserProfileService {
	@Autowired
	UserProfileDAO userProfileDAO;
	
	private static final Logger logger = Logger.getLogger(UserProfileService.class);

	public UserProfile getUserProfileByUserId(Integer userId) {

		List<UserProfile> userProfiles = userProfileDAO.getUserProfiles();

		for (UserProfile userProfile : userProfiles) {
			if (userProfile.getUserId() == userId) {
				return userProfile;
			}
		}
		return null;
	}
	
	public UserProfile getUserProfileByUserName(String userName) {

		List<UserProfile> userProfiles = userProfileDAO.getUserProfiles();
		for (UserProfile userProfile : userProfiles) {
			if (userProfile.getUserName().equals(userName)) {
				return userProfile;
			}
		}
		return null;
	}

	public List<UserProfile> getUserProfiles() {
		return userProfileDAO.getUserProfiles();
	}

	public int updateUserProfile(UserProfile userProfile) {
		return userProfileDAO.updateUserProfile(userProfile);
	}

	public int addUserProfile(UserProfile up) {

		List<UserProfile> userProfiles = getUserProfiles();

		for (UserProfile userProfile : userProfiles) {
			if (userProfile.getUserName().equals(up.getUserName())) {
				return 0;
			}
		}
		return userProfileDAO.addUserProfile(up);
	}

	public UserProfile getTestData() {
		return userProfileDAO.getTestData();
	}
}
