package fr.apsprevoyance.skylift.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.constants.TestConstants.FieldNames;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Tag("dto")
class SkiLiftDTOTest {

    private Validator validator;
    private SkiLiftDTO validDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        validDTO = new SkiLiftDTO();
        validDTO.setId(TestConstants.SkiLift.VALID_ID);
        validDTO.setName(TestConstants.SkiLift.VALID_NAME);
        validDTO.setType(TestConstants.SkiLift.VALID_TYPE);
        validDTO.setStatus(TestConstants.SkiLift.VALID_STATUS);
        validDTO.setComment(TestConstants.SkiLift.VALID_COMMENT);
        validDTO.setAvailableSports(TestConstants.SkiLift.VALID_AVAILABLE_SPORTS);
        validDTO.setCommissioningDate(TestConstants.SkiLift.VALID_COMMISSIONING_DATE);
    }

    @Test
    void skilift_dto_with_valid_data_has_no_validation_errors() {
        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);
        assertTrue(violations.isEmpty(), TestConstants.ValidationTestMessages.EMPTY_ERROR_LIST_FOR_VALID);
    }

    @Test
    void skilift_dto_with_empty_id_has_validation_error() {
        validDTO.setId("");
        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.ID)),
                TestConstants.ValidationTestMessages.ERROR_FOR_ID);
    }

    @Test
    void skilift_dto_with_empty_name_has_validation_error() {
        validDTO.setName("");
        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(3, violations.size(), TestConstants.ValidationTestMessages.THREE_ERROR_IN_EXCEPTION);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void skilift_dto_with_name_too_short_has_validation_error() {
        String shortName = "A";
        validDTO.setName(shortName);

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void skilift_dto_with_name_too_long_has_validation_error() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < ValidationConstants.NAME_MAX_LENGTH + 10; i++) {
            longName.append("a");
        }
        validDTO.setName(longName.toString());

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void skilift_dto_with_invalid_name_chars_has_validation_error() {
        validDTO.setName(TestConstants.SkiLift.VALID_NAME + "!@#$");

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void skilift_dto_with_null_type_has_validation_error() {
        validDTO.setType(null);

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.TYPE)),
                TestConstants.ValidationTestMessages.ERROR_FOR_TYPE);
    }

    @Test
    void skilift_dto_with_null_status_has_validation_error() {
        validDTO.setStatus(null);

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.STATUS)),
                TestConstants.ValidationTestMessages.ERROR_FOR_STATUS);
    }

    @Test
    void skilift_dto_with_null_comment_has_no_validation_error() {
        validDTO.setComment(null);

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);

        assertTrue(violations.isEmpty(), TestConstants.ValidationTestMessages.DTO_DESCRIPTION_NULL);
    }

    @Test
    void skilift_dto_comment_is_never_null() {
        validDTO.setComment(null);

        assertNotNull(validDTO.getComment(), TestConstants.ValidationTestMessages.DTO_DESCRIPTION_NEVER_NULL);
        assertEquals("", validDTO.getComment(), TestConstants.ValidationTestMessages.COMMENT_REPLACE_BLANK);
    }

    @Test
    void skilift_dto_with_empty_available_sports_has_validation_error() {
        validDTO.setAvailableSports(new HashSet<>());

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.AVAILABLE_SPORTS)),
                TestConstants.ValidationTestMessages.ERROR_FOR_SPORTS);
    }

    @Test
    void skilift_dto_with_null_available_sports_replaced_by_empty_set() {
        validDTO.setAvailableSports(null);

        assertNotNull(validDTO.getAvailableSports(), TestConstants.ValidationTestMessages.SPORTS_NEVER_NULL);
        assertTrue(validDTO.getAvailableSports().isEmpty());

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.AVAILABLE_SPORTS)),
                TestConstants.ValidationTestMessages.ERROR_FOR_SPORTS);
    }

    @Test
    void skilift_dto_with_null_commissioning_date_has_validation_error() {
        validDTO.setCommissioningDate(null);

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.COMMISSIONING_DATE)),
                TestConstants.ValidationTestMessages.ERROR_FOR_DATE);
    }

    @Test
    void skilift_dto_with_too_old_commissioning_date_has_validation_error() {
        validDTO.setCommissioningDate(TestConstants.SkiLift.INVALID_DATE_TOO_OLD);

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.COMMISSIONING_DATE)),
                TestConstants.ValidationTestMessages.DATE_TOO_OLD_ERROR);
    }
}