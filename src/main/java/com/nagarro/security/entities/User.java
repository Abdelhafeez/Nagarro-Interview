package com.nagarro.security.entities;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class User {

	private Long id;

	@NotBlank
	@Size(min = 3, max = 50)
	private String name;

	@NotBlank
	@Size(min = 3, max = 50)
	private String username;

	

	@NotBlank
	@Size(min = 6, max = 100)
	private String password;

	private Set<Role> roles = new HashSet<>();

	

	public User(String username, Set<Role> role, String password) {
		this.name = username;
		this.username = username;
		this.roles = role;
		this.password = password;
	}

}