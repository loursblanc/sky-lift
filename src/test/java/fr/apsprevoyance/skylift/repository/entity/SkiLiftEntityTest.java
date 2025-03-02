package fr.apsprevoyance.skylift.repository.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.groups.Default;

@Tag(TestTag.MODEL)
class SkiLiftEntityTest {

    private SkiLiftEntity skiLiftEntity;
    private Validator validator;

    // Création d'un mock de SportEntity pour les tests
    private static class MockSportEntity extends SportEntity {
        private final Long id;
        private final String name;

        public MockSportEntity(Long id, String name) {
            super(name, "Description mock", Season.WINTER, true);
            this.id = id;
            this.name = name;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof MockSportEntity))
                return false;
            MockSportEntity other = (MockSportEntity) obj;
            return id.equals(other.id) && name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return id.hashCode() + name.hashCode();
        }

        @Override
        public String toString() {
            return "MockSport{id=" + id + ", name='" + name + "'}";
        }
    }

    // Création d'un Set non vide de sports mockés
    public static final Set<SportEntity> VALID_SPORTS = Set.of(new MockSportEntity(1L, "Ski"),
            new MockSportEntity(2L, "Snowboard"));

    public static final Set<SportEntity> DIFFERENT_SPORTS = Set.of(new MockSportEntity(3L, "Luge"),
            new MockSportEntity(4L, "Snowscoot"));

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        skiLiftEntity = new SkiLiftEntity(TestConstants.SkiLift.VALID_ID, TestConstants.SkiLift.VALID_NAME,
                TestConstants.SkiLift.VALID_TYPE, TestConstants.SkiLift.VALID_STATUS,
                TestConstants.SkiLift.VALID_COMMENT, VALID_SPORTS, TestConstants.SkiLift.VALID_COMMISSIONING_DATE);
    }

    @Test
    void entity_with_valid_values_passes_validation() {
        Set<ConstraintViolation<SkiLiftEntity>> violations = validator.validate(skiLiftEntity, OnUpdate.class,
                Default.class);
        assertTrue(violations.isEmpty(), TestConstants.ValidationTestMessages.EMPTY_ERROR_LIST_FOR_VALID);
    }

    @Test
    void entity_without_id_passes_onCreate_validation() {
        SkiLiftEntity skiLiftWithoutId = new SkiLiftEntity(TestConstants.SkiLift.VALID_NAME,
                TestConstants.SkiLift.VALID_TYPE, TestConstants.SkiLift.VALID_STATUS,
                TestConstants.SkiLift.VALID_COMMENT, VALID_SPORTS, TestConstants.SkiLift.VALID_COMMISSIONING_DATE);

        Set<ConstraintViolation<SkiLiftEntity>> violations = validator.validate(skiLiftWithoutId, OnCreate.class,
                Default.class);
        assertTrue(violations.isEmpty(), TestConstants.ValidationTestMessages.EMPTY_ERROR_LIST_FOR_VALID);
    }

    @Test
    void entity_without_id_fails_onUpdate_validation() {
        SkiLiftEntity skiLiftWithoutId = new SkiLiftEntity(TestConstants.SkiLift.VALID_NAME,
                TestConstants.SkiLift.VALID_TYPE, TestConstants.SkiLift.VALID_STATUS,
                TestConstants.SkiLift.VALID_COMMENT, VALID_SPORTS, TestConstants.SkiLift.VALID_COMMISSIONING_DATE);

        Set<ConstraintViolation<SkiLiftEntity>> violations = validator.validate(skiLiftWithoutId, OnUpdate.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.ID)),
                TestConstants.ValidationTestMessages.ERROR_FOR_ID);
    }

    @Test
    void negative_id_fails_validation() {
        skiLiftEntity.setId(-1L);
        Set<ConstraintViolation<SkiLiftEntity>> violations = validator.validate(skiLiftEntity, Default.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.ID)),
                TestConstants.ValidationTestMessages.ERROR_FOR_ID);
    }

    @Test
    void empty_name_fails_validation() {
        skiLiftEntity.setName("");
        Set<ConstraintViolation<SkiLiftEntity>> violations = validator.validate(skiLiftEntity, OnCreate.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.NAME)),
                TestConstants.ValidationTestMessages.ERROR_FOR_NAME);
    }

    @Test
    void null_type_fails_validation() {
        skiLiftEntity.setType(null);
        Set<ConstraintViolation<SkiLiftEntity>> violations = validator.validate(skiLiftEntity, OnCreate.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.TYPE)),
                TestConstants.ValidationTestMessages.ERROR_FOR_TYPE);
    }

    @Test
    void null_status_fails_validation() {
        skiLiftEntity.setStatus(null);
        Set<ConstraintViolation<SkiLiftEntity>> violations = validator.validate(skiLiftEntity, OnCreate.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.STATUS)),
                TestConstants.ValidationTestMessages.ERROR_FOR_STATUS);
    }

    @Test
    void empty_sports_fails_validation() {
        skiLiftEntity.setAvailableSports(new HashSet<>());
        Set<ConstraintViolation<SkiLiftEntity>> violations = validator.validate(skiLiftEntity, OnCreate.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream().anyMatch(
                        v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.AVAILABLE_SPORTS)),
                TestConstants.ValidationTestMessages.ERROR_FOR_SPORTS);
    }

    @Test
    void null_sports_is_replaced_with_empty_set() {
        skiLiftEntity.setAvailableSports(null);
        assertNotNull(skiLiftEntity.getAvailableSports(), TestConstants.ValidationTestMessages.SPORTS_NEVER_NULL);
        assertTrue(skiLiftEntity.getAvailableSports().isEmpty());
    }

    @Test
    void null_commissioning_date_fails_validation() {
        skiLiftEntity.setCommissioningDate(null);
        Set<ConstraintViolation<SkiLiftEntity>> violations = validator.validate(skiLiftEntity, OnCreate.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream().anyMatch(
                        v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.COMMISSIONING_DATE)),
                TestConstants.ValidationTestMessages.ERROR_FOR_DATE);
    }

    @Test
    void too_old_commissioning_date_fails_validation() {
        skiLiftEntity.setCommissioningDate(ValidationConstants.FIRST_SKILIFT_DATE.minusDays(1));
        Set<ConstraintViolation<SkiLiftEntity>> violations = validator.validate(skiLiftEntity, OnCreate.class);
        assertFalse(violations.isEmpty(), TestConstants.ValidationTestMessages.ERROR_LIST_NOT_EMPTY);
        assertTrue(
                violations.stream().anyMatch(
                        v -> v.getPropertyPath().toString().equals(TestConstants.FieldNames.COMMISSIONING_DATE)),
                TestConstants.ValidationTestMessages.DATE_TOO_OLD_ERROR);
    }

    @Test
    void null_comment_is_replaced_with_empty_string() {
        skiLiftEntity.setComment(null);
        assertEquals("", skiLiftEntity.getComment(), TestConstants.ValidationTestMessages.COMMENT_REPLACE_BLANK);
    }

    @Test
    void getters_and_setters_work_correctly() {
        assertEquals(TestConstants.SkiLift.VALID_ID, skiLiftEntity.getId());
        assertEquals(TestConstants.SkiLift.VALID_NAME, skiLiftEntity.getName());
        assertEquals(TestConstants.SkiLift.VALID_TYPE, skiLiftEntity.getType());
        assertEquals(TestConstants.SkiLift.VALID_STATUS, skiLiftEntity.getStatus());
        assertEquals(TestConstants.SkiLift.VALID_COMMENT, skiLiftEntity.getComment());
        assertEquals(VALID_SPORTS, skiLiftEntity.getAvailableSports());
        assertEquals(TestConstants.SkiLift.VALID_COMMISSIONING_DATE, skiLiftEntity.getCommissioningDate());

        skiLiftEntity.setId(TestConstants.SkiLift.DIFFERENT_ID);
        skiLiftEntity.setName(TestConstants.SkiLift.DIFFERENT_NAME);
        skiLiftEntity.setType(SkiLiftType.TELESKI);
        skiLiftEntity.setStatus(SkiLiftStatus.CLOSED);
        skiLiftEntity.setComment(TestConstants.SkiLift.DIFFERENT_COMMENT);
        skiLiftEntity.setAvailableSports(DIFFERENT_SPORTS);
        skiLiftEntity.setCommissioningDate(TestConstants.SkiLift.DIFFERENT_COMMISSIONING_DATE);

        assertEquals(TestConstants.SkiLift.DIFFERENT_ID, skiLiftEntity.getId());
        assertEquals(TestConstants.SkiLift.DIFFERENT_NAME, skiLiftEntity.getName());
        assertEquals(SkiLiftType.TELESKI, skiLiftEntity.getType());
        assertEquals(SkiLiftStatus.CLOSED, skiLiftEntity.getStatus());
        assertEquals(TestConstants.SkiLift.DIFFERENT_COMMENT, skiLiftEntity.getComment());
        assertEquals(DIFFERENT_SPORTS, skiLiftEntity.getAvailableSports());
        assertEquals(TestConstants.SkiLift.DIFFERENT_COMMISSIONING_DATE, skiLiftEntity.getCommissioningDate());
    }

    @Test
    void equals_and_hashCode_work_correctly() {
        SkiLiftEntity sameSkiLift = new SkiLiftEntity(TestConstants.SkiLift.VALID_ID, TestConstants.SkiLift.VALID_NAME,
                TestConstants.SkiLift.VALID_TYPE, TestConstants.SkiLift.VALID_STATUS,
                TestConstants.SkiLift.VALID_COMMENT, VALID_SPORTS, TestConstants.SkiLift.VALID_COMMISSIONING_DATE);

        SkiLiftEntity differentSkiLift = new SkiLiftEntity(TestConstants.SkiLift.DIFFERENT_ID,
                TestConstants.SkiLift.DIFFERENT_NAME, SkiLiftType.TELESKI, SkiLiftStatus.CLOSED,
                TestConstants.SkiLift.DIFFERENT_COMMENT, DIFFERENT_SPORTS,
                TestConstants.SkiLift.DIFFERENT_COMMISSIONING_DATE);

        assertEquals(skiLiftEntity, sameSkiLift);
        assertEquals(skiLiftEntity.hashCode(), sameSkiLift.hashCode());

        assertNotEquals(skiLiftEntity, differentSkiLift);
        assertNotEquals(skiLiftEntity.hashCode(), differentSkiLift.hashCode());

        assertNotEquals(skiLiftEntity, null);
        assertNotEquals(skiLiftEntity, new Object());
    }
}