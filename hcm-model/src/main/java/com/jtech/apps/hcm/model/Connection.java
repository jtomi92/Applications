package com.jtech.apps.hcm.model;

public class Connection {

	String serialNumber;
	String host;
	Integer devicePort;
	Integer consolePort;
	String status;
	
	
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getDevicePort() {
		return devicePort;
	}
	public void setDevicePort(Integer devicePort) {
		this.devicePort = devicePort;
	}
	public Integer getConsolePort() {
		return consolePort;
	}
	public void setConsolePort(Integer consolePort) {
		this.consolePort = consolePort;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
