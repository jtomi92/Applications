package com.jtech.apps.hcm.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;


public class UserSession implements Runnable {

	private static final Logger logger = Logger.getLogger(UserSession.class);	
	private Integer userId = 0;
	private Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private boolean isAlive = true;

	public UserSession(Socket socket) {
		this.socket = socket;
	}

	public Integer getUserId() {
		return userId;
	}
	
 
 
	/**
	 * HEARTBEAT every 3 seconds to NotificationService or to the REST 
	 */
	@Override
	public void run() {

		try {

			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			printWriter = new PrintWriter(socket.getOutputStream());
			
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {

					while (isAlive){

						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						printWriter.write("HEARTBEAT\n");
						printWriter.flush();
		
					}					
				}
			};
			Thread thread = new Thread(runnable);
			thread.start();

			while (!Thread.interrupted()) {

				String read = bufferedReader.readLine();

				logger.info("READ: " + read);
				
				processIO(read);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			
		} finally {
			logger.error("Closing User Session for userid " + userId);
			isAlive = false;
			UserSessionProvider.getInstance().removeDeadSessions();
		 
			try {
				if (printWriter != null){
					printWriter.close();
				}
				if (bufferedReader != null){
					bufferedReader.close();
				}
				if (socket != null){
					socket.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * Recieves SWITCH command is recieved through the rest service, also this function is part of the NotificationService
	 * to provide device updates for the user.
	 * @param io
	 */
	private void processIO(String io) {

		String arguments[] = io.split(";");

		switch (arguments.length) {

		case 1:
			break;

		case 2:
			switch (arguments[0]){
			case "USERID":
				userId = Integer.parseInt(arguments[1]);
				logger.info("USERID is=" + userId);
			}
			break;

		case 5:
			switch (arguments[0]) {
			case "SWITCH":
				String serialNumber = arguments[1];
				Integer moduleId = Integer.parseInt(arguments[2]);
				Integer relayId = Integer.parseInt(arguments[3]);
				String state = arguments[4];
				logger.info("Switching..." + io);
				DeviceSessionProvider.getInstance().getDeviceSessionBySerialNumber(serialNumber).switchRelay(moduleId, relayId, state);		
				
				break;
			}
			break;
		}

	}
	
	/**
	 * Pushes notifications from backend to user interfaces
	 * @param toWrite
	 */
	public void notifySession(String toWrite){
		logger.info("Notifying User with id=" + userId + " message:" + toWrite);
		printWriter.write(toWrite);
		printWriter.flush();
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	

}
