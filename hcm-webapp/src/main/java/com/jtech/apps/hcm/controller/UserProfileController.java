package com.jtech.apps.hcm.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jtech.apps.hcm.form.UserForm;
import com.jtech.apps.hcm.service.UserProfileService;

@Controller
public class UserProfileController {
	
	@Autowired
	UserProfileService userProfileService;

	@RequestMapping(value = "/myaccount", method = RequestMethod.GET)
	public ModelAndView onMyAccountOpen(ModelMap model) {
		
		return userProfileService.onMyProfileOpen(model);
	}
	
	@RequestMapping(value = "/myaccount", method = RequestMethod.POST )
	public ModelAndView onSaveMyInformation(@Valid @ModelAttribute("userForm") UserForm userForm,
			BindingResult bindingResult, Map<String, Object> model) {
	
		return userProfileService.onSaveMyInformation(userForm, bindingResult, model);
	}
}
