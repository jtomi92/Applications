package com.jtech.apps.hcm.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import com.jtech.apps.hcm.service.ConsoleService;

@Controller
public class ConsoleController {

	@Autowired
	ConsoleService consoleService;

	private static final Logger logger = Logger.getLogger(ConsoleController.class);
	
	@RequestMapping(value = "/console", method = RequestMethod.GET)
	public ModelAndView onConsoleOpen(ModelMap model) {
		
		return consoleService.onConsoleOpen(model);
	}	

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Integer onRegisterProduct(@RequestBody RegisterForm registerForm) {
  
		return	consoleService.onRegisterProduct(registerForm.getUserId(), registerForm.getSerialNumber());
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Integer onUpdateProductName(@RequestBody ProductForm productForm) {
		
		logger.info(productForm.getSerialNumber() + " " + productForm.getUserId() + " " + productForm.getProductName());
 	
		return consoleService.onUpdateProductName(productForm.getUserId(), productForm.getSerialNumber(), productForm.getProductName());
	}
	
	@RequestMapping(value = "/productuser/add/{serial}/{username:.+}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Integer onInsertProductUser(@PathVariable("serial") String serialNumber, @PathVariable("username") String userName ) {
  
		logger.info(serialNumber + " " + userName);
		
		return consoleService.onInserProductUser(serialNumber, userName);
	}
	
	@RequestMapping(value = "/productuser/remove/{serial}/{username:.+}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Integer onRemoveProductUser(@PathVariable("serial") String serialNumber, @PathVariable("username") String userName ) {
  
		logger.info(serialNumber + " " + userName);
		
		return consoleService.onRemoveProductUser(serialNumber, userName);
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
		
		return consoleService.onUpdateProductUser(serialNumber, userName, productUserForm.getRelayAccess(), productUserForm.getCallAccess(), productUserForm.getPrivilige());
	}
	
	
	@RequestMapping(value = "/productsetting/update/{serial}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Integer onUpdateProductSettings(@PathVariable("serial") String serialNumber,  @RequestBody ProductSettingsForm productSettingsForm) {
		 
		 
		return consoleService.onUpdateProductSettings(serialNumber, productSettingsForm);
	}
	
	@RequestMapping(value = "/relay/{userid}/{serial}/{moduleid}/{relayid}/{status}", method = RequestMethod.POST )
	@ResponseBody
	public Integer onRelaySwitch(@PathVariable("userid") Integer userId, @PathVariable("serial") String serialNumber,@PathVariable("moduleid") String moduleId, @PathVariable("relayid") String relayId, @PathVariable("status") String status) {
	
		return consoleService.onRelaySwitch(userId, serialNumber,moduleId, relayId, status); 
	}
	
	@RequestMapping(value = "/device/update/{userid}/{serial}", method = RequestMethod.POST )
	@ResponseBody
	public Integer onUpdate(@PathVariable("userid") Integer userId, @PathVariable("serial") String serialNumber) {
	
		return consoleService.onUpdate(userId, serialNumber); 
	}
	
	@RequestMapping(value = "/device/restart/{userid}/{serial}", method = RequestMethod.POST )
	@ResponseBody
	public Integer onRestart(@PathVariable("userid") Integer userId, @PathVariable("serial") String serialNumber){
	
		return consoleService.onRestart(userId, serialNumber); 
	}
	
}
