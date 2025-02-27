package fr.apsprevoyance.skylift.model;

import static fr.apsprevoyance.skylift.constants.TestConstants.Sport.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import fr.apsprevoyance.skylift.exception.ValidationException;

public class SportTest {

    private Sport.Builder builder;

    @BeforeEach
    public void setUp() {
        builder = Sport.builder().id(VALID_ID).name(VALID_NAME).description(VALID_DESCRIPTION).active(VALID_ACTIVE)
                .season(VALID_SEASON);
    }

    @Test
    public void sport_with_valid_parameters_builds_successfully() {
        Sport sport = builder.build();

        assertNotNull(sport);
        assertEquals(VALID_ID, sport.getId());
        assertEquals(VALID_NAME, sport.getName());
        assertEquals(VALID_DESCRIPTION, sport.getDescription());
        assertEquals(VALID_ACTIVE, sport.isActive());
        assertEquals(VALID_SEASON, sport.getSeason());
    }

    @Test
    public void sport_with_null_id_throws_validation_exception() {
        builder.id(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.ID_NULL));
    }

    @Test
    public void sport_with_empty_id_throws_validation_exception() {
        builder.id("");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.ID_EMPTY));
    }

    @Test
    public void sport_with_non_numeric_id_throws_validation_exception() {
        builder.id(INVALID_ID_NON_NUMERIC);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.ID_NOT_NUMERIC));
    }

    @Test
    public void sport_with_null_name_throws_validation_exception() {
        builder.name(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.NAME_NULL));
    }

    @Test
    public void sport_with_empty_name_throws_validation_exception() {
        builder.name("");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.NAME_EMPTY));
    }

    @Test
    public void sport_with_name_too_short_throws_validation_exception() {
        String shortName = "A";
        builder.name(shortName);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.NAME_TOO_SHORT));
    }

    @Test
    public void sport_with_name_too_long_throws_validation_exception() {
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
    public void sport_with_null_season_throws_validation_exception() {
        builder.season(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.SEASON_NULL));
    }

    @Test
    public void getDescription_returns_null_when_description_is_null() {
        Sport sport = Sport.builder().id(VALID_ID).name(VALID_NAME).description(null).active(VALID_ACTIVE)
                .season(VALID_SEASON).build();

        assertNull(sport.getDescription());
    }

    @Test
    public void toString_handles_null_values_correctly() {
        Sport sport = Sport.builder().id(VALID_ID).name(VALID_NAME).description(null).active(VALID_ACTIVE)
                .season(VALID_SEASON).build();

        String result = sport.toString();

        assertNotNull(result);
    }

    @Test
    public void equals_handles_null_values_correctly() {
        Sport sport1 = Sport.builder().id(VALID_ID).name(VALID_NAME).description(null).active(VALID_ACTIVE)
                .season(VALID_SEASON).build();

        Sport sport2 = Sport.builder().id(VALID_ID).name(VALID_NAME).description(null).active(VALID_ACTIVE)
                .season(VALID_SEASON).build();

        assertEquals(sport1, sport2);
        assertEquals(sport1, sport1);
        assertNotEquals(sport1, null);
    }

    @Test
    public void equals_and_hashcode_work_correctly() {
        Sport sport1 = builder.build();
        Sport sport2 = Sport.builder().id(VALID_ID).name(VALID_NAME).description(VALID_DESCRIPTION).active(VALID_ACTIVE)
                .season(VALID_SEASON).build();
        Sport differentSport = Sport.builder().id(DIFFERENT_ID).name(DIFFERENT_NAME).description(DIFFERENT_DESCRIPTION)
                .active(VALID_ACTIVE).season(VALID_SEASON).build();

        assertEquals(sport1, sport2);
        assertEquals(sport1.hashCode(), sport2.hashCode());
        assertNotEquals(sport1, differentSport);
        assertNotEquals(sport1.hashCode(), differentSport.hashCode());
    }
}