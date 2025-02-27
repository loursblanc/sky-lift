package fr.apsprevoyance.skylift.model;

import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_AVAILABLE_SPORTS;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_COMMENT;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_COMMISSIONING_DATE;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_ID;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_NAME;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_STATUS;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_TYPE;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.VALID_AVAILABLE_SPORTS;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.VALID_COMMENT;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.VALID_COMMISSIONING_DATE;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.VALID_ID;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.VALID_NAME;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.VALID_STATUS;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.VALID_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.ValidationException;

@Tag("model")
public class SkiLiftTest {

    private SkiLift.Builder builder;

    @BeforeEach
    public void setUp() {
        builder = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(VALID_AVAILABLE_SPORTS)
                .commissioningDate(VALID_COMMISSIONING_DATE);
    }

    @Test
    public void skiLift_with_valid_parameters_builds_successfully() {
        SkiLift skiLift = builder.build();

        assertNotNull(skiLift);
        assertEquals(VALID_ID, skiLift.getId());
        assertEquals(VALID_NAME, skiLift.getName());
        assertEquals(VALID_TYPE, skiLift.getType());
        assertEquals(VALID_STATUS, skiLift.getStatus());
        assertEquals(VALID_COMMENT, skiLift.getComment());
        assertEquals(VALID_AVAILABLE_SPORTS, skiLift.getAvailableSports());
        assertEquals(VALID_COMMISSIONING_DATE, skiLift.getCommissioningDate());
    }

    @Test
    public void skiLift_with_null_id_throws_validation_exception() {
        builder.id(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });

        assertEquals(1, exception.getValidationErrors().size());
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.ID_NULL));
        assertEquals(SkiLift.class.getSimpleName(), exception.getModelName());
        assertEquals(ValidationContextType.MODEL, exception.getContextType());
    }

    @Test
    public void skiLift_with_null_name_throws_validation_exception() {
        builder.name(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });

        assertEquals(1, exception.getValidationErrors().size());
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.NAME_NULL));
    }

    @Test
    public void skiLift_with_null_type_throws_validation_exception() {
        builder.type(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });

        assertEquals(1, exception.getValidationErrors().size());
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.TYPE_NULL));
    }

    @Test
    public void skiLift_with_null_status_throws_validation_exception() {
        builder.status(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });

        assertEquals(1, exception.getValidationErrors().size());
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.STATUS_NULL));
    }

    @Test
    public void skiLift_with_null_available_sports_throws_validation_exception() {
        builder.availableSports(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });

        assertEquals(1, exception.getValidationErrors().size());
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.AVAILABLE_SPORTS_NULL));
    }

    @Test
    public void skiLift_with_null_commissioning_date_throws_validation_exception() {
        builder.commissioningDate(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });

        assertEquals(1, exception.getValidationErrors().size());
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.COMMISSIONING_DATE_NULL));
    }

    @Test
    public void skiLift_with_comment_can_be_null() {
        SkiLift skiLift = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(null).availableSports(VALID_AVAILABLE_SPORTS).commissioningDate(VALID_COMMISSIONING_DATE)
                .build();

        assertNull(skiLift.getComment());
    }

    @Test
    public void skiLift_available_sports_are_unmodifiable() {
        SkiLift skiLift = builder.build();

        assertThrows(UnsupportedOperationException.class, () -> {
            skiLift.getAvailableSports().add("NewSport");
        });
    }

    @Test
    public void equals_and_hashcode_work_correctly() {
        SkiLift skiLift1 = builder.build();
        SkiLift skiLift2 = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(VALID_AVAILABLE_SPORTS)
                .commissioningDate(VALID_COMMISSIONING_DATE).build();
        SkiLift differentSkiLift = SkiLift.builder().id(DIFFERENT_ID).name(DIFFERENT_NAME).type(DIFFERENT_TYPE)
                .status(DIFFERENT_STATUS).comment(DIFFERENT_COMMENT).availableSports(DIFFERENT_AVAILABLE_SPORTS)
                .commissioningDate(DIFFERENT_COMMISSIONING_DATE).build();

        assertEquals(skiLift1, skiLift2);
        assertEquals(skiLift1.hashCode(), skiLift2.hashCode());
        assertNotEquals(skiLift1, differentSkiLift);
        assertNotEquals(skiLift1.hashCode(), differentSkiLift.hashCode());
    }

    @Test
    public void toString_method_works_correctly() {
        SkiLift skiLift = builder.build();
        String toString = skiLift.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("id='" + VALID_ID + "'"));
        assertTrue(toString.contains("name='" + VALID_NAME + "'"));
        assertTrue(toString.contains("type=" + VALID_TYPE));
        assertTrue(toString.contains("status=" + VALID_STATUS));
    }

    @Test
    public void can_add_available_sport_to_builder() {
        Set<String> initialSports = new HashSet<>(VALID_AVAILABLE_SPORTS);
        String newSport = "NewSport";

        SkiLift skiLift = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .availableSports(initialSports).addAvailableSport(newSport).commissioningDate(VALID_COMMISSIONING_DATE)
                .build();

        assertTrue(skiLift.getAvailableSports().containsAll(initialSports));
        assertTrue(skiLift.getAvailableSports().contains(newSport));
    }
}