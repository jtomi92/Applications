package com.jtech.apps.hcm.form;
 
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class UserForm {

	@NotEmpty(message = "First Name required")
	private String firstName;
	
	@NotEmpty(message = "Last Name Required")
	private String lastName;
	
	@NotEmpty(message = "Email is required")
	@Email
	private String email;
	
    @Size(min=5, max=30, message = "Password is too short")
	private String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
