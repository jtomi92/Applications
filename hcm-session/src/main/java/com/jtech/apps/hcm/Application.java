package com.jtech.apps.hcm;
 

import com.jtech.apps.hcm.server.DeviceSessionProvider;
import com.jtech.apps.hcm.server.UserSessionProvider;
 
public class Application {
	
	private static Integer consolePort = 90;
	private static Integer devicePort = 86;

	// TODO: get these info from property file
	public static void main(String[] args) {
		
		
		DeviceSessionProvider.setDevicePort(devicePort); 
		DeviceSessionProvider.setConsolePort(consolePort);
		DeviceSessionProvider.getInstance().start();
		
		UserSessionProvider.setPort(consolePort);
		UserSessionProvider.getInstance().start();
		
	}

}
