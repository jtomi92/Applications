package com.jtech.apps.hcm.model.setting;

import java.util.List;

public class RelaySetting {
	
	private Integer relayId;
	private Integer moduleId;
	private String relayName;
	private String relayStatus;
	private String startWeekDays;
	private String endWeekDays;
	private String startTimer;
	private String endTimer;
	private String delay;
	private boolean impulseMode;
	private boolean relayEnabled;
	private boolean delayEnabled;
	private boolean timerEnabled;
	
	private List<ProductControlSetting> productControlSettings;
	
	private String creationDate;
	private String lastUpdateDate;
	
	public void addProductControlSetting(ProductControlSetting productControlSetting){
		productControlSettings.add(productControlSetting);
	}
	
	 
	public Integer getRelayId() {
		return relayId;
	}


	public void setRelayId(Integer relayId) {
		this.relayId = relayId;
	}


	public Integer getModuleId() {
		return moduleId;
	}


	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}


	public String getRelayName() {
		return relayName;
	}


	public void setRelayName(String relayName) {
		this.relayName = relayName;
	}


	public String getRelayStatus() {
		return relayStatus;
	}


	public void setRelayStatus(String relayStatus) {
		this.relayStatus = relayStatus;
	}


	public String getStartWeekDays() {
		return startWeekDays;
	}


	public void setStartWeekDays(String startWeekDays) {
		this.startWeekDays = startWeekDays;
	}


	public String getEndWeekDays() {
		return endWeekDays;
	}


	public void setEndWeekDays(String endWeekDays) {
		this.endWeekDays = endWeekDays;
	}


	public String getStartTimer() {
		return startTimer;
	}


	public void setStartTimer(String startTimer) {
		this.startTimer = startTimer;
	}


	public String getEndTimer() {
		return endTimer;
	}


	public void setEndTimer(String endTimer) {
		this.endTimer = endTimer;
	}


	public String getDelay() {
		return delay;
	}


	public void setDelay(String delay) {
		this.delay = delay;
	}


	public boolean isImpulseMode() {
		return impulseMode;
	}


	public void setImpulseMode(boolean impulseMode) {
		this.impulseMode = impulseMode;
	}


	public boolean isRelayEnabled() {
		return relayEnabled;
	}


	public void setRelayEnabled(boolean relayEnabled) {
		this.relayEnabled = relayEnabled;
	}


	public boolean isDelayEnabled() {
		return delayEnabled;
	}


	public void setDelayEnabled(boolean delayEnabled) {
		this.delayEnabled = delayEnabled;
	}


	public boolean isTimerEnabled() {
		return timerEnabled;
	}


	public void setTimerEnabled(boolean timerEnabled) {
		this.timerEnabled = timerEnabled;
	}


	public List<ProductControlSetting> getProductControlSettings() {
		return productControlSettings;
	}


	public void setProductControlSettings(List<ProductControlSetting> productControlSettings) {
		this.productControlSettings = productControlSettings;
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


	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
 

}
