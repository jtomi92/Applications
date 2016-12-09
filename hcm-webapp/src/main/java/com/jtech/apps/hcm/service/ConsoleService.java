package com.jtech.apps.hcm.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.jtech.apps.hcm.form.ProductSettingsForm;
import com.jtech.apps.hcm.form.RegisterForm;
import com.jtech.apps.hcm.helpers.RestUtils;
import com.jtech.apps.hcm.model.UserProduct;
import com.jtech.apps.hcm.model.UserProfile;
import com.jtech.apps.hcm.model.setting.ProductControlSetting;
import com.jtech.apps.hcm.model.setting.ProductUser;
import com.jtech.apps.hcm.model.setting.RelaySetting;
import com.jtech.apps.hcm.model.setting.Setting;
import com.mysql.jdbc.StringUtils;

@Service
public class ConsoleService {

	private static final Logger logger = Logger.getLogger(ConsoleService.class);
	private RestUtils restUtils = new RestUtils();

	/**
	 * When /console is hit, we retrive userName from context, get UserProfile
	 * from the username, get UserProduct from the userid and populates the
	 * ModelAndView
	 * 
	 * @param model
	 * @return ModelAndView
	 */
	public ModelAndView onConsoleOpen(ModelMap model) {
		ModelAndView modelAndView = new ModelAndView("console");

		// GET username from context
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		String productPrivilige = "";

		// GET userprofile of the username
		UserProfile userProfile = restUtils.getUserProfileByUserName(userName);

		// GET userproducts of the username by userid
		List<UserProduct> userProducts = restUtils.getUserProductsByUserId(userProfile.getUserId());

		for (UserProduct userProduct : userProducts) {

			List<RelaySetting> relaySettingsToRemove = new LinkedList<RelaySetting>();

			List<Setting> settings = userProduct.getProductSettings();
			List<ProductUser> productusers = userProduct.getProductUsers();
			for (ProductUser productUser : productusers) {
				if (productUser.getUserId().equals(userProfile.getUserId())) {
					productPrivilige = productUser.getPrivilige();
				}
			}
			List<RelaySetting> relaySettings = settings.get(0).getRelaySettings();
			for (RelaySetting relaySetting : relaySettings) {

				if (!relaySetting.isRelayEnabled()) {
					relaySettingsToRemove.add(relaySetting);
				} else {
					List<ProductControlSetting> productControlSettings = relaySetting.getProductControlSettings();
					for (ProductControlSetting productControlSetting : productControlSettings) {
						if (productControlSetting.getUserId().equals(userProfile.getUserId())
								&& !productControlSetting.isAccess()) {
							// relaySettingsToRemove.add(relaySetting);
						}

						if (productControlSetting.getUserId().equals(userProfile.getUserId())) {
							// relaySettingsToRemove.add(relaySetting);
							logger.info("RELAY=" + relaySetting.getRelayName());
						}
					}
				}
			}
			relaySettings.removeAll(relaySettingsToRemove);
		}

		// populate modelAndView
		modelAndView.addObject("firstname", userProfile.getFirstName());
		modelAndView.addObject("userid", userProfile.getUserId());
		modelAndView.addObject("privilige", productPrivilige);
		modelAndView.addObject("userProducts", userProducts);

		RegisterForm registerForm = new RegisterForm();
		modelAndView.addObject("registerForm", registerForm);

		return modelAndView;
	}

	/**
	 * Registers UserProduct by serialNumber
	 * 
	 * @param userId
	 * @param serialNumber
	 * @return 1 onSuccess, 0 onFailure
	 */
	public Integer onRegisterProduct(String userId, String serialNumber) {
		return restUtils.registerProduct(userId, serialNumber);
	}

	/**
	 * GETS UserProduct by serialNumber, updates it's name, pushes it back
	 * through the rest service
	 * 
	 * @param userId
	 * @param serialNumber
	 * @param productName
	 * @return 1 onSuccess, 0 onFailure
	 */
	public Integer onUpdateProductName(String userId, String serialNumber, String productName) {

		UserProduct userProduct = restUtils.getUserProductBySerialNumber(serialNumber);
		userProduct.setName(productName);
		return restUtils.updateUserProduct(userProduct);
	}

