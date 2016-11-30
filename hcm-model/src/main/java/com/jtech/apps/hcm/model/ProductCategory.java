package com.jtech.apps.hcm.model;

import java.util.List;
 
import com.jtech.apps.hcm.model.setting.Setting;

public class ProductCategory {

	private Integer productId;
	private String productName;
	private Integer relayCount;
	private Integer inputCount;	
	
	private String primaryHost;
	private String primaryPort;
	private String secondaryHost;
	private String secondaryPort;
	
	private String creationDate;
	private String lastUpdateDate;
	
	private List<Setting> settings;

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getRelayCount() {
		return relayCount;
	}

	public void setRelayCount(Integer relayCount) {
		this.relayCount = relayCount;
	}

	public Integer getInputCount() {
		return inputCount;
	}

	public void setInputCount(Integer inputCount) {
		this.inputCount = inputCount;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	} 

	public String getPrimaryHost() {
		return primaryHost;
	}

	public void setPrimaryHost(String primaryHost) {
		this.primaryHost = primaryHost;
	}

	public String getPrimaryPort() {
		return primaryPort;
	}

	public void setPrimaryPort(String primaryPort) {
		this.primaryPort = primaryPort;
	}

	public String getSecondaryHost() {
		return secondaryHost;
	}

	public void setSecondaryHost(String secondaryHost) {
		this.secondaryHost = secondaryHost;
	}

	public String getSecondaryPort() {
		return secondaryPort;
	}

	public void setSecondaryPort(String secondaryPort) {
		this.secondaryPort = secondaryPort;
	}

	public List<Setting> getSettings() {
		return settings;
	}

	public void setSettings(List<Setting> settings) {
		this.settings = settings;
	}
	 
	 
	
	
 
	

	
	
}
