package com.jtech.apps.hcm.controller;

import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jtech.apps.hcm.form.ProductForm;
import com.jtech.apps.hcm.form.ProductSettingsForm;
import com.jtech.apps.hcm.form.ProductUserForm;
import com.jtech.apps.hcm.form.RegisterForm;
import com.jtech.apps.hcm.form.UserForm;
import com.jtech.apps.hcm.service.WebAppService;
 


@Controller
public class WebAppController {

	@Autowired
	WebAppService webAppService;

	private static final Logger logger = Logger.getLogger(WebAppController.class);

	@RequestMapping(value = "/console", method = RequestMethod.GET)
	public ModelAndView onConsoleOpen(ModelMap model) {
		
		return webAppService.onConsoleOpen(model);
	}
	

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Integer onRegisterProduct(@RequestBody RegisterForm registerForm) {
  
		return	webAppService.onRegisterProduct(registerForm.getUserId(), registerForm.getSerialNumber());
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Integer onUpdateProductName(@RequestBody ProductForm productForm) {
		
		logger.info(productForm.getSerialNumber() + " " + productForm.getUserId() + " " + productForm.getProductName());
 	
		return webAppService.onUpdateProductName(productForm.getUserId(), productForm.getSerialNumber(), productForm.getProductName());
	}
	
	
	@RequestMapping(value = "/productuser/add/{serial}/{username:.+}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Integer onInsertProductUser(@PathVariable("serial") String serialNumber, @PathVariable("username") String userName ) {
  
		logger.info(serialNumber + " " + userName);
		
		return webAppService.onInserProductUser(serialNumber, userName);
	}
	
	@RequestMapping(value = "/productuser/remove/{serial}/{username:.+}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Integer onRemoveProductUser(@PathVariable("serial") String serialNumber, @PathVariable("username") String userName ) {
  
		logger.info(serialNumber + " " + userName);
		
		return webAppService.onRemoveProductUser(serialNumber, userName);
	}
	
	@RequestMapping(value = "/productuser/update/{serial}/{username:.+}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Integer onUpdateProductUser(@PathVariable("serial") String serialNumber, @PathVariable("username") String userName, @RequestBody ProductUserForm productUserForm  ) {
  
		logger.info(serialNumber + " " + userName);
		
		for (String relayAccess : productUserForm.getRelayAccess()) {
			logger.info("REL ACCESS=" + relayAccess);
		}
		for (String callAccess : productUserForm.getCallAccess()) {
			logger.info("CALL ACCESS=" + callAccess);
		}
		
		return webAppService.onUpdateProductUser(serialNumber, userName, productUserForm.getRelayAccess(), productUserForm.getCallAccess());
	}
	
	
	@RequestMapping(value = "/productsetting/update/{serial}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Integer onUpdateProductSettings(@PathVariable("serial") String serialNumber,  @RequestBody ProductSettingsForm productSettingsForm) {
		 
		 
		return webAppService.onUpdateProductSettings(serialNumber, productSettingsForm);
	}
	
	@RequestMapping(value = "/relay/{userid}/{serial}/{relayid}/{status}", method = RequestMethod.POST )
	@ResponseBody
	public Integer onRelaySwitch(@PathVariable("userid") Integer userId, @PathVariable("serial") String serialNumber, @PathVariable("relayid") String relayId, @PathVariable("status") String status) {
	
		return webAppService.onRelaySwitch(userId, serialNumber, relayId, status); 
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView onRegisterPageLoad(Map<String, Object> model) {
		
		return webAppService.onRegisterPageLoad(model); 
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST )
	public ModelAndView onRegisterUser(@Valid @ModelAttribute("userForm") UserForm userForm,
			BindingResult bindingResult, Map<String, Object> model) {
	
		return webAppService.onRegisterUser(userForm, bindingResult, model);
	}
	
	

}
