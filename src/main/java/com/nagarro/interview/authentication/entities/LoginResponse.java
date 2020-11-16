package com.nagarro.interview.authentication.entities;

import com.nagarro.interview.utils.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * LoginResponse.java , this class represents model of LoginResponse
 * 
 * @author Abdalhafeez Bushara
 *
 */
@Data
@AllArgsConstructor
public class LoginResponse {
	private String token;
	private String type = Constants.APP_TOKEN_TYPE;
	private String message;

	public LoginResponse(String accessToken, String message) {
		this.token = accessToken;
		this.message = message;
	}
}