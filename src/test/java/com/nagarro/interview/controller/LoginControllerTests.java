package com.nagarro.interview.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.nagarro.interview.NagrooDemoApplicationTests;
import com.nagarro.interview.authentication.entities.LoginRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginControllerTests extends NagrooDemoApplicationTests {

	@Test
	public void testAccessingWithValidCredentialsThenConcurrentLogin() throws URISyntaxException {
		log.info("Test Login with valid credentials");
		RestTemplate restTemplate = new RestTemplate();
		LoginRequest request = new LoginRequest("testadmin", "adminpassword");
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/auth/login";
		URI uri = new URI(baseUrl);
		ResponseEntity<String> result;
		try {
			result = restTemplate.postForEntity(uri, request, String.class);
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			result = new ResponseEntity<String>(httpClientOrServerExc.getStatusCode());
		}

		// Verify request succeed
		Assert.assertEquals(200, result.getStatusCodeValue());
		String token = StringUtils.substringBetween(result.getBody(), "token\":\"", "\",");

		log.info("Test Login with valid credentials - retry (should be denied)");
		try {
			result = restTemplate.postForEntity(uri, request, String.class);
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			result = new ResponseEntity<String>(httpClientOrServerExc.getStatusCode());
		}

		// Verify request was not successful
		Assert.assertEquals(400, result.getStatusCodeValue());
		log.info("Test Logout with valid token");
		headers.add("Authorization", "Bearer " + token);
		ResponseEntity<String> entity = new TestRestTemplate().exchange(
				"http://localhost:" + randomServerPort + "/api/auth/logout", HttpMethod.GET,
				new HttpEntity<String>(headers), String.class);
		Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());

	}

	@Test
	public void testAccessingWithInValidCredentials() throws URISyntaxException {
		log.info("Test Login with invalid credentials");
		RestTemplate restTemplate = new RestTemplate();
		LoginRequest request = new LoginRequest("admin66", "123456");
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/auth/login";
		URI uri = new URI(baseUrl);
		ResponseEntity<String> result;
		try {
			result = restTemplate.postForEntity(uri, request, String.class);
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			result = new ResponseEntity<String>(httpClientOrServerExc.getStatusCode());
		}

		// Verify request was not succeed
		Assert.assertEquals(401, result.getStatusCodeValue());

	}

	@Test
	public void testAccessingWithInValidMethod() throws URISyntaxException {
		log.info("Test Login with invalid method");
		RestTemplate restTemplate = new RestTemplate();

		final String baseUrl = "http://localhost:" + randomServerPort + "/api/auth/login";
		URI uri = new URI(baseUrl);
		ResponseEntity<String> result;
		try {
			result = restTemplate.getForEntity(uri, String.class);
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			result = new ResponseEntity<String>(httpClientOrServerExc.getStatusCode());
		}

		// Verify request not succeed
		Assert.assertEquals(405, result.getStatusCodeValue());

	}

}
