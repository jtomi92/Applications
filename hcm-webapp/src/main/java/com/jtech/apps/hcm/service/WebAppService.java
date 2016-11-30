package com.jtech.apps.hcm.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.jtech.apps.hcm.form.ProductSettingsForm;
import com.jtech.apps.hcm.form.RegisterForm;
import com.jtech.apps.hcm.form.UserForm;
import com.jtech.apps.hcm.helpers.RestUrls;
import com.jtech.apps.hcm.model.UserProduct;
import com.jtech.apps.hcm.model.UserProfile;
import com.jtech.apps.hcm.model.setting.ProductControlSetting;
import com.jtech.apps.hcm.model.setting.ProductUser;
import com.jtech.apps.hcm.model.setting.RelaySetting;
import com.jtech.apps.hcm.model.setting.Setting;

@Service
public class WebAppService {

	private static final Logger logger = Logger.getLogger(WebAppService.class);

	RestTemplate restTemplate = new RestTemplate();
	HttpHeaders headers = new HttpHeaders();
	HttpHeaders requestHeaders = new HttpHeaders();
	HttpEntity<?> httpEntity = new HttpEntity<Object>(requestHeaders);

	/**
	 * When /console is hit, we retrive userName from context, get UserProfile from the username,
	 * get UserProduct from the userid and populates the ModelAndView
	 * @param model
	 * @return ModelAndView
	 */
	public ModelAndView onConsoleOpen(ModelMap model) {
		ModelAndView modelAndView = new ModelAndView("console");
		
		// GET username from context
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		logger.debug("UserName=" + name);

		// GET userprofile of the username
		UserProfile userProfile = restTemplate.getForObject(RestUrls.REST_USER_PROFILE_GET_BY_NAME, UserProfile.class,
				name);
		
		// GET userproducts of the username by userid
		ParameterizedTypeReference<List<UserProduct>> typeRef = new ParameterizedTypeReference<List<UserProduct>>() {
		};
		ResponseEntity<List<UserProduct>> responseEntity = restTemplate.exchange(RestUrls.REST_USER_PRODUCT_GET_BY_ID,
				HttpMethod.GET, httpEntity, typeRef, userProfile.getUserId());
		List<UserProduct> userProducts = responseEntity.getBody();

		// This does not do anything, just goes through the user's products and logs information for verification
		StringBuffer sb = new StringBuffer("ASD");
		for (UserProduct userProduct : userProducts) {

			if (userProduct.isConnected()) {
				logger.info("CONNECTED");
			} else {
				logger.info("NOT CONNECTED");
			}

			sb.append("\nSERIAL=" + userProduct.getSerialNumber());
			List<Setting> settings = userProduct.getProductSettings();
			List<ProductUser> productusers = userProduct.getProductUsers();
			for (ProductUser productUser : productusers) {
				sb.append("\nUSER=" + productUser.getUserName() + " (" + productUser.getUserId() + ")");
			}
			List<RelaySetting> relaySettings = settings.get(0).getRelaySettings();
			for (RelaySetting relaySetting : relaySettings) {
				List<ProductControlSetting> productControlSettings = relaySetting.getProductControlSettings();
				for (ProductControlSetting productControlSetting : productControlSettings) {
					sb.append("\nPRIV/ REL=" + relaySetting.getRelayName() + " USER="
							+ productControlSetting.getUserId());
				}
			}
		}
		// logger.info(sb.toString());

		// populate modelAndView
		modelAndView.addObject("firstname", userProfile.getFirstName());
		modelAndView.addObject("userid", userProfile.getUserId());
		modelAndView.addObject("userProducts", userProducts);

		RegisterForm registerForm = new RegisterForm();
		modelAndView.addObject("registerForm", registerForm);

		return modelAndView;
	}
	
