package com.jtech.apps.hcm.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtech.apps.hcm.dao.interfaces.UserProductDAO;
import com.jtech.apps.hcm.model.Connection;
import com.jtech.apps.hcm.model.ProductCategory;
import com.jtech.apps.hcm.model.RegisteredProduct;
import com.jtech.apps.hcm.model.UserProduct;
import com.jtech.apps.hcm.model.UserProfile;
import com.jtech.apps.hcm.model.setting.InputSetting;
import com.jtech.apps.hcm.model.setting.ProductControlSetting;
import com.jtech.apps.hcm.model.setting.ProductTriggerSetting;
import com.jtech.apps.hcm.model.setting.ProductUser;
import com.jtech.apps.hcm.model.setting.RelaySetting;
import com.jtech.apps.hcm.model.setting.Setting;
import com.jtech.apps.hcm.util.TimeUtil;

@Service
public class UserProductService {
	@Autowired
	UserProductDAO userProductDAO;
	@Autowired
	ProductRegistrationService productregistrationService;
	@Autowired
	ProductCategoryService productCategoryService;
	@Autowired
	UserProfileService userProfileService;
	@Autowired
	ConnectionService connectionService;

	private final Logger logger = Logger.getLogger(UserProductService.class);

	/**
	 * getUserProducts function returns every userProducts
	 * 
	 * @return List<UserProduct>
	 */
	public List<UserProduct> getUserProducts() {

		logger.debug("Getting all UserProducts...");
		List<UserProduct> userProducts = userProductDAO.getUserProducts();
		for (UserProduct userProduct : userProducts) {
			Connection connection = connectionService.getConnection(userProduct.getSerialNumber());

			if (connection == null) {
				userProduct.setConnected(false);
			} else {
				userProduct.setConnected(connection.getStatus().equals("CONNECTED"));
			}
		}
		return userProducts;
	}

	/**
	 * switchRelay function
	 * 
	 * returns 0 if device is offline (connections table) opens socket on
	 * deviceSession console port, that starts a userSession where it's able to
	 * execute commands on user's devices device's response is recieved through
	 * the notificationService
	 * 
	 * @param userId
	 * @param serialNumber
	 * @param moduleId
	 * @param relayId
	 * @param state
	 * @return Integer
	 */
	public Integer switchRelay(Integer userId, String serialNumber, String moduleId, String relayId, String state) {

		Connection connection = connectionService.getConnection(serialNumber);

		if (connection == null || connection.getStatus().equals("DISCONNECTED")) {
			logger.error("Module is offline... (" + serialNumber + ") REL(" + relayId + ")");
			return 0;
		}

		Socket socket = null;
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;
		try {
			logger.debug("Opening socket to " + connection.getHost() + ":" + connection.getConsolePort());
			socket = new Socket(connection.getHost(), connection.getConsolePort());
			socket.setSoTimeout(10000);
			printWriter = new PrintWriter(socket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter.write("USERID;" + userId + "\n");
			printWriter.flush();
			printWriter.write("SWITCH;" + serialNumber + ";" + moduleId + ";" + relayId + ";" + state + "\n");
			printWriter.flush();
			// String response = bufferedReader.readLine();
			// do something with the response
		} catch (SocketException e) {
			logger.error("SocketException Serial=" + serialNumber + " Host:" + connection.getHost() + ":"
					+ connection.getConsolePort());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			logger.error("NumberFormatException Serial=" + serialNumber + " Host:" + connection.getHost() + ":"
					+ connection.getConsolePort());
			e.printStackTrace();
		} catch (UnknownHostException e) {
			logger.error("UnknownHostException Serial=" + serialNumber + " Host:" + connection.getHost() + ":"
					+ connection.getConsolePort());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException Serial=" + serialNumber + " Host:" + connection.getHost() + ":"
					+ connection.getConsolePort());
			e.printStackTrace();
		} finally {
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				logger.error("IOException Serial=" + serialNumber + " Host:" + connection.getHost() + ":"
						+ connection.getConsolePort());
				e.printStackTrace();
			}
		}

