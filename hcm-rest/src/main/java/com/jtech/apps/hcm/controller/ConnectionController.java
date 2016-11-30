package com.jtech.apps.hcm.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jtech.apps.hcm.model.Connection;
import com.jtech.apps.hcm.service.ConnectionService;

 
@Controller
public class ConnectionController {
	
	private static final Logger logger = Logger.getLogger(ConnectionController.class);
 
	@Autowired 
	ConnectionService connectionService;
	
	@RequestMapping(value = "/connection/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> updateConnection(@RequestBody Connection connection) {

		int err = connectionService.updateConnection(connection);
		return new ResponseEntity<Integer>(err, HttpStatus.OK);
	}
	@RequestMapping(value = "/connection/get/{serial}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Connection> getConnection(@PathVariable("serial") String serialNumber) {
	
		Connection connection = connectionService.getConnection(serialNumber);
		return new ResponseEntity<Connection>(connection, HttpStatus.OK);
	}
	@RequestMapping(value = "/connection/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Connection>> getConnections() {
		logger.info("GETTING CONNECTIONS");
		List<Connection> connections = connectionService.getConnections();
		logger.info("PROCESSING");
		return new ResponseEntity<List<Connection>>(connections, HttpStatus.OK);
	}
	@RequestMapping(value = "/connection/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Connection> test() {
	
		Connection connection = new Connection();
		connection.setHost("TestHost.com");
		connection.setDevicePort(90);
		connection.setConsolePort(90);
		connection.setStatus("CONNECTED");
		
		return new ResponseEntity<Connection>(connection, HttpStatus.OK);
	}
}
