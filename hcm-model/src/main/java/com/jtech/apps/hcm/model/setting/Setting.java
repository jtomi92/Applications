package com.jtech.apps.hcm.model.setting;

import java.util.LinkedList;
import java.util.List;



public class Setting {
	
	private Integer settingId;
	private String settingName;
	private boolean selected;
	
	private List<RelaySetting> relaySettings = new LinkedList<RelaySetting>();
	private List<InputSetting> inputSettings = new LinkedList<InputSetting>();
	
	private String creationDate;
	private String lastUpdateDate;
	
	public Integer getSettingId() {
		return settingId;
	}
	public void setSettingId(Integer settingId) {
		this.settingId = settingId;
	}
	public String getSettingName() {
		return settingName;
	}
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public List<RelaySetting> getRelaySettings() {
		return relaySettings;
	}
	public void setRelaySettings(List<RelaySetting> relaySettings) {
		this.relaySettings = relaySettings;
	}
	public List<InputSetting> getInputSettings() {
		return inputSettings;
	}
	public void setInputSettings(List<InputSetting> inputSettings) {
		this.inputSettings = inputSettings;
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
