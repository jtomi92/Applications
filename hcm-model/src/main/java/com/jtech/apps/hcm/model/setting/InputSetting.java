package com.jtech.apps.hcm.model.setting;

import java.util.List;

public class InputSetting {
	
	private Integer inputId;
	private String inputName;
	private String startTimer;
	private String endTimer;
	private boolean timerEnabled;
	private String valuePostfix;
	private String sampleRate;
	private String creationDate;
	private String lastUpdateDate;
	
	private List<ProductTriggerSetting> productTriggerSettings;
	
	public Integer getInputId() {
		return inputId;
	}
	public void setInputId(Integer inputId) {
		this.inputId = inputId;
	}
	public String getInputName() {
		return inputName;
	}
	public void setInputName(String inputName) {
		this.inputName = inputName;
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
	
	public boolean isTimerEnabled() {
		return timerEnabled;
	}
	public void setTimerEnabled(boolean timerEnabled) {
		this.timerEnabled = timerEnabled;
	}
	public String getValuePostfix() {
		return valuePostfix;
	}
	public void setValuePostfix(String valuePostfix) {
		this.valuePostfix = valuePostfix;
	}
	public String getSampleRate() {
		return sampleRate;
	}
	public void setSampleRate(String sampleRate) {
		this.sampleRate = sampleRate;
	}
	public List<ProductTriggerSetting> getProductTriggerSettings() {
		return productTriggerSettings;
	}
	public void setProductTriggerSettings(List<ProductTriggerSetting> productTriggerSettings) {
		this.productTriggerSettings = productTriggerSettings;
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
