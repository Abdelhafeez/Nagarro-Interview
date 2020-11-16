package com.nagarro.interview.api.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nagarro.interview.api.model.ViewStatementRequest;

public class AmountRangeValidator implements ConstraintValidator<ValidAmountRange, ViewStatementRequest> {

	@Override
	public void initialize(ValidAmountRange constraintAnnotation) {

	}

	@Override
	public boolean isValid(ViewStatementRequest bean, ConstraintValidatorContext context) {
		if (bean.getToAmount() == null && bean.getFromAmount() == null)
			return true;
		if (bean.getToAmount() == null && bean.getFromAmount() != null)
			return false;
		if (bean.getToAmount() != null && bean.getFromAmount() == null)
			return false;
		return (bean.getToAmount().compareTo(bean.getFromAmount()) < 0) ? false : true;
	}

}
