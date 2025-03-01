package fr.apsprevoyance.skylift.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.TestTag;

@Tag(TestTag.EXCEPTION)
class DuplicateEntityExceptionTest {

    @Test
    void testExceptionMessageAndFields() {

        String entityName = "User";
        String field = "email";
        String value = "test@example.com";
        String expectedMessage = String.format(ErrorMessageConstants.Validation.ERROR_DUPLICATE_MESSAGE, entityName,
                field, value);

        DuplicateEntityException exception = new DuplicateEntityException(entityName, field, value);

        assertTrue(exception instanceof RuntimeException);
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(entityName, exception.getEntityName());
        assertEquals(field, exception.getField());
        assertEquals(value, exception.getValue());
    }
}