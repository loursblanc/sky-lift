package fr.apsprevoyance.skylift.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.apsprevoyance.skylift.constants.AnnotationMessages;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ValidSkiLiftDateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSkiLiftDate {
    String message() default AnnotationMessages.Date.TOO_OLD;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}