	/**
	 * Loads UserForm to /register page
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
		userProfile.setPassword(userForm.getPassword());
		userProfile.setGroupName("USER");
		userProfile.setEnabled(true);

		HttpEntity<Object> httpEntity = new HttpEntity<Object>(userProfile, requestHeaders);
		ResponseEntity<Integer> response = restTemplate.exchange(RestUrls.REST_ADD_USER_PROFILE, HttpMethod.PUT,
				httpEntity, Integer.class);

		if (response.getBody() == 0) {
			logger.error("Error registering user " + userForm.getEmail() + " already exists");
			modelAndView.addObject("error", "User already exists.");
			return modelAndView;
		}
		modelAndView.addObject("success", "Registration successful");

		return modelAndView;
	}

	/**
	 * Registers UserProduct by serialNumber
	 * @param userId
	 * @param serialNumber
	 * @return 1 onSuccess, 0 onFailure
	 */
	public Integer onRegisterProduct(String userId, String serialNumber) {
		return restTemplate.getForObject(RestUrls.REST_REGISTER_USER_PRODUCT, Integer.class, userId, serialNumber);
	}

	/**
	 * GETS UserProduct by serialNumber, updates it's name, pushes it back through the rest service
	 * @param userId
	 * @param serialNumber
	 * @param productName
	 * @return 1 onSuccess, 0 onFailure
	 */
	public Integer onUpdateProductName(String userId, String serialNumber, String productName) {
		
		UserProduct userProduct = restTemplate.getForObject(RestUrls.REST_USER_PRODUCT_GET_BY_SERIAL_NUMBER,
				UserProduct.class, serialNumber);
		userProduct.setName(productName);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(userProduct, requestHeaders);
		ResponseEntity<Integer> response = restTemplate.exchange(RestUrls.REST_UPDATE_USER_PRODUCT, HttpMethod.PUT,
				httpEntity, Integer.class);
		return response.getBody();
	}

	/**
	 * GETS the userProfile we'd like to add by userName, creates ProductUser object from it.
	 * GETS the userProduct by serialNumber, add the ProductUser to it and generate ProductControlSettings as well.
	 * Finally it updates the userProduct through the rest service. 
	 * @param serialNumber
	 * @param userName
	 * @return 1 onSuccess, 0 onFailure
	 */
	public Integer onInserProductUser(String serialNumber, String userName) {

		/* GET USERPROFILE */
		UserProfile userProfile = restTemplate.getForObject(RestUrls.REST_USER_PROFILE_GET_BY_NAME, UserProfile.class,
				userName);
		if (userProfile == null) {
			logger.info("Error: onInserProductUser - no userprofile found for " + userName);
			return 0;
		}
		/* CREATE PRODUCT USER FROM UP */
		ProductUser productUser = new ProductUser();
		productUser.setPrivilige("USER");
		productUser.setUserId(userProfile.getUserId());
		productUser.setSelected(false);
		productUser.setUserName(userName);

		logger.debug("ID=" + userProfile.getUserId());

		/* GET USERPRODUCT */
		UserProduct userProduct = restTemplate.getForObject(RestUrls.REST_USER_PRODUCT_GET_BY_SERIAL_NUMBER,
				UserProduct.class, serialNumber);
		List<ProductUser> productUsers = userProduct.getProductUsers();
		for (ProductUser pu : productUsers) {
			if (pu.getUserName().equals(productUser.getUserName())) {
				logger.info("ALREADY ADDED");
				return -1;
			}
		}
		userProduct.addProductUser(productUser);

		/* ADD PRODUCT CONTROL SETTING */
		ProductControlSetting productControlSetting = new ProductControlSetting();
		productControlSetting.setUserId(userProfile.getUserId());
		productControlSetting.setAccess(true);
		productControlSetting.setCallAccess(true);

		List<RelaySetting> relaySettings = userProduct.getProductSettings().get(0).getRelaySettings();
		for (RelaySetting relaySetting : relaySettings) {
			relaySetting.addProductControlSetting(productControlSetting);
		}
		userProduct.getProductSettings().get(0).setRelaySettings(relaySettings);

		HttpEntity<Object> httpEntity = new HttpEntity<Object>(userProduct, requestHeaders);
		ResponseEntity<Integer> response = restTemplate.exchange(RestUrls.REST_UPDATE_USER_PRODUCT, HttpMethod.PUT,
				httpEntity, Integer.class);

		return response.getBody();
	}

