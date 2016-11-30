package com.jtech.apps.hcm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class UserSessionProvider extends Thread implements Runnable {

	private static final Logger logger = Logger.getLogger(UserSessionProvider.class);

	private static List<UserSession> userSessions = new LinkedList<UserSession>();
	private static Integer port = 90;
	private static UserSessionProvider userSessionProvider;

	public static UserSessionProvider getInstance() {
		if (userSessionProvider == null) {
			userSessionProvider = new UserSessionProvider();
		}
		return userSessionProvider;
	}

	public static void setPort(Integer port) {
		UserSessionProvider.port = port;
	}

	@Override
	public void run() {

		logger.info("User Session Started...");

		ServerSocket serverSocket = null;

		try {

			serverSocket = new ServerSocket(port);

			while (!Thread.interrupted()) {

				logger.info("Waiting for users...");

				Socket socket = serverSocket.accept();

				logger.info("User connected.");

				UserSession userSession = new UserSession(socket);
				
				Thread thread = new Thread(userSession);
				thread.start();
				
				userSessions.add(userSession);

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
	
	public List<UserSession> getUserSessions() {
		return userSessions;
	}
	
	public List<UserSession> getUserSessionById(Integer userId) {
		List<UserSession> sessions = new LinkedList<UserSession>();
		for (int i = 0; i < userSessions.size(); i++) {
 
			if (userSessions.get(i).getUserId().equals(userId)) {
				sessions.add(userSessions.get(i));
			}
		}
		return sessions;
	}
	
	public void removeDeadSessions(){
		List<UserSession> deadSessions = new LinkedList<UserSession>();
		for (UserSession userSession : userSessions) {
			if (!userSession.isAlive()){
				deadSessions.add(userSession);
			}
		}
		userSessions.removeAll(deadSessions);
	}


}
