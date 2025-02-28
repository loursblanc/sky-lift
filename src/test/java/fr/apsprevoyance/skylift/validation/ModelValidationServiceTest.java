package fr.apsprevoyance.skylift.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.AnnotationMessages;
import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Tag("validation")
class ModelValidationServiceTest {

    private ModelValidationService validationService;

    private static final Long VALID_ID = 111L;
    private static final Long INVALID_ID = -1L;

    @BeforeEach
    void setUp() {
        validationService = new ModelValidationService();
    }

    @Test
    void checkWithAnnotations_shouldReturnEmptyList_whenObjectIsValid() {
        TestModel validModel = new TestModel(123L, TestConstants.SKILIFT_VALID_NAME, LocalDate.now());

        List<String> errors = validationService.checkWithAnnotations(validModel);

        assertTrue(errors.isEmpty(), TestConstants.ValidationTestMessages.EMPTY_ERROR_LIST_FOR_VALID);
    }

    @Test
    void checkWithAnnotations_shouldReturnErrors_whenObjectHasNullValues() {
        TestModel invalidModel = new TestModel(null, null, null);

        List<String> errors = validationService.checkWithAnnotations(invalidModel);

        assertFalse(errors.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(3, errors.size(), TestConstants.ValidationTestMessages.THREE_ERROR_IN_EXCEPTION);
        assertTrue(errors.stream().anyMatch(error -> error.contains(TestConstants.FieldNames.ID)),
                TestConstants.ValidationTestMessages.ERROR_FOR_ID);
        assertTrue(errors.stream().anyMatch(error -> error.contains(TestConstants.FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
        assertTrue(errors.stream().anyMatch(error -> error.contains(TestConstants.FieldNames.DATE)),
                TestConstants.ValidationTestMessages.ERROR_FOR_DATE);
    }

    @Test
    void checkWithAnnotations_shouldReturnErrors_whenObjectHasInvalidValues() {
        TestModel invalidModel = new TestModel(INVALID_ID, "", LocalDate.now());

        List<String> errors = validationService.checkWithAnnotations(invalidModel);

        assertFalse(errors.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(3, errors.size(), TestConstants.ValidationTestMessages.THREE_ERROR_IN_EXCEPTION);
        assertTrue(errors.stream().anyMatch(error -> error.contains(TestConstants.FieldNames.ID)),
                TestConstants.ValidationTestMessages.ERROR_FOR_ID);
        assertTrue(errors.stream().anyMatch(error -> error.contains(TestConstants.FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void checkAndThrowIfInvalid_shouldNotThrowException_whenObjectIsValid() {
        TestModel validModel = new TestModel(123L, TestConstants.SKILIFT_VALID_NAME, LocalDate.now());

        assertDoesNotThrow(() -> validationService.checkAndThrowIfInvalid(validModel, "TestModel"),
                TestConstants.ValidationTestMessages.NO_EXCEPTION_FOR_VALID);
    }

    @Test
    void checkAndThrowIfInvalid_shouldThrowException_whenObjectIsInvalid() {
        TestModel invalidModel = new TestModel(INVALID_ID, "", null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validationService.checkAndThrowIfInvalid(invalidModel, "TestModel"),
                TestConstants.ValidationTestMessages.EXCEPTION_FOR_INVALID);

        assertEquals("TestModel", exception.getModelName(), TestConstants.ValidationTestMessages.ENTITY_NAME_CORRECT);
        assertEquals(ValidationContextType.MODEL, exception.getContextType(),
                TestConstants.ValidationTestMessages.CONTEXT_TYPE_MODEL);
        assertEquals(4, exception.getValidationErrors().size(),
                TestConstants.ValidationTestMessages.FOUR_ERRORS_IN_EXCEPTION);
    }

    @Test
    void checkAndThrowIfInvalid_withCustomValidator_shouldNotThrowException_whenAllValidationsPass() {
        TestModel validModel = new TestModel(VALID_ID, TestConstants.SKILIFT_VALID_NAME, LocalDate.now());

        CustomValidator<TestModel> customValidator = (model, errors) -> {
        };

        assertDoesNotThrow(() -> validationService.checkAndThrowIfInvalid(validModel, "TestModel", customValidator),
                TestConstants.ValidationTestMessages.NO_EXCEPTION_ALL_VALIDATIONS_PASS);
    }

    @Test
    void checkAndThrowIfInvalid_withCustomValidator_shouldThrowException_whenCustomValidationFails() {
        TestModel validModel = new TestModel(VALID_ID, "Valid name", TestConstants.SkiLift.INVALID_DATE_TOO_OLD);

        CustomValidator<TestModel> customValidator = (model, errors) -> {
            if (model.getDate().isBefore(ValidationConstants.FIRST_SKILIFT_DATE)) {
                errors.add(ErrorMessageConstants.Errors.COMMISSIONING_DATE_TOO_OLD);
            }
        };

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validationService.checkAndThrowIfInvalid(validModel, "TestModel", customValidator),
                TestConstants.ValidationTestMessages.EXCEPTION_CUSTOM_VALIDATION_FAILS);

        assertEquals(1, exception.getValidationErrors().size(),
                TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertEquals(ErrorMessageConstants.Errors.COMMISSIONING_DATE_TOO_OLD, exception.getValidationErrors().get(0),
                TestConstants.ValidationTestMessages.CUSTOM_ERROR_IN_EXCEPTION);
    }

    @Test
    void checkAndThrowIfInvalid_withCustomValidator_shouldIncludeAllErrors_whenBothValidationsFail() {
        TestModel invalidModel = new TestModel(INVALID_ID, "", TestConstants.SkiLift.INVALID_DATE_TOO_OLD);

        CustomValidator<TestModel> customValidator = (model, errors) -> {
            if (model.getDate().isBefore(ValidationConstants.FIRST_SKILIFT_DATE)) {
                errors.add(ErrorMessageConstants.Errors.COMMISSIONING_DATE_TOO_OLD);
            }
        };

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validationService.checkAndThrowIfInvalid(invalidModel, "TestModel", customValidator),
                TestConstants.ValidationTestMessages.EXCEPTION_BOTH_VALIDATIONS_FAIL);

        assertEquals(4, exception.getValidationErrors().size(),
                TestConstants.ValidationTestMessages.FOUR_ERRORS_IN_EXCEPTION);
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.COMMISSIONING_DATE_TOO_OLD),
                TestConstants.ValidationTestMessages.DATE_TOO_OLD_ERROR_PRESENT);
    }

    @Test
    void checkWithAnnotations_shouldReturnErrors_whenNameIsTooLong() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < ValidationConstants.NAME_MAX_LENGTH + 10; i++) {
            longName.append("a");
        }

        TestModel invalidModel = new TestModel(VALID_ID, longName.toString(), LocalDate.now());

        List<String> errors = validationService.checkWithAnnotations(invalidModel);

        assertFalse(errors.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, errors.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(errors.stream().anyMatch(error -> error.contains(TestConstants.FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    private static class TestModel {
        @NotNull(message = AnnotationMessages.Id.NULL)
        @Positive(message = AnnotationMessages.Id.POSITIVE)
        private final Long id;

        @NotBlank(message = TestConstants.ValidationTestMessages.NAME_CANNOT_BE_EMPTY)
        @Size(min = ValidationConstants.NAME_MIN_LENGTH, max = ValidationConstants.NAME_MAX_LENGTH, message = AnnotationMessages.Name.TEXT_LENGHT)
        private final String name;

        @NotNull(message = TestConstants.ValidationTestMessages.DATE_CANNOT_BE_NULL)
        private final LocalDate date;

        public TestModel(Long id, String name, LocalDate date) {
            this.id = id;
            this.name = name;
            this.date = date;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public LocalDate getDate() {
            return date;
        }
    }
}