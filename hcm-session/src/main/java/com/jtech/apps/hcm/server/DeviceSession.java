package com.jtech.apps.hcm.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

/*
 * TODO:
 * - Redo REST communication similar to WebAppService
 * - Use property file for device commands
 * - Encode/Decode communication with device (HW Updates required)
 * - Change delays to waitForOKResponse (HW Updates required)
 * - Rethink the process we communicate towards the device (switchRelay procedure)
 * - DeviceSessions should retrieve UserProduct automatically when Settings/ProductUsers are updated 
 */

public class DeviceSession extends Thread implements Runnable {

	private static final Logger logger = Logger.getLogger(DeviceSession.class);

	private final String REST_URL = "http://localhost:8081";
	private final String REST_ADD_REGISTERED_PRODUCT = REST_URL + "/registeredproduct/add";
	private final String REST_GET_REGISTERED_PRODUCT_BY_SERIAL_NUMBER = REST_URL + "/registeredproduct/get/{serial}";
	private final String REST_GET_USER_PRODUCT_BY_SERIAL_NUMBER = REST_URL + "/product/get/serial/{serial}";
	private final String REST_GET_PRODUCT_CATEGORY_BY_ID = REST_URL + "/productcategory/get/id/{id}";
	private final String REST_GET_USER_PROFILE_BY_ID = REST_URL + "/userprofile/get/id/{userid}";
	private final String REST_GET_PRODUCT_CATEGORY_BY_NAME = REST_URL + "/productcategory/get/name/{id}";
	private final String REST_UPDATE_CONNECTION = REST_URL + "/connection/update";
	public final String REST_UPDATE_USER_PRODUCT = REST_URL + "/product/update";

	private String serialNumber;
	private Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private Connection connection;

	private boolean isValidConnection = true;

	private ProductCategory productCategory;
	private UserProduct userProduct;

	/**
	 * 
	 * @param socket
	 * @param host
	 * @param devicePort
	 * @param consolePort
	 */
	public DeviceSession(Socket socket, String host, Integer devicePort, Integer consolePort) {
		this.socket = socket;
 
		connection = new Connection();
		connection.setHost(host);
		connection.setDevicePort(devicePort);
		connection.setConsolePort(consolePort);
	}

