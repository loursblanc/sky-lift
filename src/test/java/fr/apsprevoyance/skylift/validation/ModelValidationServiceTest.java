package fr.apsprevoyance.skylift.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.AnnotationMessages;
import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.exception.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

@Tag("validation")
class ModelValidationServiceTest {

    private ModelValidationService modelValidationService;

    @BeforeEach
    void setUp() {
        modelValidationService = new ModelValidationService();
    }

    static class TestEntity {
        @NotNull(message = AnnotationMessages.Id.NULL, groups = Default.class)
        private Long id;

        @NotBlank(message = AnnotationMessages.Name.EMPTY, groups = Default.class)
        private String name;

        public TestEntity(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Test
    void testValidationSuccess() {
        TestEntity entity = new TestEntity(TestConstants.Sport.VALID_ID, TestConstants.Sport.VALID_NAME);

        List<String> errors = modelValidationService.checkWithAnnotations(entity);

        assertTrue(errors.isEmpty(), TestConstants.ValidationTestMessages.EMPTY_ERROR_LIST_FOR_VALID);
    }

    @Test
    void testValidationFailureWithoutGroup() {
        TestEntity entity = new TestEntity(TestConstants.Sport.VALID_ID, "");

        List<String> errors = modelValidationService.checkWithAnnotations(entity);

        assertFalse(errors.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, errors.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(errors.get(0).contains(AnnotationMessages.Name.EMPTY),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void testValidationFailureWithException() {
        TestEntity entity = new TestEntity(null, "");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> modelValidationService.checkAndThrowIfInvalid(entity, "TestEntity"));

        assertEquals(2, exception.getValidationErrors().size(),
                TestConstants.ValidationTestMessages.TWO_ERROR_IN_EXCEPTION);
        assertTrue(
                exception.getValidationErrors().stream().anyMatch(error -> error.contains(AnnotationMessages.Id.NULL)),
                TestConstants.ValidationTestMessages.ERROR_FOR_ID);
        assertTrue(
                exception.getValidationErrors().stream()
                        .anyMatch(error -> error.contains(AnnotationMessages.Name.EMPTY)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }
}