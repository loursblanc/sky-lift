package fr.apsprevoyance.skylift.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.enums.ValidationContextType;

@Tag(TestTag.EXCEPTION)
class ValidationExceptionTest {

    @Test
    void testConstructorWithMultipleErrors() {

        String modelName = "SkiLift";
        ValidationContextType contextType = ValidationContextType.MODEL;
        List<String> errors = Arrays.asList("Error 1", "Error 2");
        String expectedMessage = String.format(ErrorMessageConstants.Validation.ERROR_MESSAGE, modelName,
                String.join(", ", errors));

        ValidationException exception = new ValidationException(modelName, contextType, errors);

        assertTrue(exception instanceof RuntimeException);
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(modelName, exception.getModelName());
        assertEquals(contextType, exception.getContextType());
        assertEquals(errors, exception.getValidationErrors());
    }

    @Test
    void testConstructorWithSingleErrorString() {

        String modelName = "Sport";
        ValidationContextType contextType = ValidationContextType.BUSINESS_RULE;
        String errorMessage = "Invalid sport type";
        String expectedMessage = String.format(ErrorMessageConstants.Validation.ERROR_MESSAGE, modelName, errorMessage);

        ValidationException exception = new ValidationException(modelName, contextType, errorMessage);

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(modelName, exception.getModelName());
        assertEquals(contextType, exception.getContextType());
        assertEquals(Collections.singletonList(errorMessage), exception.getValidationErrors());
    }

    @Test
    void testSimpleConstructor() {

        String modelName = "User";
        String errorMessage = "Invalid user data";
        String expectedMessage = String.format(ErrorMessageConstants.Validation.ERROR_MESSAGE, modelName, errorMessage);

        ValidationException exception = new ValidationException(modelName, errorMessage);

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(modelName, exception.getModelName());
        assertEquals(ValidationContextType.MODEL, exception.getContextType());
        assertEquals(Collections.singletonList(errorMessage), exception.getValidationErrors());
    }

    @Test
    void testValidationErrorsImmutability() {

        List<String> mutableErrors = new ArrayList<>();
        mutableErrors.add("Error 1");
        ValidationException exception = new ValidationException("Test", ValidationContextType.MODEL, mutableErrors);

        mutableErrors.add("Error 2");

        assertEquals(1, exception.getValidationErrors().size());
        assertThrows(UnsupportedOperationException.class, () -> exception.getValidationErrors().add("New Error"));
    }
}