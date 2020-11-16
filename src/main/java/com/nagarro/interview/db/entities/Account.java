package com.nagarro.interview.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Account.java This is a model class represents Account entity
 * 
 * @author Abdalhafeez Bushara
 *
 */

@Entity
@Table(name = "account")
@JsonIgnoreProperties("{id}")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;
	@Column(name = "account_type")
	private String accountType;
	@Column(name = "account_number")
	private String accountNumber;
	
	
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountNumber() {
		return (new BCryptPasswordEncoder()).encode(accountNumber);
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "Account ;[id=" + id + ", accountType=" + accountType + ", accountNumber=" + accountNumber + "]";
	}
	
	

}
