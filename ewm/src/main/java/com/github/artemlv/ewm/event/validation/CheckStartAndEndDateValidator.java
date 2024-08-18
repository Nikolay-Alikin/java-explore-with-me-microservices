package com.github.artemlv.ewm.event.validation;

import com.github.artemlv.ewm.event.model.PublicParameter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class CheckStartAndEndDateValidator implements ConstraintValidator<EventStartDateBeforeEndDate, PublicParameter> {
    @Override
    public boolean isValid(final PublicParameter publicParameter, final ConstraintValidatorContext context) {
        return Objects.nonNull(publicParameter.getRangeStart())
                && Objects.nonNull(publicParameter.getRangeEnd())
                && publicParameter.getRangeStart().isBefore(publicParameter.getRangeEnd());
    }
}
