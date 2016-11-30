package com.jtech.apps.hcm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
 
public class DeviceSessionProvider extends Thread implements Runnable {
 
	
	private static final Logger logger = Logger.getLogger(DeviceSessionProvider.class);
	
	private static List<DeviceSession> deviceSessions = new LinkedList<DeviceSession>();
	private static Integer devicePort; 
	private static Integer consolePort;
	private static String host = "localhost";
	private static DeviceSessionProvider serverThread;
	
	public static DeviceSessionProvider getInstance() {
		if (serverThread == null) {
			serverThread = new DeviceSessionProvider();
		}
		return serverThread;
	}

	@Override
	public void run() {

		logger.info("DeviceSessionProvider started...");
		
		ServerSocket serverSocket = null;
		
		try {
			
			serverSocket = new ServerSocket(devicePort); 

			while (!Thread.interrupted()) {

				logger.info("Waiting for devices...");

				Socket socket = serverSocket.accept();
					
				logger.info("Device connected!");
				
				DeviceSession deviceSession = new DeviceSession(socket, host, devicePort, consolePort);
				deviceSession.start();

				deviceSessions.add(deviceSession);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
				logger.info("Closing server socket");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void setDevicePort(Integer devicePort) {
		DeviceSessionProvider.devicePort = devicePort;
	}
	public static void setConsolePort(Integer consolePort){
		DeviceSessionProvider.consolePort = consolePort;
	}

	public List<DeviceSession> getDeviceSessions() {
		return deviceSessions;
	}
 
	/**
	 * 
	 * @param serial
	 * @return
	 */
	public DeviceSession getDeviceSessionBySerialNumber(String serial) {
		for (int i = 0; i < deviceSessions.size(); i++) {
			if (deviceSessions.get(i).getSerialNumber().equals(serial)) {
				return deviceSessions.get(i);
			}
		}
		return null;
	}
	
}
