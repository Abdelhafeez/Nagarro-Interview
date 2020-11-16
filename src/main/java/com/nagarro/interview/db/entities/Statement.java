package com.nagarro.interview.db.entities;
//import com.sun.jersey.spi.container.servlet.ServletContainer;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "statement")
@JsonIgnoreProperties("{id}")
@NoArgsConstructor
public class Statement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;
	@Column(name = "account_id")
	private String accountId;
	@Column(name = "datefield")
	private String date;
	@Column(name = "amount")
	private BigDecimal amount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
