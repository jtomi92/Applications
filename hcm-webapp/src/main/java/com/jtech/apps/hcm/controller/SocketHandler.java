package com.jtech.apps.hcm.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.jtech.apps.hcm.helpers.RestUrls;
import com.jtech.apps.hcm.model.Connection;
import com.jtech.apps.hcm.service.NotificationService;

 

public class SocketHandler extends TextWebSocketHandler {

	private static final Logger logger = Logger.getLogger(SocketHandler.class);
	
	RestTemplate restTemplate = new RestTemplate();
	HttpHeaders headers = new HttpHeaders();
	HttpHeaders requestHeaders = new HttpHeaders();
	HttpEntity<?> httpEntity = new HttpEntity<Object>(requestHeaders);
	
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
         
    	
    	logger.info("RECIEVED " + message.getPayload());
    	
    	if (message.getPayload().contains("USER")){
    		
    		String[] payload = message.getPayload().split("#");
    		logger.info("SIZE=" + payload.length);
    		
    		if (payload.length == 3){
    			Integer userId = Integer.parseInt(payload[1]);
    			
    			logger.info("GETTING CONNECTIONS");
    			ParameterizedTypeReference<List<Connection>> typeRef = new ParameterizedTypeReference<List<Connection>>() {
    			};
    	 
    			ResponseEntity<List<Connection>> responseEntity = restTemplate.exchange(RestUrls.REST_GET_CONNECTIONS,
    					HttpMethod.GET, httpEntity, typeRef);
    		 
    			List<Connection> connections = responseEntity.getBody();
    			
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