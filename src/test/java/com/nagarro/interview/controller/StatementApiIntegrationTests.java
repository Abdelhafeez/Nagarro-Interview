package com.nagarro.interview.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.interview.SimpleTestCase;
import com.nagarro.interview.api.model.ViewStatementRequest;
import com.nagarro.interview.api.model.ViewStatementResponse;
import com.nagarro.interview.authentication.entities.LoginRequest;
import com.nagarro.interview.db.entities.Statement;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class StatementApiIntegrationTests {
	@LocalServerPort
	protected
    int randomServerPort;
	List<SimpleTestCase> cases;
	String adminToken;
	String userToken;

	/*
	 * Test with No Bearer Token Expected : Unauthorized
	 */
	@Test
	public void testingAPIUseCases() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		log.info("(1)Testing Unauthorized access");
		RestTemplate restTemplate = new RestTemplate();
		ViewStatementRequest request = new ViewStatementRequest("1234", null, null, null, null);
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		HttpEntity<ViewStatementRequest> requestObj = new HttpEntity<>(request, headers);
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/fetch";
		URI uri = new URI(baseUrl);
		ResponseEntity<ViewStatementResponse> result;
		try {
			result = restTemplate.postForEntity(uri, requestObj, ViewStatementResponse.class);
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			result = new ResponseEntity<ViewStatementResponse>(httpClientOrServerExc.getStatusCode());
		}

		// Verify request succeed
		Assert.assertEquals(401, result.getStatusCodeValue());
		log.info("(1)Testing Unauthorized access ends");
		log.info("-------------------------------------------");

		int i = 1;
		for (SimpleTestCase test : cases) {

			i++;

			runTest(test, i,uri);

		}

	}

	
	void runTest(SimpleTestCase test, int i,URI uri) throws JsonParseException, JsonMappingException, IOException {
		ResponseEntity<String> result;
		RestTemplate restTemplate = new RestTemplate();

		log.info("(" + i + ")Testing with " + test.getTokenType() + " token" + " and " + test.getRequestTestCase()
				+ "expected http response is " + test.getExpectedHttpResponse());
		HttpHeaders newHeader = new HttpHeaders();
		newHeader.add("content-type", "application/json");

		if (test.getTokenType().contentEquals("admin"))
			newHeader.add("Authorization", "Bearer " + adminToken);
		else
			newHeader.add("Authorization", "Bearer " + userToken);
		HttpEntity<ViewStatementRequest> caseRequest = new HttpEntity<>(test.getRequestTestCase(), newHeader);

		try {
			result = restTemplate.postForEntity(uri, caseRequest, String.class);
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			result = new ResponseEntity<String>(httpClientOrServerExc.getStatusCode());
		}

		// Verify request succeed
		Assert.assertEquals(test.getExpectedHttpResponse(), result.getStatusCodeValue());
		if(result.getStatusCodeValue()==200)
		{			ViewStatementRequest r =test.getRequestTestCase();
			ObjectMapper objectMapper = new ObjectMapper();
			ViewStatementResponse respObj = objectMapper.readValue(result.getBody().toString(), ViewStatementResponse.class);	
			//make sure that the data is consistent
			if(respObj.getStatement()!=null &&respObj.getStatement().size()>0 )
			{
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				LocalDate beforePeriod = LocalDate.now().minusMonths(3);

				List<Statement> st=respObj.getStatement();
			      List<Statement> results = st.stream()               
			              .filter(statement ->
			              (test.getRequestTestCase().getFromAmount()==null?true:(r.getFromAmount().compareTo(statement.getAmount())>0
			            		  && r.getFromAmount().compareTo(statement.getAmount())<0
			            		  )
			              //Filter by date
			              &&
			              (	      
			            		  test.getRequestTestCase().getFromDate()==null?
			            	(beforePeriod.compareTo(LocalDate.parse(statement.getDate(), formatter))<0)
			            	:
			            	  
			            	  (r.getFromDate()).compareTo(LocalDate.parse(statement.getDate(), formatter))>0
			            		  && r.getFromDate().compareTo(LocalDate.parse(statement.getDate(), formatter))<0
			            		  ))
			            		     
			              ).collect(Collectors.toList());             

			  assert(st.equals(results));
			}
		}
		log.info("(" + i + ")Test ended");
		log.info("-------------------------------------------");
	}

	@Before
	public void setup() throws InterruptedException, URISyntaxException {
		cases = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

		cases.add(new SimpleTestCase(new ViewStatementRequest("0012250016001", null, null, null, null), 200, "user"));
		cases.add(new SimpleTestCase(
				new ViewStatementRequest("0012250019001", null, null, BigDecimal.valueOf(2d), BigDecimal.valueOf(300d)),
				401, "user"));
		cases.add(new SimpleTestCase(new ViewStatementRequest("123456", null, null, null, null), 400, "admin"));
		cases.add(new SimpleTestCase(
				new ViewStatementRequest("0012250016001", null, null, BigDecimal.valueOf(2d), BigDecimal.valueOf(3d)),
				200, "admin"));
		cases.add(new SimpleTestCase(
				new ViewStatementRequest("0012250016001", null, null, BigDecimal.valueOf(6d), BigDecimal.valueOf(3d)),
				400, "admin"));
		cases.add(new SimpleTestCase(
				new ViewStatementRequest("0012250016001", LocalDate.parse("01.01.1993", formatter), LocalDate.parse("01.01.2020", formatter), null, null),
				200, "admin"));
		// wait for tokens created by other test case (loginControllerTests.java) to expire
		Thread.sleep(5000);
		adminToken = getToken(new LoginRequest("testadmin", "adminpassword"));
		userToken = getToken(new LoginRequest("testUser", "userpassword"));

		System.out.println(adminToken);
		System.out.println("===================================");

	}

	String getToken(LoginRequest request) throws URISyntaxException {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/auth/login";
		URI uri = new URI(baseUrl);
		ResponseEntity<String> result;
		result = restTemplate.postForEntity(uri, request, String.class);
		System.err.println(result);
		String x = result.getBody();
		return StringUtils.substringBetween(x, "token\":\"", "\",");

	}
	
	@After 
	public void tearDown()
	{
		 String url = "http://localhost:" + randomServerPort + "/api/auth/logout";

		try {
			logOut(adminToken, url);
			logOut(userToken, url);

			
		}
		catch(Exception e) {}
	}
	
	
	public static String getToken(LoginRequest request,String url) throws URISyntaxException {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		URI uri = new URI(url);
		ResponseEntity<String> result;
		result = restTemplate.postForEntity(uri, request, String.class);
		System.err.println(result);
		String x = result.getBody();
		return StringUtils.substringBetween(x, "token\":\"", "\",");

	}
	
	public static HttpStatus logOut(String token,String url)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		ResponseEntity<String> entity = new TestRestTemplate().exchange(
				url, HttpMethod.GET,
				new HttpEntity<String>(headers), String.class);
		return entity.getStatusCode();
	}
	 

}
