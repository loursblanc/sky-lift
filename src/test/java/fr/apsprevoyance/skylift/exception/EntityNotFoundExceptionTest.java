package fr.apsprevoyance.skylift.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.TestTag;

@Tag(TestTag.EXCEPTION)
class EntityNotFoundExceptionTest {

    @Test
    void testExceptionMessageAndFields() {
        // Given
        String entityName = "SkiLift";
        String identifier = "123";
        String expectedMessage = String.format(ErrorMessageConstants.Validation.ENTITY_NOT_FOUND, entityName,
                identifier);

        // When
        EntityNotFoundException exception = new EntityNotFoundException(entityName, identifier);

        // Then
        assertTrue(exception instanceof RuntimeException);
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(entityName, exception.getEntityName());
        assertEquals(identifier, exception.getIdentifier());
    }
}