package com.nagarro.interview.authentication.entities;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * LoginRequest.java , this class represents model of login request
 * 
 * @author Abdalhafeez Bushara
 *
 */
@Data
public class LoginRequest {
	@NotBlank(message = "{message.validation.username.required}")
	@Size(min = 3, max = 60, message = "{message.validation.username.invalid}")
	private String username;

	@NotBlank(message = "{message.validation.password.required}")
	@Size(min = 4, max = 40, message = "{message.validation.password.invalid}")
	private String password;

	@Override
	public String toString() {
		return "LoginRequest [username=" + username + ", password=" + "******" + "]";
	}

	public LoginRequest(String user, String pass) {
		this.username = user;
		this.password = pass;
	}

}