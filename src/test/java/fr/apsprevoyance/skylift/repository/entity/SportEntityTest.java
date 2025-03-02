package fr.apsprevoyance.skylift.repository.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.groups.Default;

@Tag(TestTag.MODEL)
class SportEntityTest {

    private SportEntity sportEntity;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        sportEntity = new SportEntity(TestConstants.Sport.VALID_ID, TestConstants.Sport.VALID_NAME,
                TestConstants.Sport.VALID_DESCRIPTION, TestConstants.Sport.VALID_SEASON,
                TestConstants.Sport.VALID_ACTIVE);
    }

    @Test
    void entity_with_valid_values_passes_validation() {
        Set<ConstraintViolation<SportEntity>> violations = validator.validate(sportEntity, OnUpdate.class,
                Default.class);
        assertTrue(violations.isEmpty(), TestConstants.ValidationTestMessages.EMPTY_ERROR_LIST_FOR_VALID);
    }

    @Test
    void entity_without_id_passes_onCreate_validation() {
        SportEntity sportWithoutId = new SportEntity(TestConstants.Sport.VALID_NAME,
                TestConstants.Sport.VALID_DESCRIPTION, TestConstants.Sport.VALID_SEASON,
                TestConstants.Sport.VALID_ACTIVE);

        Set<ConstraintViolation<SportEntity>> violations = validator.validate(sportWithoutId, OnCreate.class,
                Default.class);
        assertTrue(violations.isEmpty(), TestConstants.ValidationTestMessages.EMPTY_ERROR_LIST_FOR_VALID);
    }

    @Test
    void entity_without_id_fails_onUpdate_validation() {
        SportEntity sportWithoutId = new SportEntity(TestConstants.Sport.VALID_NAME,
                TestConstants.Sport.VALID_DESCRIPTION, TestConstants.Sport.VALID_SEASON,
                TestConstants.Sport.VALID_ACTIVE);

        Set<ConstraintViolation<SportEntity>> violations = validator.validate(sportWithoutId, OnUpdate.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.ID)),
                TestConstants.ValidationTestMessages.ERROR_FOR_ID);
    }

    @Test
    void negative_id_fails_validation() {
        sportEntity.setId(-1L);

        Set<ConstraintViolation<SportEntity>> violations = validator.validate(sportEntity, Default.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.ID)),
                TestConstants.ValidationTestMessages.ERROR_FOR_ID);
    }

    @Test
    void empty_name_fails_validation() {
        sportEntity.setName("");

        Set<ConstraintViolation<SportEntity>> violations = validator.validate(sportEntity, OnCreate.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void too_short_name_fails_validation() {
        sportEntity.setName("AB");

        Set<ConstraintViolation<SportEntity>> violations = validator.validate(sportEntity, OnCreate.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void too_long_name_fails_validation() {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i <= ValidationConstants.NAME_MAX_LENGTH; i++) {
            name.append("A");
        }

        sportEntity.setName(name.toString());

        Set<ConstraintViolation<SportEntity>> violations = validator.validate(sportEntity, OnCreate.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void null_season_fails_validation() {
        sportEntity.setSeason(null);

        Set<ConstraintViolation<SportEntity>> violations = validator.validate(sportEntity, OnCreate.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.SEASON)),
                "There should be an error for season");
    }

    @Test
    void too_long_description_fails_validation() {
        StringBuilder description = new StringBuilder();
        for (int i = 0; i <= ValidationConstants.DESCRIPTION_MAX_LENGTH; i++) {
            description.append("A");
        }

        sportEntity.setDescription(description.toString());

        Set<ConstraintViolation<SportEntity>> violations = validator.validate(sportEntity, Default.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.DESCRIPTION)),
                TestConstants.ValidationTestMessages.ERROR_FOR_DESCRIPTION);
    }

    @Test
    void null_description_is_replaced_with_empty_string() {
        sportEntity.setDescription(null);
        assertEquals("", sportEntity.getDescription(),
                TestConstants.ValidationTestMessages.DTO_DESCRIPTION_REPLACE_BLANK);
    }

    @Test
    void constructor_handles_null_values() {
        SportEntity entity = new SportEntity(null, null, Season.WINTER, true);

        assertNotNull(entity.getName(), TestConstants.ValidationTestMessages.DTO_DESCRIPTION_NEVER_NULL);
        assertNotNull(entity.getDescription(), TestConstants.ValidationTestMessages.DTO_DESCRIPTION_NEVER_NULL);
        assertEquals("", entity.getName());
        assertEquals("", entity.getDescription());
    }

    @Test
    void getters_and_setters_work_correctly() {
        assertEquals(TestConstants.Sport.VALID_ID, sportEntity.getId());
        assertEquals(TestConstants.Sport.VALID_NAME, sportEntity.getName());
        assertEquals(TestConstants.Sport.VALID_DESCRIPTION, sportEntity.getDescription());
        assertEquals(TestConstants.Sport.VALID_SEASON, sportEntity.getSeason());
        assertEquals(TestConstants.Sport.VALID_ACTIVE, sportEntity.isActive());

        sportEntity.setId(TestConstants.Sport.DIFFERENT_ID);
        sportEntity.setName(TestConstants.Sport.DIFFERENT_NAME);
        sportEntity.setDescription(TestConstants.Sport.DIFFERENT_DESCRIPTION);
        sportEntity.setSeason(Season.SUMMER);
        sportEntity.setActive(false);

        assertEquals(TestConstants.Sport.DIFFERENT_ID, sportEntity.getId());
        assertEquals(TestConstants.Sport.DIFFERENT_NAME, sportEntity.getName());
        assertEquals(TestConstants.Sport.DIFFERENT_DESCRIPTION, sportEntity.getDescription());
        assertEquals(Season.SUMMER, sportEntity.getSeason());
        assertFalse(sportEntity.isActive());
    }

    @Test
    void equals_and_hashCode_work_correctly() {
        SportEntity sameSport = new SportEntity(TestConstants.Sport.VALID_ID, TestConstants.Sport.VALID_NAME,
                TestConstants.Sport.VALID_DESCRIPTION, TestConstants.Sport.VALID_SEASON,
                TestConstants.Sport.VALID_ACTIVE);

        SportEntity differentSport = new SportEntity(TestConstants.Sport.DIFFERENT_ID,
                TestConstants.Sport.DIFFERENT_NAME, TestConstants.Sport.DIFFERENT_DESCRIPTION, Season.SUMMER, false);

        assertEquals(sportEntity, sameSport);
        assertEquals(sportEntity.hashCode(), sameSport.hashCode());

        assertNotEquals(sportEntity, differentSport);
        assertNotEquals(sportEntity.hashCode(), differentSport.hashCode());

        assertNotEquals(sportEntity, null);
        assertNotEquals(sportEntity, new Object());
    }

    @Test
    void default_active_value_is_true() {
        SportEntity defaultSport = new SportEntity(TestConstants.Sport.VALID_NAME,
                TestConstants.Sport.VALID_DESCRIPTION, TestConstants.Sport.VALID_SEASON, true);

        assertTrue(defaultSport.isActive(), TestConstants.ValidationTestMessages.DEFAULT_ACTIVE);
    }
}
