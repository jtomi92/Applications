package com.jtech.apps.hcm.form;

import org.hibernate.validator.constraints.NotEmpty;

public class ContactForm {

	@NotEmpty(message = "Name is required")
	private String name;
	
	@NotEmpty(message = "Email is required")
	private String email;
	
	@NotEmpty(message = "Message is required")
	private String message;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