	/**
	 * GET the UserProfile of the userName we want to remove from the UserProduct identified by serialNumber
	 * Updates UserProduct through the rest service
	 * @param serialNumber
	 * @param userName
	 * @return 1 onSuccess, 0 onFailure
	 */
	public Integer onRemoveProductUser(String serialNumber, String userName) {

		/* GET USERPROFILE */
		UserProfile userProfile = restTemplate.getForObject(RestUrls.REST_USER_PROFILE_GET_BY_NAME, UserProfile.class,
				userName);
		if (userProfile == null) {
			logger.info("NO PROFILE");
			return 0;
		}

		logger.info("ID=" + userProfile.getUserId());
		/* GET USERPRODUCT */
		UserProduct userProduct = restTemplate.getForObject(RestUrls.REST_USER_PRODUCT_GET_BY_SERIAL_NUMBER,
				UserProduct.class, serialNumber);
		for (ProductUser pu : userProduct.getProductUsers()) {
			if (pu.getUserName().equals(userName)) {
				userProduct.removeProductUser(pu);
			}
		}

		/* REMOVE PRODUCT CONTROL SETTING */
		ProductControlSetting productControlSetting = new ProductControlSetting();

		List<RelaySetting> relaySettings = userProduct.getProductSettings().get(0).getRelaySettings();
		for (RelaySetting relaySetting : relaySettings) {
			for (ProductControlSetting pcs : relaySetting.getProductControlSettings()) {
				if (pcs.getUserId().equals(userProfile.getUserId())) {
					productControlSetting = pcs;
					break;
				}
			}
			relaySetting.getProductControlSettings().remove(productControlSetting);
		}
		userProduct.getProductSettings().get(0).setRelaySettings(relaySettings);

		logger.info("OK");

		HttpEntity<Object> httpEntity = new HttpEntity<Object>(userProduct, requestHeaders);
		ResponseEntity<Integer> response = restTemplate.exchange(RestUrls.REST_UPDATE_USER_PRODUCT, HttpMethod.PUT,
				httpEntity, Integer.class);

		return response.getBody();
	}

	
	/**
	 * GET the UserProfile of the userName we want to update in the UserProduct identified by serialNumber
	 * Updates UserProduct through the rest service
	 * @param serialNumber
	 * @param userName
	 * @param relayAccess
	 * @param callAccess
	 * @return
	 */
	public Integer onUpdateProductUser(String serialNumber, String userName, List<String> relayAccess,
			List<String> callAccess) {

		/* GET USERPROFILE */
		UserProfile userProfile = restTemplate.getForObject(RestUrls.REST_USER_PROFILE_GET_BY_NAME, UserProfile.class,
				userName);
		if (userProfile == null) {
			logger.error("Profile does not exist");
			return 0;
		}

		logger.info("UserName=" + userProfile.getUserName() + " UserID=" + userProfile.getUserId());
		/* GET USERPRODUCT */
		UserProduct userProduct = restTemplate.getForObject(RestUrls.REST_USER_PRODUCT_GET_BY_SERIAL_NUMBER,
				UserProduct.class, serialNumber);

		List<RelaySetting> relaySettings = userProduct.getProductSettings().get(0).getRelaySettings();
		for (RelaySetting relaySetting : relaySettings) {
			List<ProductControlSetting> pcs = relaySetting.getProductControlSettings();
			for (ProductControlSetting productControlSetting : pcs) {
				if (productControlSetting.getUserId().equals(userProfile.getUserId())) {
					logger.info("Disabling access for " + relaySetting.getRelayName());
					productControlSetting.setAccess(false);
					productControlSetting.setCallAccess(false);
					for (String ra : relayAccess) {
						if (ra.equals(relaySetting.getRelayName())) {
							logger.info("Enabling relay access for " + relaySetting.getRelayName());
							productControlSetting.setAccess(true);
						}
					}
					for (String ca : callAccess) {
						if (ca.equals(relaySetting.getRelayName())) {
							logger.info("Enabling call access for " + relaySetting.getRelayName());
							productControlSetting.setCallAccess(true);
						}
					}
				}
			}
		}

		userProduct.getProductSettings().get(0).setRelaySettings(relaySettings);

		HttpEntity<Object> httpEntity = new HttpEntity<Object>(userProduct, requestHeaders);
		ResponseEntity<Integer> response = restTemplate.exchange(RestUrls.REST_UPDATE_USER_PRODUCT, HttpMethod.PUT,
				httpEntity, Integer.class);

		return response.getBody();
	}

	

