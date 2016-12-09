package com.jtech.apps.hcm.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jtech.apps.hcm.form.UserForm;
import com.jtech.apps.hcm.service.UserRegistrationService;

@Controller
public class UserRegistrationController {

	@Autowired
	UserRegistrationService userRegistrationService;
 
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView onRegisterPageLoad(Map<String, Object> model) {
		
		return userRegistrationService.onRegisterPageLoad(model); 
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST )
	public ModelAndView onRegisterUser(@Valid @ModelAttribute("userForm") UserForm userForm,
			BindingResult bindingResult, Map<String, Object> model) {
	
		return userRegistrationService.onRegisterUser(userForm, bindingResult, model);
	}
	
}
