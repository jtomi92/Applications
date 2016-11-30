package com.jtech.apps.hcm.model.setting;

public class ProductControlSetting {

	private Integer userId;
	private boolean access;
	private boolean callAccess;
	
	private String creationDate;
	private String lastUpdateDate;
	
	
	 
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	 
	 
	public boolean isAccess() {
		return access;
	}
	public void setAccess(boolean access) {
		this.access = access;
	}
	 
	public boolean isCallAccess() {
		return callAccess;
	}
	public void setCallAccess(boolean callAccess) {
		this.callAccess = callAccess;
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
	
	
	
 

}
