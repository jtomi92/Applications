package com.jtech.apps.hcm.model.setting;

public class ProductUser {

	private Integer userId;
	private String userName;
	private String privilige;
	private boolean selected;
	
	 
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPrivilige() {
		return privilige;
	}
	public void setPrivilige(String privilige) {
		this.privilige = privilige;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}
