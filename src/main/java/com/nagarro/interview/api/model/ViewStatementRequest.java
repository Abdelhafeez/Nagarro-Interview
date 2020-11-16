package com.nagarro.interview.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nagarro.interview.api.validator.ValidAmountRange;
import com.nagarro.interview.api.validator.ValidDateRange;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * ViewStatementRequest.java , this class represents model of
 * ViewStatementRequest request
 * 
 * @author Abdalhafeez Bushara
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@ValidAmountRange(message = "{message.validation.invalidDateRange}")
@ValidDateRange(message = "{message.validation.invalidAmountRange}")
public class ViewStatementRequest {
	@NotBlank(message = "{message.validation.accountId.required}")
	@Pattern(regexp = "[\\d]{13}", message = "{message.validation.accountId.invalid}")
	private String accountId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
	private LocalDate fromDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
	private LocalDate toDate;

	@Digits(integer = 12, fraction = 12, message = "{message.validation.fromAmount.invalid}")
	private BigDecimal fromAmount;

	@Digits(integer = 12, fraction = 12, message = "{message.validation.toAmount.invalid}")
	private BigDecimal toAmount;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public BigDecimal getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
	}

	public BigDecimal getToAmount() {
		return toAmount;
	}

	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}

	@Override
	public String toString() {
		return "ViewStatementRequest [accountId=" + accountId + ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", fromAmount=" + fromAmount + ", toAmount=" + toAmount + "]";
	}

}