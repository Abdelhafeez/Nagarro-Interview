package com.nagarro.interview.api.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = { AmountRangeValidator.class })
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAmountRange {

	String message() default "Amount Range is invalid, either one is unspecified or min is greater than max";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		ValidAmountRange[] value();
	}

}
