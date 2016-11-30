package com.jtech.apps.hcm.dao.interfaces;

import java.util.List;

import com.jtech.apps.hcm.model.Connection;

public interface ConnectionsDAO {

	public int addConnection(Connection connection);
	public int updateConnection(Connection connection);
	public Connection getConnection(String serialNumber);
	public List<Connection> getConnections();
}
