package com.jtech.apps.hcm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtech.apps.hcm.dao.interfaces.ConnectionsDAO;
import com.jtech.apps.hcm.model.Connection;

@Service
public class ConnectionService {

	@Autowired
	ConnectionsDAO connectionDAO;

	public int addConnection(Connection connection) {

		return connectionDAO.addConnection(connection);
	}

	public int updateConnection(Connection connection) {

		Connection conn = getConnection(connection.getSerialNumber());
		if (conn == null) {
			return addConnection(connection);
		}
		return connectionDAO.updateConnection(connection);
	}

	public Connection getConnection(String serialNumber) {

		return connectionDAO.getConnection(serialNumber);
	}

	public List<Connection> getConnections() {
		
		return connectionDAO.getConnections();
	}
}
