package com.nagarro.interview.api.model;

import java.util.List;

import com.nagarro.interview.db.entities.Account;
import com.nagarro.interview.db.entities.Statement;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * ViewStatementResponse.java , this class represents model of ViewStatementResponse 
 * 
 * @author Abdalhafeez Bushara
 *
 */
@Data
@AllArgsConstructor
public class ViewStatementResponse {
    private List<Statement> statement;
    private Account account;
    private String message;
    
}