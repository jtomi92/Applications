package com.jtech.apps.hcm.service;

import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.jtech.apps.hcm.form.UserForm;
import com.jtech.apps.hcm.helpers.RestUtils;
import com.jtech.apps.hcm.model.UserProfile;
import com.mysql.jdbc.StringUtils;

@Service
public class UserProfileService {

	RestUtils restUtils = new RestUtils();

	public ModelAndView onMyProfileOpen(ModelMap model) {

		// GET username from context
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		// GET userprofile of the username
		UserProfile userProfile = restUtils.getUserProfileByUserName(userName);

		ModelAndView modelAndView = new ModelAndView("myaccount");
		modelAndView.addObject("firstName", userProfile.getFirstName());
		modelAndView.addObject("lastName", userProfile.getLastName());
		modelAndView.addObject("email", userProfile.getUserName());
		modelAndView.addObject("phone", userProfile.getPhoneNumber());
		modelAndView.addObject("address", userProfile.getAddress());
		modelAndView.addObject("city", userProfile.getCity());

		return modelAndView;
	}

	public ModelAndView onSaveMyInformation(UserForm userForm, BindingResult bindingResult, Map<String, Object> model) {

		ModelAndView modelAndView = new ModelAndView("myaccount");
		modelAndView.addAllObjects(model);
		modelAndView.addObject("firstName", userForm.getFirstName());
		modelAndView.addObject("lastName", userForm.getLastName());
		modelAndView.addObject("email", userForm.getEmail());
		modelAndView.addObject("phone", userForm.getPhone());
		modelAndView.addObject("address", userForm.getAddress());
		modelAndView.addObject("city", userForm.getCity());
		if (bindingResult.hasErrors()) {
			return modelAndView;
		}

		// GET username from context
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		// GET userprofile of the username
		UserProfile up = restUtils.getUserProfileByUserName(userName);

		UserProfile userProfile = new UserProfile();
		userProfile.setUserId(up.getUserId());
		userProfile.setFirstName(userForm.getFirstName());
		userProfile.setLastName(userForm.getLastName());
		userProfile.setUserName(userForm.getEmail());
		userProfile.setPhoneNumber(userForm.getPhone());
		userProfile.setAddress(userForm.getAddress());
		userProfile.setCity(userForm.getCity());
		userProfile.setGroupName(up.getGroupName());
		userProfile.setEnabled(true);

		if (!StringUtils.isNullOrEmpty(userForm.getOldPassword()) && userForm.getOldPassword().equals(up.getPassword())
				&& !StringUtils.isNullOrEmpty(userForm.getNewPassword())
				&& !StringUtils.isNullOrEmpty(userForm.getConfirmPassword())
				&& userForm.getNewPassword().equals(userForm.getConfirmPassword()) &&
				userForm.getNewPassword().length() >= 4) {
			userProfile.setPassword(userForm.getPassword());
			modelAndView.addObject("passwordsuccess", "Password successfully updated.");
		} else {
			userProfile.setPassword(up.getPassword());
			if (!StringUtils.isNullOrEmpty(userForm.getOldPassword())
					&& !userForm.getOldPassword().equals(up.getPassword())) {
				modelAndView.addObject("passworderror", "Old password is incorrect.");
			} else if (!StringUtils.isNullOrEmpty(userForm.getOldPassword())
					&& userForm.getOldPassword().equals(up.getPassword())
					&& !userForm.getNewPassword().equals(userForm.getConfirmPassword())) {
				modelAndView.addObject("passworderror", "New password doesn't match.");	
			} else if (!StringUtils.isNullOrEmpty(userForm.getOldPassword())){
				modelAndView.addObject("passworderror", "New password is incorrect.");	
			}
		}

		int err = restUtils.updateUserProfile(userProfile);

		if (err == 0) {
			// logger.error("Error registering user " + userForm.getEmail() + "
			// already exists");
			modelAndView.addObject("error", "Error during saving user data.");
			return modelAndView;
		}
		modelAndView.addObject("success", "User information saved.");

		if (!up.getUserName().equals(userForm.getEmail())){
			ModelAndView mav = new ModelAndView("login");
			return mav;
		}
		return modelAndView;

	}

}
