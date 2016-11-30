package com.jtech.apps.hcm.form;

import java.util.List;

public class ProductUserForm {
	
	private List<String> relayAccess; 
	private List<String> callAccess;
	public List<String> getRelayAccess() {
		return relayAccess;
	}
	public void setRelayAccess(List<String> relayAccess) {
		this.relayAccess = relayAccess;
	}
	public List<String> getCallAccess() {
		return callAccess;
	}
	public void setCallAccess(List<String> callAccess) {
		this.callAccess = callAccess;
	}
	
	
}
