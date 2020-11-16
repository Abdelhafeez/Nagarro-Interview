package com.nagarro.interview.authentication.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.interview.api.controllers.BaseController;
import com.nagarro.interview.api.error.ConcurrentLoginException;
import com.nagarro.interview.authentication.entities.LoginRequest;
import com.nagarro.interview.authentication.entities.LoginResponse;
import com.nagarro.interview.security.jwt.APITokenProvider;
import com.nagarro.interview.security.jwt.InMemoryTokenValut;
import com.nagarro.interview.security.services.AuthService;
import com.nagarro.interview.security.services.UserDetailsServiceImpl;
import com.nagarro.interview.security.services.UserPrinciple;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController extends BaseController {

	@Autowired
	UserDetailsServiceImpl userService;

	@Autowired
	private APITokenProvider tokenProvider;

	@Autowired
	AuthService authService;

	@Autowired
	InMemoryTokenValut tokenVault;

	@ApiOperation(value = "Login to get brear token used for rest of apis", response = Iterable.class, tags = "login")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess"),
			@ApiResponse(code = 401, message = "Not authorized | Failed login or no token"),
			@ApiResponse(code = 400, message = "Format Erorr") })

	@PostMapping("/login")
	public ResponseEntity<? extends Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("/login -> loginRequest : \n  " + loginRequest);

		Authentication authentication = authService.authenticate(loginRequest.getUsername(),
				loginRequest.getPassword());

		String token = tokenVault.getTokenByUserName(loginRequest.getUsername());
		if (token == null || !tokenProvider.validateToken(token)) {
			String jwt = authService.generateJwtToken(authentication);
			tokenVault.putToken(loginRequest.getUsername(), jwt);

			return ResponseEntity.ok(new LoginResponse(jwt, "Successful Log-In"));
		}
		throw new ConcurrentLoginException(loginRequest.getUsername());
	}

	@ApiOperation(value = "Logout", response = Iterable.class, tags = "logout")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess"),
			@ApiResponse(code = 401, message = "Not authorized | Failed login or no token"),
			@ApiResponse(code = 400, message = "Format Erorr") })
	@GetMapping("/logout")
	@ApiImplicitParam(name = "Authorization", value = "Bearer ", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
	public ResponseEntity<? extends Object> logout() {
		UserPrinciple user = (UserPrinciple) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		tokenVault.removeTokenByKey(user.getUsername());
		return ResponseEntity.ok(new LoginResponse(null, null, "Successful Log-Out"));
	}

}