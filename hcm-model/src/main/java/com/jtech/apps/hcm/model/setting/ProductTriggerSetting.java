package com.jtech.apps.hcm.model.setting;

public class ProductTriggerSetting {

	private Integer triggerId;
	private Integer triggerRelayId;
	private boolean triggerEnabled;
	private String triggerValue;
	private String triggerState;
	private String triggerAction;
	private String lastUpdateDate;
	

	public Integer getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(Integer triggerId) {
		this.triggerId = triggerId;
	}
	public Integer getTriggerRelayId() {
		return triggerRelayId;
	}
	public void setTriggerRelayId(Integer triggerRelayId) {
		this.triggerRelayId = triggerRelayId;
	}
	public boolean isTriggerEnabled() {
		return triggerEnabled;
	}
	public void setTriggerEnabled(boolean triggerEnabled) {
		this.triggerEnabled = triggerEnabled;
	}
	public String getTriggerValue() {
		return triggerValue;
	}
	public void setTriggerValue(String triggerValue) {
		this.triggerValue = triggerValue;
	}
	public String getTriggerState() {
		return triggerState;
	}
	public void setTriggerState(String trriggerState) {
		this.triggerState = trriggerState;
	}
	public String getTriggerAction() {
		return triggerAction;
	}
	public void setTriggerAction(String triggerAction) {
		this.triggerAction = triggerAction;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	
	
	
	
}
