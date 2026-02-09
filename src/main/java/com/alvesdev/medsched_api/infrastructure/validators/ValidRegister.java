package com.alvesdev.medsched_api.infrastructure.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = RegisterValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRegister {
    String message() default "Invalid registration data for the selected profile type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
