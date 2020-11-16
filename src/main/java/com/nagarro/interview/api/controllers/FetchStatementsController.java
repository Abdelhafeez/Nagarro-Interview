package com.nagarro.interview.api.controllers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.interview.api.error.UserCanNotFilterParametersException;
import com.nagarro.interview.api.model.ViewStatementRequest;
import com.nagarro.interview.api.model.ViewStatementResponse;
import com.nagarro.interview.api.services.FetchStatmentsService;
import com.nagarro.interview.security.jwt.InMemoryTokenValut;
import com.nagarro.interview.security.services.UserPrinciple;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class FetchStatementsController extends BaseController {
	@Autowired
	InMemoryTokenValut tokenVault;

	@PersistenceContext
	EntityManager factory;

	@Autowired
	FetchStatmentsService fetchingService;
    @ApiOperation(value = "Get Statements by account and citeria", response = Iterable.class, tags = "fetch")
	@PostMapping("/api/fetch")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @ApiImplicitParam(name = "Authorization", value = "Bearer ", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
	public ViewStatementResponse getStatements(@Valid @RequestBody ViewStatementRequest request) {
		log.info("/api/fetch " + request);
        UserPrinciple user = (UserPrinciple) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if (request.getFromAmount() != null || request.getToAmount() != null || request.getFromDate() != null
				|| request.getToDate() != null)
			if (!checkIfHasAuthorityToSpecifyParams(user))
				throw new UserCanNotFilterParametersException(user.getUsername());
		ViewStatementResponse response = fetchingService.fetch(request);
		log.info("/api/fetch response:" + response);
		return response;

	}

	boolean checkIfHasAuthorityToSpecifyParams(UserPrinciple user) {
		System.err.println(user.getAuthorities());
		boolean check= user.getAuthorities().toString().contains("ROLE_ADMIN");
		log.info("check is "+check);
		return check;

	}

}