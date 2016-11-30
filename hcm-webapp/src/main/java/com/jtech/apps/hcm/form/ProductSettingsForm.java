package com.jtech.apps.hcm.form;

import java.util.List;
 

public class ProductSettingsForm {
 
	private List<String> relayIds;
	private List<String> relayNames;
	private List<String> delays;
	private List<String> impulses;
	private List<List<String>> startWeekDays;
	private List<List<String>> endWeekDays;
	private List<String> timerEnabled;
	private List<String> startTimers;
	private List<String> endTimers;
	
	public List<String> getRelayIds() {
		return relayIds;
	}
	public void setRelayIds(List<String> relayIds) {
		this.relayIds = relayIds;
	}
	public List<String> getRelayNames() {
		return relayNames;
	}
	public void setRelayNames(List<String> relayNames) {
		this.relayNames = relayNames;
	}
	public List<String> getDelays() {
		return delays;
	}
	public void setDelays(List<String> delays) {
		this.delays = delays;
	}
	public List<String> getImpulses() {
		return impulses;
	}
	public void setImpulses(List<String> impulses) {
		this.impulses = impulses;
	}
	 
	public List<List<String>> getStartWeekDays() {
		return startWeekDays;
	}
	public void setStartWeekDays(List<List<String>> startWeekDays) {
		this.startWeekDays = startWeekDays;
	}
	public List<List<String>> getEndWeekDays() {
		return endWeekDays;
	}
	public void setEndWeekDays(List<List<String>> endWeekDays) {
		this.endWeekDays = endWeekDays;
	}
	public List<String> getTimerEnabled() {
		return timerEnabled;
	}
	public void setTimerEnabled(List<String> timerEnabled) {
		this.timerEnabled = timerEnabled;
	}
	public List<String> getStartTimers() {
		return startTimers;
	}
	public void setStartTimers(List<String> startTimers) {
		this.startTimers = startTimers;
	}
	public List<String> getEndTimers() {
		return endTimers;
	}
	public void setEndTimers(List<String> endTimers) {
		this.endTimers = endTimers;
	}
	
	 
 
	 
	
	
}
