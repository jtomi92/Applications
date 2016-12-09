package com.jtech.apps.hcm.service;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.jtech.apps.hcm.form.UserForm;
import com.jtech.apps.hcm.helpers.RestUtils;
import com.jtech.apps.hcm.model.UserProfile;

@Service
public class UserRegistrationService {
	private static final Logger logger = Logger.getLogger(UserRegistrationService.class);
	private RestUtils restUtils = new RestUtils();


	/**
	 * Loads UserForm to /register page
	 * 
	 * @param model
	 * @return ModelAndView
	 */
	public ModelAndView onRegisterPageLoad(Map<String, Object> model) {
		ModelAndView modelAndView = new ModelAndView("register");
		UserForm userForm = new UserForm();
		modelAndView.addObject("userForm", userForm);
		return modelAndView;
	}

	/**
	 * Registers user, validates fields
	 * 
	 * @param userForm
	 * @param bindingResult
	 * @param model
	 * @return ModelAndView
	 */
	public ModelAndView onRegisterUser(UserForm userForm, BindingResult bindingResult, Map<String, Object> model) {

		ModelAndView modelAndView = new ModelAndView("register");
		modelAndView.addAllObjects(model);
		if (bindingResult.hasErrors()) {
			return modelAndView;
		}
		UserProfile userProfile = new UserProfile();
		userProfile.setFirstName(userForm.getFirstName());
		userProfile.setLastName(userForm.getLastName());
		userProfile.setUserName(userForm.getEmail());
		userProfile.setPhoneNumber(userForm.getPhone());
		userProfile.setPassword(userForm.getPassword());
		userProfile.setGroupName("USER");
		userProfile.setEnabled(true);
		
		int err = restUtils.addUserProfile(userProfile);

		if (err == 0) {
			logger.error("Error registering user " + userForm.getEmail() + " already exists");
			modelAndView.addObject("error", "User already exists.");
			return modelAndView;
		}
		modelAndView.addObject("success", "Registration successful");

		return modelAndView;
	}


}