package fr.apsprevoyance.skylift.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.enums.ValidationContextType;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final List<String> validationErrors;
    private final String modelName;
    private final ValidationContextType contextType;

    public ValidationException(String modelName, ValidationContextType contextType, List<String> validationErrors) {
        super(String.format(ErrorMessageConstants.Validation.ERROR_MESSAGE, modelName,
                String.join(", ", validationErrors)));
        this.modelName = modelName;
        this.contextType = contextType;
        this.validationErrors = new ArrayList<>(validationErrors);
    }

    public ValidationException(String modelName, ValidationContextType contextType, String errorMessage) {
        this(modelName, contextType, Collections.singletonList(errorMessage));
    }

    public ValidationException(String modelName, String errorMessage) {
        this(modelName, ValidationContextType.MODEL, Collections.singletonList(errorMessage));
    }

    public List<String> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    public String getModelName() {
        return modelName;
    }

    public ValidationContextType getContextType() {
        return contextType;
    }
}
