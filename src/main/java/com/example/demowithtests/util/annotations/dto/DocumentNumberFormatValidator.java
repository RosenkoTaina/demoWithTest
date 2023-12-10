package com.example.demowithtests.util.annotations.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentNumberFormatValidator implements ConstraintValidator<DocumentNumberFormat, String> {

    @Override
    public void initialize(DocumentNumberFormat constraintAnnotation) {
    }
    @Override
    public boolean isValid(String number, ConstraintValidatorContext constraintValidatorContext) {
        if (number == null)
            return true;
        return number.startsWith(String.valueOf(0));
    }
}
