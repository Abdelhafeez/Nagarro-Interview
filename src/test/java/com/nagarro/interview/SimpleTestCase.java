package com.nagarro.interview;

import com.nagarro.interview.api.model.ViewStatementRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class SimpleTestCase {
	ViewStatementRequest requestTestCase;
	int expectedHttpResponse;
	String tokenType;
}



