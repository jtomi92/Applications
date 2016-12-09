package com.jtech.apps.hcm.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.jtech.apps.hcm.helpers.RestUtils;
import com.jtech.apps.hcm.model.Connection;
import com.jtech.apps.hcm.service.NotificationService;

 

public class SocketHandler extends TextWebSocketHandler {

	private static final Logger logger = Logger.getLogger(SocketHandler.class);
	private RestUtils restUtils = new RestUtils();

	
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
         
    	
    	logger.info("RECIEVED " + message.getPayload());
    	
    	if (message.getPayload().contains("USER")){
    		
    		String[] payload = message.getPayload().split("#");
    		logger.info("SIZE=" + payload.length);
    		
    		if (payload.length == 3){
    			Integer userId = Integer.parseInt(payload[1]);
    			
    			logger.info("GETTING CONNECTIONS");
    			 
    			List<Connection> connections = restUtils.getConnections();
    			
    			if (connections != null && connections.size() != 0){
    				
    				for (Connection connection : connections) {
						   
						NotificationService notificationService = new NotificationService(userId,session,connection);
						Thread thread = new Thread(notificationService);
						thread.start();
					}
    			} else {
    				logger.error("NO CONNECTIONS");
    			}
    		}
    	 
    	} else if (message.getPayload().contains("DISCONNECT")){
    		
    	} else {
    		
    		TextMessage msg = new TextMessage("Hello");
            try {
            	
    			session.sendMessage(msg);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			 
    			e.printStackTrace();
    		}
            
    	}
    }

}