	@Override
	public void run() {

		try {

			socket.setSoTimeout(40000);
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(socket.getOutputStream());

			String time = new SimpleDateFormat("yy;MM;dd;HH;mm;ss;").format(Calendar.getInstance().getTime());
			write("TIME;" + time);

			while (!Thread.interrupted() && isValidConnection) {

				String read = bufferedReader.readLine();

				logger.info("Recieved data:" + read);

				processIO(read);

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			
			if (serialNumber != null) {
				Integer sessionCount = 0;
				List<DeviceSession> deviceSessions = DeviceSessionProvider.getInstance().getDeviceSessions();
				for (DeviceSession deviceSession : deviceSessions) {
					if (deviceSession.getSerialNumber().equals(serialNumber)){
						sessionCount++;
					}
				}
				
				// When a device is restarted, it connects to the server in less time, than it's previous session is dumped.
				if (sessionCount == 1){
					connection.setStatus("DISCONNECTED");
					connection.setSerialNumber(serialNumber);
					restPut(connection, Integer.class, REST_UPDATE_CONNECTION);
					notifyUserSessions("NOTIFICATION;CONNECTION;OFFLINE");
				}
			}
			
			logger.error("Closing device session with serialnumber: " + serialNumber);

			// TODO: This maybe not working
			DeviceSessionProvider.getInstance().getDeviceSessions().remove(this);
			try {
				printWriter.close();
				bufferedReader.close();
				socket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Process recieved data from device Input String must start with #
	 * character
	 * 
	 * @param readLine
	 */
	private void processIO(String readLine) {

		String[] read = readLine.split("#");
		if (read.length == 1 || read.length == 0) {
			logger.error("Error with incoming data: " + readLine);
			isValidConnection = false;
			return;
		}
		// TODO: Implement decoding procedures
		// decode(read[1])
		String[] arguments = read[1].split(";");

		String command = arguments[0];

		switch (command) {

		// DEVICES FIRST SEND PRODUCT_NAME IF NO SERIAL_NUMBER SET
		case DeviceConstants.REQUEST_SERIAL_NUMBER:

			if (arguments.length == 2) {
				String productName = arguments[1];
				logger.info("Requesting serialNumber...");
				requestSerialNumber(productName);
				notifyUserSessions("NOTIFICATION;CONNECTION;ONLINE");
			} else {
				logger.error("Not enough parameters for REQUEST_SERIAL_NUMBER command: " + readLine);
				isValidConnection = false;
			}
			break;

		// If device have serialnumber it'll only send it.
		case DeviceConstants.SERIAL_NUMBER:

			if (arguments.length == 2) {
				String serialNumber = arguments[1];
				logger.info("Device's serialNumber is " + serialNumber + " uploading settings...");
				updateDevice(serialNumber);
				notifyUserSessions("NOTIFICATION;CONNECTION;ONLINE");
			} else {
				isValidConnection = false;
				logger.error("Not enough parameters for SERIAL_NUMBER command: " + readLine);
			}
			break;

		case "NOTIFICATION":// NOTIFICATION;SWITCH;relay_id;state
			notifyUserSessions(readLine);
			break;

		case "RELAYCONNECTIONS": // RELAYCONNECTIONS;1:2:3:4:10:11:12;

			logger.info("Updating relay connections...");
			String[] relayIds = arguments[1].split(":");

			processRelayConnections(relayIds);

			break;

		case "CHECK":// INPUT;input_id;value
			// problem statement: turn off and on the device, session needs 40
			// sec to time out,
			// but device reconnects sooner, upon timing out, connection will be
			// updated to disconnected
			// this should be done in a better way
			write("LIVE");
			break;

		default:
			break;
		}
	}

	/**
	 * processRelayConnections procedure
	 * 
	 * A device's relay interfaces can dynamically change with time. We can
	 * connect/disconnect relaymodules Upon these events, device will update the
	 * server with it's connected relayIds. This function will set current
	 * relays to enabled/disabled accordingly or add new RelaySetting(s).
	 * Database is update with the new configuration along with the device
	 * itself.
	 * 
	 * @param relayIds
	 */
	private void processRelayConnections(String[] relayIds) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < relayIds.length; i++) {
			sb.append(relayIds[i] + ",");
		}
		logger.debug("Connected relayIds for " + userProduct.getSerialNumber() + ":" + sb.toString());

		List<RelaySetting> relaySettings = userProduct.getProductSettings().get(0).getRelaySettings();
		List<ProductControlSetting> productControlSettings = relaySettings.get(0).getProductControlSettings();
		List<RelaySetting> generatedRelaySettings = new LinkedList<RelaySetting>();

		// set all relays to disabled
		for (RelaySetting relaySetting : relaySettings) {
			relaySetting.setRelayEnabled(false);
		}

		// enable connected relays, generate new relays
		for (int i = 0; i < relayIds.length; i++) {
			boolean found = false;
			for (RelaySetting relaySetting : relaySettings) {
				if (relaySetting.getRelayId().equals(Integer.parseInt(relayIds[i]))) {
					relaySetting.setRelayEnabled(true); // enables relay
					found = true; // mark it as found
					break;
				}
			}
			if (!found) { // relay was recently added and not present in db
				logger.debug("Relay ID not found:" + relayIds[i] + " generating new setting...");
				RelaySetting relaySetting = generateRelaySetting(Integer.parseInt(relayIds[i]), productControlSettings);
				generatedRelaySettings.add(relaySetting);
			}
		}

		if (generatedRelaySettings.size() != 0) {
			userProduct.getProductSettings().get(0).getRelaySettings().addAll(generatedRelaySettings);
		}

		// update UserProductSetting in db and on device

	}

	/**
	 * notifyUserSessions procedure
	 * 
	 * Finds ProductUser's UserSessions and notify them about stuff Problem
	 * Statement: if I add a new ProductUser to the device, UserProduct should
	 * be updated somehow
	 * 
	 * @param readLine
	 */
	private void notifyUserSessions(String readLine) {
		String[] args = readLine.split(";");

		logger.info("Pushing device notification to UserSessions...");

		List<ProductUser> productUsers = userProduct.getProductUsers();
		if (productUsers == null || productUsers.isEmpty()) {
			logger.error("No ProductUser found for " + userProduct.getSerialNumber());
			return;
		}

		for (ProductUser productUser : productUsers) {

			logger.debug("PRODUCTUSER=" + productUser.getUserName() + " (" + productUser.getUserId() + ")");
			List<UserSession> userSessions = UserSessionProvider.getInstance()
					.getUserSessionById(productUser.getUserId());

			for (UserSession userSession : userSessions) {

				// here we list all kinds of notifications

				if (args.length == 4 && args[1].equals("SWITCH")) {
					String notification = "SWITCH " + serialNumber + " " + args[2] + " " + args[3] + "\n";
					userSession.notifySession(notification);
					logger.debug("USERID in Session: " + userSession.getUserId() + " Notification:" + notification);
				}
				if (args.length == 3 && args[1].equals("CONNECTION")) {
					String notification = "CONNECTION " + serialNumber + " " + args[2] + "\n";
					userSession.notifySession(notification);
					logger.debug("USERID in Session: " + userSession.getUserId() + " Notification:" + notification);
				}
			}
		}
	}

	/**
	 * requestSerialNumber procedure When device connects for the first time,
	 * they do not have serial number, so they request one. SerialNumber 10 char
	 * long, is randomly generated from numbers and characters
	 * 
	 * @param productName
	 */
	private void requestSerialNumber(String productName) {

		ProductCategory productCategory = (ProductCategory) restGet(REST_GET_PRODUCT_CATEGORY_BY_NAME,
				ProductCategory.class, productName);

		if (productCategory != null) {

			String serialNumber = generateSerialNumber(10);

			RegisteredProduct registeredProduct = new RegisteredProduct();
			registeredProduct.setSerialNumber(serialNumber);
			registeredProduct.setProductId(productCategory.getProductId());
			registeredProduct.setRegistered(true);
			registeredProduct.setActivated(false);
			registeredProduct.setCreationDate(TimeUtil.getTimeStamp());
			registeredProduct.setLastUpdateDate(TimeUtil.getTimeStamp());

			logger.info("Adding new registered product with serialnumber: " + serialNumber);

			if (((Integer) restPut(registeredProduct, Integer.class, REST_ADD_REGISTERED_PRODUCT)) == 1) {
				write("SERIAL_NUMBER#" + serialNumber + "#");

				connection.setStatus("CONNECTED");
				connection.setSerialNumber(serialNumber);
				restPut(connection, Integer.class, REST_UPDATE_CONNECTION);

			} else {
				logger.error("Error during adding new registered product with serialnumber:" + serialNumber);
				isValidConnection = false;
			}
			// TODO: change delay to wait for OK response
			delay(1000);
			uploadProductSettings(productCategory);

		} else {
			logger.error("Error during getting productCategory for :" + productName);
			isValidConnection = false;
		}
	}

	/**
	 * updateDevice procedure Updates device's Settings if they have changed.
	 * Load ProductCategory settings if product is not registered Load
	 * UserProductSetting if product is registered
	 * 
	 * @param serialNumber
	 */
	private void updateDevice(String serialNumber) {

		connection.setStatus("CONNECTED");
		connection.setSerialNumber(serialNumber);
		restPut(connection, Integer.class, REST_UPDATE_CONNECTION);

		RegisteredProduct registeredProduct = (RegisteredProduct) restGet(REST_GET_REGISTERED_PRODUCT_BY_SERIAL_NUMBER,
				RegisteredProduct.class, serialNumber);

		if (registeredProduct == null) {
			logger.error("Registered product does not exist for serialnumber: " + serialNumber);
			isValidConnection = false;
		} else {
			if (registeredProduct.isActivated() && registeredProduct.isRegistered()) {

				logger.info("Registered product is ACTIVATED and REGISTERED, getting user product...");
				userProduct = (UserProduct) restGet(REST_GET_USER_PRODUCT_BY_SERIAL_NUMBER, UserProduct.class,
						serialNumber);

				// TODO: wait for OK response instead of delay
				if (userProduct != null) {
					delay(1000);
					uploadProductSettings(userProduct);
				}
			} else {

				logger.info("Registered product is NOT ACTIVATED, getting product category...");
				productCategory = (ProductCategory) restGet(REST_GET_PRODUCT_CATEGORY_BY_ID, ProductCategory.class,
						registeredProduct.getProductId());

				// TODO: wait for OK response instead of delay
				if (productCategory != null) {
					delay(1000);
					uploadProductSettings(productCategory);
				}
			}
			this.serialNumber = serialNumber;
		}
	}

	/**
	 * generateRelaySetting function
	 * 
	 * @param relayId
	 * @param productControlSettings
	 * @return RelaySetting
	 */
	private RelaySetting generateRelaySetting(Integer relayId, List<ProductControlSetting> productControlSettings) {
		RelaySetting relaySetting = new RelaySetting();
		relaySetting.setRelayId(relayId);
		relaySetting.setImpulseMode(false);
		relaySetting.setRelayEnabled(true);
		relaySetting.setDelayEnabled(false);
		relaySetting.setTimerEnabled(false);
		relaySetting.setProductControlSettings(productControlSettings);

		return relaySetting;
	}

	/**
	 * uploadProductSettings procedure uploads settings in chunks, waits for
	 * response after each chunk
	 * 
	 * @param productCategory
	 * @return boolean
	 */
	private boolean uploadProductSettings(ProductCategory productCategory) {

		List<String> settingStrings = new LinkedList<String>();
		List<Setting> settings = productCategory.getSettings();

		settingStrings.add("[CLEAR_EEPROM]");

		logger.info("Uploading product category settings...");
		StringBuilder settingString = new StringBuilder("#NS;");
		settingString.append("HOST1:" + productCategory.getPrimaryHost() + ";");
		settingString.append("PORT1:" + productCategory.getPrimaryPort() + ";");
		settingString.append("HOST2:" + productCategory.getSecondaryHost() + ";");
		settingString.append("PORT2:" + productCategory.getSecondaryPort() + ";");

		settingStrings.add(settingString.toString());

		if (settings != null) {
			for (Setting setting : settings) {
				if (setting.isSelected()) {
					settingStrings.addAll(generateSettingString(null, setting, null));
				}
			}
		}

		settingStrings.add("[END]");
		for (String str : settingStrings) {
			write("[CFG]$" + str + "$");
			try {
				bufferedReader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;

	}

	/**
	 * uploadProductSettings procedure uploads settings in chunks, waits for
	 * response after each chunk
	 * 
	 * @param userProduct
	 * @return
	 */
	private boolean uploadProductSettings(UserProduct userProduct) {

		if (!userProduct.isEdited()){
			logger.info("UserProduct Setting is not edited... Not uploading settings...");
			return false;
		}
			
		List<String> settingStrings = new LinkedList<String>();
		List<Setting> settings = userProduct.getProductSettings();

		settingStrings.add("[CLEAR_EEPROM]");

		logger.info("Uploading user product settings...");
		StringBuilder settingString = new StringBuilder("#NS;");
		settingString.append("HOST1:" + userProduct.getPrimaryHost() + ";");
		settingString.append("PORT1:" + userProduct.getPrimaryPort() + ";");
		settingString.append("HOST2:" + userProduct.getSecondaryHost() + ";");
		settingString.append("PORT2:" + userProduct.getSecondaryPort() + ";");

		settingStrings.add(settingString.toString());
		if (settings != null) {
			for (Setting setting : settings) {
				if (setting.isSelected()) {
					settingStrings.addAll(generateSettingString(userProduct.getSerialNumber(), setting,
							userProduct.getProductUsers()));
				}
			}
		}
		settingStrings.add("[END]");
		for (String str : settingStrings) {
			write("[CFG]$" + str + "$");
			try {
				bufferedReader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// setting isEdited flag so next time it won't be updated (unless settings change)
		userProduct.setEdited(false);
		restPut(userProduct, Integer.class, REST_UPDATE_USER_PRODUCT);
		return true;

	}

	/**
	 * generateSettingString function
	 * 
	 * @param serialNumber
	 * @param setting
	 * @param productUsers
	 * @return List<String> 
	 */
	private List<String> generateSettingString(String serialNumber, Setting setting, List<ProductUser> productUsers) {

		List<String> settingStrings = new LinkedList<String>();
		List<RelaySetting> relaySettings = setting.getRelaySettings();
		List<InputSetting> inputSettings = setting.getInputSettings();

		for (RelaySetting relaySetting : relaySettings) {
			StringBuilder relayString = new StringBuilder("#RS;");
			relayString.append("MI:1" + ";"); // Module ID
			relayString.append("RI:" + relaySetting.getRelayId() + ";");
			relayString.append("ST:" + relaySetting.getStartTimer() + ";");
			relayString.append("ET:" + relaySetting.getEndTimer() + ";");
			relayString.append("D:" + relaySetting.getDelay() + ";");
			relayString.append("DE:" + (relaySetting.isDelayEnabled() ? "Y;" : "N;"));
			relayString.append("TE:" + (relaySetting.isTimerEnabled() ? "Y#" : "N#"));

			// relayString.append("#PRODUCT_CONTROL_SETTING_" + i++ + ";");

			settingStrings.add(relayString.toString());
		}

		for (InputSetting inputSetting : inputSettings) {
			StringBuilder inputString = new StringBuilder("#IS;");
			inputString.append("II:" + inputSetting.getInputId() + ";");
			inputString.append("ST:" + inputSetting.getStartTimer() + ";");
			inputString.append("ET:" + inputSetting.getEndTimer() + ";");
			inputString.append("TE:" + (inputSetting.isTimerEnabled() ? "Y;" : "N;"));
			inputString.append("SR:" + inputSetting.getSampleRate() + "#");
			settingStrings.add(inputString.toString());
		}

		if (productUsers != null) {

			for (ProductUser productUser : productUsers) {

				String privilige = "USER";
				if (productUser.getPrivilige().equals("ADMIN")) {
					privilige = "ADMIN";
				}

				StringBuilder controlSetting = new StringBuilder(
						"#" + privilige + ":" + ((UserProfile) (restGet(REST_GET_USER_PROFILE_BY_ID, UserProfile.class,
								productUser.getUserId()))).getPhoneNumber() + ";");

				// logger.info("user id:" + productUser.getUserId());
				for (RelaySetting relaySetting : relaySettings) {
					for (ProductControlSetting productControlSetting : relaySetting.getProductControlSettings()) {
						// logger.info("product control user id:" +
						// productControlSetting.getUserId() + " sms:"
						// + (productControlSetting.isAccess() ? "Y" : "N"));
						if (productControlSetting.getUserId().equals(productUser.getUserId())) {
							controlSetting.append(productControlSetting.isAccess() ? "1" : "0");
						}

					}
				}
				controlSetting.append(";");

				for (RelaySetting relaySetting : relaySettings) {
					for (ProductControlSetting productControlSetting : relaySetting.getProductControlSettings()) {
						if (productControlSetting.getUserId() == productUser.getUserId()) {
							if (productControlSetting.isCallAccess()) {
								controlSetting.append(relaySetting.getRelayId());
							}
						}
					}
				}
				controlSetting.append("#");

				logger.info(controlSetting);
				settingStrings.add(controlSetting.toString());
			}
		}

		for (InputSetting inputSetting : inputSettings) {
			for (ProductTriggerSetting pts : inputSetting.getProductTriggerSettings()) {
				StringBuilder sb = new StringBuilder("#TRIGGER;");
				sb.append("TI:" + pts.getTriggerId() + ";");
				sb.append("TE:" + (pts.isTriggerEnabled() ? "Y" : "N") + ";");
				sb.append("TR:" + pts.getTriggerRelayId() + ";");
				sb.append("TV:" + pts.getTriggerValue() + ";");
				sb.append("TS:" + pts.getTriggerState() + ";");
				settingStrings.add(sb.toString());
			}
		}
		// 309225427;111101111;5
		return settingStrings;
	}

	/**
	 * switchRelay function
	 * 
	 * @param moduleId
	 * @param relayId
	 * @param state
	 * @return
	 */
	// TODO: should only use write in a generic manner
	public boolean switchRelay(Integer moduleId, Integer relayId, String state) {

		String toWrite = "SWITCHRELAY;" + moduleId + ";" + relayId + ";" + state + ";";
		// logger.info(toWrite);
		write(toWrite);

		return true;
	}

	/**
	 * Writes to device
	 * 
	 * @param toWrite
	 */
	private void write(String toWrite) {

		printWriter.write(toWrite + "\n");
		printWriter.flush();
	}

	/**
	 * generateSerialNumber function
	 * we use 10 char long serialNumbers generated from numbers and alphabetic characters
	 * @param len
	 * @return String
	 */
	private String generateSerialNumber(int len) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		SecureRandom rnd = new SecureRandom();
		boolean isValidSerial = false;

		String serialNumber = "";

		while (isValidSerial == false) {
			StringBuilder sb = new StringBuilder(len);
			for (int i = 0; i < len; i++)
				sb.append(AB.charAt(rnd.nextInt(AB.length())));
			serialNumber = sb.toString();

			if ((RegisteredProduct) restGet(REST_GET_REGISTERED_PRODUCT_BY_SERIAL_NUMBER, RegisteredProduct.class,
					serialNumber) == null) {
				isValidSerial = true;
			}
		}

		return serialNumber;
	}

	/**
	 * getSerialNumber function
	 * @return String
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private int restPut(Object object, Class cl, String url) {

		logger.info("REST_URL=" + url);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> entity = new HttpEntity<Object>(object, headers);
		ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.PUT, entity, cl);

		if (response.getStatusCode() == HttpStatus.OK) {
			return 1;
		} else {
			return 0;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object restGet(String url, Class cl, Object... args) {
		logger.info("REST_URL=" + url);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Object object = restTemplate.getForObject(url, cl, args);
		// check the response, e.g. Location header, Status, and body
		return object;
	}

	private void delay(Integer ms) {

		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
