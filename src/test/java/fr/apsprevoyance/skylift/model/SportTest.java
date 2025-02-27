package fr.apsprevoyance.skylift.model;

import static fr.apsprevoyance.skylift.constants.TestConstants.Sport.DIFFERENT_DESCRIPTION;
import static fr.apsprevoyance.skylift.constants.TestConstants.Sport.DIFFERENT_ID;
import static fr.apsprevoyance.skylift.constants.TestConstants.Sport.DIFFERENT_NAME;
import static fr.apsprevoyance.skylift.constants.TestConstants.Sport.VALID_ACTIVE;
import static fr.apsprevoyance.skylift.constants.TestConstants.Sport.VALID_DESCRIPTION;
import static fr.apsprevoyance.skylift.constants.TestConstants.Sport.VALID_ID;
import static fr.apsprevoyance.skylift.constants.TestConstants.Sport.VALID_NAME;
import static fr.apsprevoyance.skylift.constants.TestConstants.Sport.VALID_SEASON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.ValidationException;

@Tag("model")
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

        assertEquals(1, exception.getValidationErrors().size());
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.ID_NULL));
        assertEquals(SkiLift.class.getSimpleName(), exception.getModelName());
        assertEquals(ValidationContextType.MODEL, exception.getContextType());
    }

    @Test
    public void sport_with_null_name_throws_validation_exception() {
        builder.name(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });

        assertEquals(1, exception.getValidationErrors().size());
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.NAME_NULL));
    }

    @Test
    public void sport_with_null_season_throws_validation_exception() {
        builder.season(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            builder.build();
        });

        assertEquals(1, exception.getValidationErrors().size());
        assertTrue(exception.getValidationErrors().contains(ErrorMessageConstants.Errors.SEASON_NULL));
    }

    @Test
    public void sport_description_defaults_to_empty_string_when_null() {
        Sport sport = Sport.builder().id(VALID_ID).name(VALID_NAME).description(null).active(VALID_ACTIVE)
                .season(VALID_SEASON).build();

        assertEquals("", sport.getDescription());
    }

    @Test
    public void sport_builder_default_active_is_true() {
        Sport sport = Sport.builder().id(VALID_ID).name(VALID_NAME).season(VALID_SEASON).build();

        assertTrue(sport.isActive());
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

    @Test
    public void toString_method_works_correctly() {
        Sport sport = builder.build();
        String toString = sport.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("id='" + VALID_ID + "'"));
        assertTrue(toString.contains("name='" + VALID_NAME + "'"));
        assertTrue(toString.contains("season=" + VALID_SEASON));
    }
}