	/**
	 * GETS the userProfile we'd like to add by userName, creates ProductUser
	 * object from it. GETS the userProduct by serialNumber, add the ProductUser
	 * to it and generate ProductControlSettings as well. Finally it updates the
	 * userProduct through the rest service.
	 * 
	 * @param serialNumber
	 * @param userName
	 * @return 1 onSuccess, 0 onFailure
	 */
	public Integer onInserProductUser(String serialNumber, String userName) {

		/* GET USERPROFILE */
		UserProfile userProfile = restUtils.getUserProfileByUserName(userName);

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
		UserProduct userProduct = restUtils.getUserProductBySerialNumber(serialNumber);
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

		return restUtils.updateUserProduct(userProduct);
	}

	/**
	 * GET the UserProfile of the userName we want to remove from the
	 * UserProduct identified by serialNumber Updates UserProduct through the
	 * rest service
	 * 
	 * @param serialNumber
	 * @param userName
	 * @return 1 onSuccess, 0 onFailure
	 */
	public Integer onRemoveProductUser(String serialNumber, String userName) {

		/* GET USERPROFILE */
		UserProfile userProfile = restUtils.getUserProfileByUserName(userName);
		if (userProfile == null) {
			logger.info("NO PROFILE");
			return 0;
		}

		logger.info("ID=" + userProfile.getUserId());
		/* GET USERPRODUCT */
		UserProduct userProduct = restUtils.getUserProductBySerialNumber(serialNumber);
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

		return restUtils.updateUserProduct(userProduct);
	}

	/**
	 * GET the UserProfile of the userName we want to update in the UserProduct
	 * identified by serialNumber Updates UserProduct through the rest service
	 * 
	 * @param serialNumber
	 * @param userName
	 * @param relayAccess
	 * @param callAccess
	 * @return
	 */
	public Integer onUpdateProductUser(String serialNumber, String userName, List<String> relayAccess,
			List<String> callAccess, String privilige) {

		/* GET USERPROFILE */
		UserProfile userProfile = restUtils.getUserProfileByUserName(userName);
		if (userProfile == null) {
			logger.error("Profile does not exist");
			return 0;
		}

		logger.info("UserName=" + userProfile.getUserName() + " UserID=" + userProfile.getUserId());
		/* GET USERPRODUCT */
		UserProduct userProduct = restUtils.getUserProductBySerialNumber(serialNumber);

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

		if (!StringUtils.isNullOrEmpty(privilige) && (privilige.equals("USER") || privilige.equals("ADMIN"))) {
			for (ProductUser productUser : userProduct.getProductUsers()) {
				if (productUser.getUserName().equals(userName)) {
					productUser.setPrivilige(privilige);	
				}
			}
		}

		return restUtils.updateUserProduct(userProduct);
	}

	/**
	 * Get UserProduct by serialNumber, retrieves product information from
	 * ProductSettingsForm push data from form to UserProduct, updates DB
	 * through the rest service
	 * 
	 * @param serialNumber
	 * @param psf
	 * @return
	 */
	public Integer onUpdateProductSettings(String serialNumber, ProductSettingsForm psf) {

		UserProduct userProduct = restUtils.getUserProductBySerialNumber(serialNumber);

		logger.info("onUpdateProductSettings " + serialNumber);

		List<RelaySetting> relaySettings = userProduct.getProductSettings().get(0).getRelaySettings();
		for (RelaySetting relaySetting : relaySettings) {
			for (int index = 0; index < psf.getRelayIds().size(); index++) {
				if (relaySetting.getRelayId().equals(Integer.parseInt(psf.getRelayIds().get(index)))
						&& relaySetting.getModuleId().equals(Integer.parseInt(psf.getModuleIds().get(index)))) {

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

		return restUtils.updateUserProduct(userProduct);
	}

	/**
	 * Switch relay of a product identified by serialnumber. RelayID specifies
	 * which relay to switch into status which is 0 or 1
	 * 
	 * @param userId
	 * @param serialNumber
	 * @param relayId
	 * @param status
	 * @return
	 */
	public Integer onRelaySwitch(Integer userId, String serialNumber, String moduleId, String relayId, String status) {
		logger.info("SWITCHING " + serialNumber + " RelayId=" + relayId);
		return restUtils.onSwitchRelay(userId, serialNumber, moduleId, relayId, status);
	}

	public Integer onUpdate(Integer userId, String serialNumber) {
		logger.info("UPDATING DEVICE " + serialNumber);
		return restUtils.onUpdate(userId, serialNumber);
	}

	public Integer onRestart(Integer userId, String serialNumber) {
		logger.info("RESTARTING DEVICE " + serialNumber);
		return restUtils.onRestart(userId, serialNumber);
	}

}