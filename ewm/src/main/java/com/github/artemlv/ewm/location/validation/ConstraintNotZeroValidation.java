package com.github.artemlv.ewm.location.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class ConstraintNotZeroValidation implements ConstraintValidator<ConstraintNotZero, Long> {
    @Override
    public boolean isValid(Long number, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.nonNull(number) && number != 0;
    }
}
