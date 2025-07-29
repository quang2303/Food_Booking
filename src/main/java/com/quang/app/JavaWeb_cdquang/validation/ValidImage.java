package com.quang.app.JavaWeb_cdquang.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImageFileValidator.class})
public @interface ValidImage {
	String message() default "Invalid image file";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
