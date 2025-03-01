package fr.apsprevoyance.skylift.validation;

import java.time.LocalDate;

import fr.apsprevoyance.skylift.constants.ValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidSkiLiftDateValidator implements ConstraintValidator<ValidSkiLiftDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true;
        }
        return !date.isBefore(ValidationConstants.FIRST_SKILIFT_DATE);
    }
}