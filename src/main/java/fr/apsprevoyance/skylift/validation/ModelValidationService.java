package fr.apsprevoyance.skylift.validation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.groups.Default;

@Service
public class ModelValidationService {
    private static final Logger log = LoggerFactory.getLogger(ModelValidationService.class);

    private final Validator validator;

    public ModelValidationService() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public <T> List<String> checkWithAnnotations(T object, Class<?>... groups) {

        if (groups == null || groups.length == 0) {
            groups = new Class<?>[] { Default.class };
        }

        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);

        return violations.stream().map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
    }

    public <T> void checkAndThrowIfInvalid(T object, String entityName, Class<?>... groups) {
        List<String> errors = checkWithAnnotations(object, groups);

        if (!errors.isEmpty()) {
            log.warn(ErrorMessageConstants.Logs.VALIDATION_FAILED, entityName, object.getClass().getPackage().getName(),
                    String.join(", ", errors));

            throw new ValidationException(entityName, ValidationContextType.MODEL, errors);
        }
    }
}
