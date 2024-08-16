package com.github.artemlv.ewm.event.validation;

import com.github.artemlv.ewm.event.model.AdminParameter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

public class CheckStartAndEndDateValidator implements ConstraintValidator<EventStartDateBeforeEndDate, AdminParameter> {
    @Override
    public boolean isValid(final AdminParameter adminParameter, final ConstraintValidatorContext context) {
        if (ObjectUtils.isEmpty(adminParameter.getRangeStart()) || ObjectUtils.isEmpty(adminParameter.getRangeEnd())) {
            return false;
        }

        return adminParameter.getRangeStart().isBefore(adminParameter.getRangeEnd());
    }
}