		return 0;
	}

	/**
	 * getUserProductByUserId function
	 * 
	 * @param userId
	 * @return List<UserProduct>
	 */
	public List<UserProduct> getUserProductByUserId(Integer userId) {
		logger.debug("getUserProductByUserId userid:" + userId);
		List<UserProduct> userProducts = getUserProducts();
		List<UserProduct> ups = new LinkedList<UserProduct>();

		for (UserProduct userProduct : userProducts) {

			List<ProductUser> productUsers = userProduct.getProductUsers();
			for (ProductUser productUser : productUsers) {
				if (productUser.getUserId().equals(userId)) {
					ups.add(userProduct);
				}
			}
		}
		return ups;
	}

	/**
	 * getUserProductBySerialNumber function
	 * 
	 * @param serialNumber
	 * @return UserProduct
	 */
	public UserProduct getUserProductBySerialNumber(String serialNumber) {

		logger.debug("getUserProductBySerialNumber serialNumber:" + serialNumber);

		List<UserProduct> userProducts = getUserProducts();

		for (UserProduct userProduct : userProducts) {
			if (userProduct.getSerialNumber().equals(serialNumber)) {
				return userProduct;
			}
		}
		logger.error("getUserProductBySerialNumber serialNumber:" + serialNumber + " UserProduct not found...");
		return null;
	}

	/**
	 * getUserProductSetting function
	 * 
	 * @param serialNumber
	 * @return Setting
	 */
	public Setting getUserProductSetting(String serialNumber) {

		logger.debug("getUserProductSetting serialNumber:" + serialNumber);

		List<Setting> settings = userProductDAO.getUserProductSettings(serialNumber);
		for (Setting setting : settings) {
			if (setting.isSelected()) {
				return setting;
			}
		}
		logger.error("getUserProductSetting serialNumber:" + serialNumber + " UserProductSetting not found...");
		return null;
	}

	/**
	 * updateUserProduct function
	 * 
	 * @param userProduct
	 * @return Integer
	 */
	public Integer updateUserProduct(UserProduct userProduct) {

		String serialNumber = userProduct.getSerialNumber();
		
		logger.debug("updateUserProduct serialNumber:" + serialNumber);

		// Not existing product
		if (getUserProductBySerialNumber(serialNumber) == null) {
			logger.error("updateUserProduct - serialNumber:" + serialNumber + " UserProduct not exist");
			return 0;
		}

		// Only debugging
		for (Setting setting : userProduct.getProductSettings()) {
			for (RelaySetting relaySetting : setting.getRelaySettings()) {
				for (ProductControlSetting pcs : relaySetting.getProductControlSettings()) {
					// logger.debug("relay:" + relaySetting.getRelayId() + "user
					// id:" + pcs.getUserId() + " access:" + (pcs.isAccess() ?
					// "Y" : "N"));
				}
			}
		}

		return userProductDAO.updateUserProduct(userProduct);
	}

	/**
	 * addRelaySetting function
	 * 
	 * @param relaySetting
	 * @param serialNumber
	 * @param settingId
	 * @return
	 */
	public Integer addRelaySetting(RelaySetting relaySetting, String serialNumber, Integer settingId) {
		logger.debug("addRelaySetting serialNumber:" + serialNumber);
		return userProductDAO.addUserProductRelaySetting(relaySetting, serialNumber, settingId);
	}

	/**
	 * updateRelaySetting function
	 * 
	 * @param relaySetting
	 * @param serialNumber
	 * @param settingId
	 * @return
	 */
	public Integer updateRelaySetting(RelaySetting relaySetting, String serialNumber, Integer settingId) {
		logger.debug("updateRelaySetting serialNumber:" + serialNumber);
		return userProductDAO.updateUserProductRelaySetting(relaySetting, serialNumber, settingId);
	}

	/**
	 * addUserProduct function
	 * 
	 * @param userProduct
	 * @return
	 */
	public Integer addUserProduct(UserProduct userProduct) {

		logger.debug("addUserProduct");
		
		List<UserProduct> userProducts = getUserProducts();

		for (UserProduct up : userProducts) {
			if (up.getSerialNumber().equals(userProduct.getSerialNumber()))
				logger.error("addUserProduct serialNumber:" + userProduct.getSerialNumber() + " already exist...");
				return 0;
		}

		return userProductDAO.addUserProduct(userProduct);
	}

	/**
	 * registerProduct function
	 * 
	 * @param userId
	 * @param serialNumber
	 * @return
	 */
	public Integer registerProduct(Integer userId, String serialNumber) {

		boolean isValidUser = false;
		boolean isValidSerial = false;
		
		logger.debug("registerProduct userId:" + userId + " serialNumber:" + serialNumber);

		RegisteredProduct registeredProduct = productregistrationService
				.getRegisteredProductBySerialNumber(serialNumber);

		if (registeredProduct != null && registeredProduct.isRegistered() == true
				&& registeredProduct.isActivated() == false) {
			isValidSerial = true;
			logger.debug("Valid serial number: " + serialNumber);
		}

		UserProfile userProfile = userProfileService.getUserProfileByUserId(userId);
		if (userProfile != null) {
			isValidUser = true;
			logger.debug("Valid userId: " + userId);
		}

		if (isValidUser && isValidSerial) {

			int err = productregistrationService.registerProduct(serialNumber);
			
			if (err == 0){
				logger.error("Error during productRegistration. serialNumber:" + serialNumber + " userId:" + userId);
				return 0;
			}

			logger.info(serialNumber + " registered successfully.");	

			ProductCategory pc = productCategoryService.getProductCategoryById(registeredProduct.getProductId());

			List<ProductUser> productUsers = new LinkedList<ProductUser>();
			ProductUser productUser = new ProductUser();
			productUser.setUserId(userProfile.getUserId());
			productUser.setUserName(userProfile.getUserName());
			productUser.setPrivilige("ADMIN");
			productUsers.add(productUser);

			UserProduct userProduct = new UserProduct();
			userProduct.setSerialNumber(serialNumber);
			userProduct.setName(pc.getProductName());
			userProduct.setRelayCount(pc.getRelayCount());
			userProduct.setInputCount(pc.getInputCount());
			userProduct.setPhoneNumber("");
			userProduct.setPrimaryHost(pc.getPrimaryHost());
			userProduct.setPrimaryPort(pc.getPrimaryPort());
			userProduct.setSecondaryHost(pc.getSecondaryHost());
			userProduct.setSecondaryPort(pc.getSecondaryPort());
			userProduct.setEdited(true);

			ProductControlSetting productControlSetting = new ProductControlSetting();
			productControlSetting.setUserId(userProfile.getUserId());
			productControlSetting.setAccess(true);
			productControlSetting.setCallAccess(true);

			List<Setting> settings = pc.getSettings();
			for (RelaySetting relaySetting : settings.get(0).getRelaySettings()) {
				relaySetting.addProductControlSetting(productControlSetting);
			}
			userProduct.setProductSettings(settings);

			userProduct.setProductUsers(productUsers);

			err = addUserProduct(userProduct);

			return err;
		}

		return 0;
	}

	public UserProduct getTestData() {
		UserProduct userProduct = getUserProductBySerialNumber("NO7S0YJR2N");

		List<ProductControlSetting> productControlSettings = new LinkedList<ProductControlSetting>();
		List<ProductTriggerSetting> productTriggerSettings = new LinkedList<ProductTriggerSetting>();

		ProductControlSetting pcs = new ProductControlSetting();
		pcs.setUserId(1);

		pcs.setCallAccess(true);
		pcs.setAccess(true);
		pcs.setCreationDate(TimeUtil.getTimeStamp());
		pcs.setLastUpdateDate(TimeUtil.getTimeStamp());

		productControlSettings.add(pcs);

		ProductTriggerSetting pts = new ProductTriggerSetting();
		pts.setTriggerId(1);
		pts.setTriggerRelayId(1);
		pts.setTriggerEnabled(true);
		pts.setTriggerAction("test action");
		pts.setTriggerState("ON");
		pts.setTriggerValue("test value");
		pts.setLastUpdateDate(TimeUtil.getTimeStamp());

		productTriggerSettings.add(pts);

		List<Setting> settings = userProduct.getProductSettings();

		for (Setting setting : settings) {
			List<RelaySetting> relaySettings = setting.getRelaySettings();
			for (RelaySetting relaySetting : relaySettings) {
				relaySetting.setProductControlSettings(productControlSettings);
			}
			List<InputSetting> inputSettings = setting.getInputSettings();
			for (InputSetting inputSetting : inputSettings) {
				inputSetting.setProductTriggerSettings(productTriggerSettings);
			}
			setting.setRelaySettings(relaySettings);
			setting.setInputSettings(inputSettings);
		}

		userProduct.setProductSettings(settings);

		return userProduct;
	}

}
