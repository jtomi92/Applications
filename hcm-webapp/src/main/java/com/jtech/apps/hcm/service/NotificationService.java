package com.jtech.apps.hcm.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.jtech.apps.hcm.model.Connection;

public class NotificationService implements Runnable  {

	private static final Logger logger = Logger.getLogger(NotificationService.class);
	private Integer userId;
	private WebSocketSession session;
	private Connection connection;


	public NotificationService(Integer userId, WebSocketSession session, Connection connection) {
		this.userId = userId;
		this.session = session;
		this.connection = connection;
	}

	public void run() {

		if (userId == null || userId == 0) {
			logger.error("INVALID USERID=" + userId);
			return;
		}
		if (session == null || !session.isOpen()) {
			logger.error("SESSION IS NULL OR NOT OPEN");
		}
		if (connection.getConsolePort() == null || connection.getHost() == null) {
			logger.error("CONNECTION ERROR");
		}

		logger.info("Notification service started on host=" + connection.getHost() + ":" + connection.getConsolePort()
				+ " for user=" + userId);

		Socket socket = null;
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;
		try {
			socket = new Socket(connection.getHost(), connection.getConsolePort());			
			socket.setSoTimeout(10000);
			printWriter = new PrintWriter(socket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			printWriter.write("USERID;" + userId + ";\n");
			printWriter.flush();
 
  
			while (session.isOpen() && socket.isConnected()) {
				String notification = bufferedReader.readLine();
				if (notification.contains("HEARTBEAT")) {
					//logger.info("HEARTBEAT");
				} else {
					logger.info("Notification is=" + notification);
					session.sendMessage(new TextMessage(notification));
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			logger.error("UNKNOWN HOST");
		} catch (SocketTimeoutException e) {
			logger.error("TIMEOUT");
		} catch (ConnectException e) {
			logger.error("UNABLE TO CONNECT");
		} catch (SocketException e){
			logger.error("CONNECTION RESET");
		}	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				logger.info("Closing NotificationServiceSocket for " + connection.getHost());
				if (bufferedReader != null){
					bufferedReader.close();
				}
				if (printWriter != null){
					printWriter.close();
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

	public Integer getUserId() {
		return userId;
	}

 
}
