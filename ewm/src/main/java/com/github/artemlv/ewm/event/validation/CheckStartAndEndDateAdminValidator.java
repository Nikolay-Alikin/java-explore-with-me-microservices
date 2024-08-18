package com.github.artemlv.ewm.event.validation;

import com.github.artemlv.ewm.event.model.AdminParameter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class CheckStartAndEndDateAdminValidator implements ConstraintValidator<EventStartDateBeforeEndDate, AdminParameter> {
    @Override
    public boolean isValid(final AdminParameter adminParameter, final ConstraintValidatorContext context) {
        return Objects.nonNull(adminParameter.getRangeStart())
                && Objects.nonNull(adminParameter.getRangeEnd())
                && adminParameter.getRangeStart().isBefore(adminParameter.getRangeEnd());
    }
}
