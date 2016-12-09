package com.jtech.apps.hcm.service;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.jtech.apps.hcm.form.ContactForm;
import com.jtech.apps.hcm.helpers.RestUtils;
import com.jtech.apps.hcm.model.UserProfile;
import com.mysql.jdbc.StringUtils;

@Service
public class ContactService {

	RestUtils restUtils = new RestUtils();

	public ModelAndView onContactRequest(Map<String, Object> model) {
		ModelAndView modelAndView = new ModelAndView("contact");
		

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		if (!StringUtils.isNullOrEmpty(userName)) {
			UserProfile userProfile = restUtils.getUserProfileByUserName(userName);

			if (userProfile != null) {
				modelAndView.addObject("firstname", userProfile.getFirstName());
			}
		}
		ContactForm contactForm = new ContactForm();
		modelAndView.addObject("contactForm", contactForm);
		
		return modelAndView;
	}

	public ModelAndView onContactSubmit(ContactForm contactForm, BindingResult bindingResult) {	
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("contact");
			return modelAndView;
		}
		
		ModelAndView modelAndView = new ModelAndView("contact-success");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		if (!StringUtils.isNullOrEmpty(userName)) {
			UserProfile userProfile = restUtils.getUserProfileByUserName(userName);

			if (userProfile != null) {
				modelAndView.addObject("firstname", userProfile.getFirstName());
			}
		}

		return modelAndView;
	}
}
