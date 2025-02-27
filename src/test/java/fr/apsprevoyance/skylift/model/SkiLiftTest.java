package fr.apsprevoyance.skylift.model;

import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_AVAILABLE_SPORTS;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_COMMENT;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_COMMISSIONING_DATE;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_ID;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_NAME;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_STATUS;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.DIFFERENT_TYPE;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.INVALID_DATE_TOO_OLD;
import static fr.apsprevoyance.skylift.constants.TestConstants.SkiLift.INVALID_ID_NON_NUMERIC;
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
import fr.apsprevoyance.skylift.constants.ValidationConstants;
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
    public void skilift_with_valid_parameters_builds_successfully() {
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
    public void skilift_with_null_id_throws_validation_exception() {
        builder.id(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.ID_NULL));
    }

    @Test
    public void skilift_with_empty_id_throws_validation_exception() {
        builder.id("");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.ID_EMPTY));
    }

    @Test
    public void skilift_with_non_numeric_id_throws_validation_exception() {
        builder.id(INVALID_ID_NON_NUMERIC);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.ID_NOT_NUMERIC));
    }

    @Test
    public void skilift_with_null_name_throws_validation_exception() {
        builder.name(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.NAME_NULL));
    }

    @Test
    public void skilift_with_empty_name_throws_validation_exception() {
        builder.name("");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.NAME_EMPTY));
    }

    @Test
    public void skilift_with_name_too_short_throws_validation_exception() {
        String shortName = "A";
        builder.name(shortName);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.NAME_TOO_SHORT));
    }

    @Test
    public void skilift_with_name_too_long_throws_validation_exception() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i <= ValidationConstants.NAME_MAX_LENGTH; i++) {
            longName.append("a");
        }
        builder.name(longName.toString());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.NAME_TOO_LONG));
    }

    @Test
    public void skilift_with_null_type_throws_validation_exception() {
        builder.type(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.TYPE_NULL));
    }

    @Test
    public void skilift_with_null_status_throws_validation_exception() {
        builder.status(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.STATUS_NULL));
    }

    @Test
    public void skilift_with_null_available_sports_throws_validation_exception() {
        builder.availableSports(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.AVAILABLE_SPORTS_NULL));
    }

    @Test
    public void skilift_with_empty_available_sports_throws_validation_exception() {
        builder.availableSports(new HashSet<>());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.AVAILABLE_SPORTS_EMPTY));
    }

    @Test
    public void skilift_with_null_commissioning_date_throws_validation_exception() {
        builder.commissioningDate(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.COMMISSIONING_DATE_NULL));
    }

    @Test
    public void skilift_with_commissioning_date_too_old_throws_validation_exception() {
        builder.commissioningDate(INVALID_DATE_TOO_OLD);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.COMMISSIONING_DATE_TOO_OLD));
    }

    @Test
    public void getComment_returns_null_when_comment_is_null() {
        SkiLift skiLift = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(null).availableSports(VALID_AVAILABLE_SPORTS).commissioningDate(VALID_COMMISSIONING_DATE)
                .build();

        assertNull(skiLift.getComment());
    }

    @Test
    public void addAvailableSport_adds_sport_to_set() {
        Set<String> initialSports = new HashSet<>();
        initialSports.add("Ski");

        builder.availableSports(initialSports).addAvailableSport("Surf");

        SkiLift skiLift = builder.build();

        assertEquals(2, skiLift.getAvailableSports().size());
        assertTrue(skiLift.getAvailableSports().contains("Ski"));
        assertTrue(skiLift.getAvailableSports().contains("Surf"));
    }

    @Test
    public void toString_handles_null_values_correctly() {
        SkiLift skiLift = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(null).availableSports(VALID_AVAILABLE_SPORTS).commissioningDate(VALID_COMMISSIONING_DATE)
                .build();

        String result = skiLift.toString();

        assertNotNull(result);
    }

    @Test
    public void equals_handles_null_values_correctly() {
        SkiLift skiLift1 = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(null).availableSports(VALID_AVAILABLE_SPORTS).commissioningDate(VALID_COMMISSIONING_DATE)
                .build();

        SkiLift skiLift2 = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(null).availableSports(VALID_AVAILABLE_SPORTS).commissioningDate(VALID_COMMISSIONING_DATE)
                .build();

        assertEquals(skiLift1, skiLift2);
        assertEquals(skiLift1, skiLift1);
        assertNotEquals(skiLift1, null);
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
}