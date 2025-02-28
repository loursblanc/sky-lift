package fr.apsprevoyance.skylift.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
class SportCreateDTOTest {

    private Validator validator;
    private SportCreateDTO validDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        validDTO = new SportCreateDTO();
        validDTO.setName(TestConstants.Sport.VALID_NAME);
        validDTO.setDescription(TestConstants.Sport.VALID_DESCRIPTION);
        validDTO.setActive(TestConstants.Sport.VALID_ACTIVE);
        validDTO.setSeason(TestConstants.Sport.VALID_SEASON);
    }

    @Test
    void create_sport_dto_with_valid_data_has_no_validation_errors() {
        Set<ConstraintViolation<SportCreateDTO>> violations = validator.validate(validDTO);
        assertTrue(violations.isEmpty(), TestConstants.ValidationTestMessages.EMPTY_ERROR_LIST_FOR_VALID);
    }

    @Test
    void create_sport_dto_with_empty_name_has_validation_error() {
        validDTO.setName("");
        Set<ConstraintViolation<SportCreateDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(3, violations.size(), TestConstants.ValidationTestMessages.THREE_ERROR_IN_EXCEPTION);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void create_sport_dto_with_name_too_short_has_validation_error() {
        String shortName = "A";
        validDTO.setName(shortName);

        Set<ConstraintViolation<SportCreateDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void create_sport_dto_with_name_too_long_has_validation_error() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < ValidationConstants.NAME_MAX_LENGTH + 10; i++) {
            longName.append("a");
        }
        validDTO.setName(longName.toString());

        Set<ConstraintViolation<SportCreateDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void create_sport_dto_with_invalid_name_chars_has_validation_error() {
        validDTO.setName(TestConstants.Sport.VALID_NAME + "!@#$");

        Set<ConstraintViolation<SportCreateDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void create_sport_dto_with_null_season_has_validation_error() {
        validDTO.setSeason(null);

        Set<ConstraintViolation<SportCreateDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.SEASON)));
    }

    @Test
    void create_sport_dto_with_null_description_has_no_validation_error() {
        validDTO.setDescription(null);

        Set<ConstraintViolation<SportCreateDTO>> violations = validator.validate(validDTO);

        assertTrue(violations.isEmpty(), TestConstants.ValidationTestMessages.DTO_DESCRIPTION_NULL);
    }

    @Test
    void create_sport_dto_description_is_never_null() {
        validDTO.setDescription(null);

        assertNotNull(validDTO.getDescription(), TestConstants.ValidationTestMessages.DTO_DESCRIPTION_NEVER_NULL);
        assertEquals("", validDTO.getDescription(), TestConstants.ValidationTestMessages.DTO_DESCRIPTION_REPLACE_BLANK);
    }

    @Test
    void create_sport_dto_with_description_too_long_has_validation_error() {
        StringBuilder longDescription = new StringBuilder();
        for (int i = 0; i < ValidationConstants.DESCRIPTION_MAX_LENGTH + 10; i++) {
            longDescription.append("a");
        }
        validDTO.setDescription(longDescription.toString());

        Set<ConstraintViolation<SportCreateDTO>> violations = validator.validate(validDTO);

        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertEquals(1, violations.size(), TestConstants.ValidationTestMessages.ONE_ERROR_IN_EXCEPTION);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(FieldNames.DESCRIPTION)),
                TestConstants.ValidationTestMessages.ERROR_FOR_DESCRIPTION);
    }

    @Test
    void create_sport_dto_default_active_is_true() {
        SportCreateDTO newDto = new SportCreateDTO();
        assertTrue(newDto.isActive(), TestConstants.ValidationTestMessages.DEFAULT_ACTIVE);
    }
}