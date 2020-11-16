package com.nagarro.interview.api.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nagarro.interview.api.model.ViewStatementRequest;




public class DateRangeValidator implements ConstraintValidator<ValidDateRange, ViewStatementRequest> {

	@Override
	public void initialize(ValidDateRange constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(ViewStatementRequest bean, ConstraintValidatorContext context) {
		if(bean.getToDate()==null && bean.getFromDate()==null)
			return true;
		if(bean.getToDate()==null && bean.getFromDate()!=null)
			return false;
		if(bean.getToDate()!=null && bean.getFromDate()==null)
				return false;
	    return (bean.getToDate().compareTo(bean.getFromDate())<0)?false:true;
	}

}
