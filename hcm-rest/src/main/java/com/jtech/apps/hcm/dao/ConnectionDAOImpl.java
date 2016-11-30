package com.jtech.apps.hcm.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.jtech.apps.hcm.dao.interfaces.ConnectionsDAO;
import com.jtech.apps.hcm.model.Connection;
import com.jtech.apps.hcm.model.setting.InputSetting;
import com.jtech.apps.hcm.model.setting.ProductTriggerSetting;
import com.jtech.apps.hcm.util.TimeUtil;

@Repository
public class ConnectionDAOImpl implements ConnectionsDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public int addConnection(Connection connection) {

		String sql = "INSERT INTO CONNECTIONS (SERIAL_NUMBER, HOST, DEVICE_PORT, CONSOLE_PORT, STATUS, CREATION_DATE, LAST_UPDATE_DATE) VALUES (:SERIAL_NUMBER, :HOST, :DEVICE_PORT, :CONSOLE_PORT, :STATUS, :CREATION_DATE, :LAST_UPDATE_DATE)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SERIAL_NUMBER", connection.getSerialNumber());
		parameters.put("HOST", connection.getHost());
		parameters.put("DEVICE_PORT", Integer.toString(connection.getDevicePort()));
		parameters.put("CONSOLE_PORT", Integer.toString(connection.getConsolePort()));
		parameters.put("STATUS", connection.getStatus());
		parameters.put("CREATION_DATE", TimeUtil.getTimeStamp());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int updateConnection(Connection connection) {
		String sql = "UPDATE CONNECTIONS SET HOST = :HOST, DEVICE_PORT = :DEVICE_PORT, CONSOLE_PORT = :CONSOLE_PORT, STATUS = :STATUS, LAST_UPDATE_DATE = :LAST_UPDATE_DATE WHERE SERIAL_NUMBER = :SERIAL_NUMBER";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("HOST", connection.getHost());
		parameters.put("DEVICE_PORT", Integer.toString(connection.getDevicePort()));
		parameters.put("CONSOLE_PORT", Integer.toString(connection.getConsolePort()));
		parameters.put("STATUS", connection.getStatus());
		parameters.put("SERIAL_NUMBER", connection.getSerialNumber());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public Connection getConnection(String serialNumber) {

		String sql = "SELECT * FROM CONNECTIONS WHERE SERIAL_NUMBER = ?";
		
		Map<String, Object> row = new HashMap<String, Object>();
		
		try {
		row = jdbcTemplate.queryForMap(sql, serialNumber);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
			
		Connection connection = new Connection();
		connection.setHost((String)row.get("HOST"));
		connection.setDevicePort(Integer.parseInt((String)row.get("DEVICE_PORT")));
		connection.setConsolePort(Integer.parseInt((String)row.get("CONSOLE_PORT")));
		connection.setStatus((String)row.get("STATUS"));
		
		return connection;
	}

	@Override
	public List<Connection> getConnections() {
		
		List<Connection> connections = new LinkedList<Connection>();

		String sql = "SELECT DISTINCT HOST, CONSOLE_PORT FROM CONNECTIONS";
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> row : rows) {

			Connection connection = new Connection();
			connection.setHost((String)row.get("HOST"));
			connection.setConsolePort(Integer.parseInt((String)row.get("CONSOLE_PORT")));

			connections.add(connection);
		}
		
  	
		return connections;
	}
	
	

}
