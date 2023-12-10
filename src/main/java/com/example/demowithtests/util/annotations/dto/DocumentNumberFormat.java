package com.example.demowithtests.util.annotations.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation verifies that the document number is set correctly.
 * Throws: MethodArgumentNotValidException if the string does not meet the conditions.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DocumentNumberFormatValidator.class)
public @interface DocumentNumberFormat {
    String message() default "Number must be start from 0";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