	/**
	 * Get UserProduct by serialNumber, retrieves product information from ProductSettingsForm
	 * push data from form to UserProduct, updates DB through the rest service
	 * @param serialNumber
	 * @param psf
	 * @return
	 */
	public Integer onUpdateProductSettings(String serialNumber, ProductSettingsForm psf) {

		UserProduct userProduct = restTemplate.getForObject(RestUrls.REST_USER_PRODUCT_GET_BY_SERIAL_NUMBER,
				UserProduct.class, serialNumber);

		logger.info("onUpdateProductSettings " + serialNumber);

		List<RelaySetting> relaySettings = userProduct.getProductSettings().get(0).getRelaySettings();
		for (RelaySetting relaySetting : relaySettings) {
			for (int index = 0; index < psf.getRelayIds().size(); index++) {
				if (relaySetting.getRelayId().equals(Integer.parseInt(psf.getRelayIds().get(index)))) {

					logger.info("RelaySetting=" + relaySetting.getRelayId() + " psf=" + psf.getRelayIds().get(index));

					if (psf.getRelayNames().get(index) != null && psf.getRelayNames().get(index) != "") {
						relaySetting.setRelayName(psf.getRelayNames().get(index));
					}
					if (psf.getDelays().get(index) != null && psf.getDelays().get(index) != "") {
						relaySetting.setDelay(psf.getDelays().get(index));
					}
					if (psf.getImpulses().get(index) != null && psf.getImpulses().get(index) != "") {
						relaySetting.setImpulseMode(psf.getImpulses().get(index).equals("1"));
					}
					if (psf.getStartTimers().get(index) != null && psf.getStartTimers().get(index) != "") {
						relaySetting.setStartTimer(psf.getStartTimers().get(index));
					}
					if (psf.getEndTimers().get(index) != null && psf.getEndTimers().get(index) != "") {
						relaySetting.setEndTimer(psf.getEndTimers().get(index));
					}
					if (psf.getTimerEnabled().get(index) != null && psf.getTimerEnabled().get(index) != "") {
						relaySetting.setTimerEnabled(psf.getTimerEnabled().get(index).equals("1"));
					}
					if (psf.getStartWeekDays().get(index) != null && psf.getStartWeekDays().get(index).size() != 0) {
						StringBuffer sb = new StringBuffer();
						for (String weekday : psf.getStartWeekDays().get(index)) {
							sb.append(weekday + ",");
						}
						logger.info("START WEEKDAYS=" + sb.toString());
						relaySetting.setStartWeekDays(sb.toString());
					} else if (psf.getStartWeekDays().get(index) == null) {
						relaySetting.setStartWeekDays("");
					}
					if (psf.getEndWeekDays().get(index) != null && psf.getEndWeekDays().get(index).size() != 0) {
						StringBuffer sb = new StringBuffer();
						for (String weekday : psf.getEndWeekDays().get(index)) {
							sb.append(weekday + ",");
						}
						logger.info("END WEEKDAYS=" + sb.toString());
						relaySetting.setEndWeekDays(sb.toString());
					} else if (psf.getEndWeekDays().get(index) == null) {
						relaySetting.setEndWeekDays("");
					}
				}
			}
		}
		userProduct.getProductSettings().get(0).setRelaySettings(relaySettings);
		userProduct.setEdited(true);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(userProduct, requestHeaders);
		ResponseEntity<Integer> response = restTemplate.exchange(RestUrls.REST_UPDATE_USER_PRODUCT, HttpMethod.PUT,
				httpEntity, Integer.class);

		return response.getBody();
	}

	/**
	 * Switch relay of a product identified by serialnumber. RelayID specifies which relay to switch into status which is 0 or 1
	 * @param userId
	 * @param serialNumber
	 * @param relayId
	 * @param status
	 * @return
	 */
	public Integer onRelaySwitch(Integer userId, String serialNumber, String relayId, String status) {
		logger.info("SWITCHING " + serialNumber + " RelayId=" + relayId);
		return restTemplate.getForObject(RestUrls.REST_SWITCH_RELAY, Integer.class, userId, serialNumber, "1", relayId, status);
	}

}
