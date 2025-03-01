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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.validation.ModelValidationService;

@Tag("model")
public class SkiLiftTest {

    private SkiLift.Builder builder;
    private ModelValidationService validationService;

    @BeforeEach
    public void setUp() {
        builder = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(VALID_AVAILABLE_SPORTS)
                .commissioningDate(VALID_COMMISSIONING_DATE);

        validationService = new ModelValidationService();
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
    public void validation_detects_negative_id() {
        SkiLift skiLift = SkiLift.builder().id(-1L).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS).build();

        List<String> violations = validationService.checkWithAnnotations(skiLift);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.contains("id") && v.contains("positive")));
    }

    @Test
    public void availableSports_is_empty_set_when_null_is_provided() {
        SkiLift skiLift = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .availableSports(null).build();

        assertNotNull(skiLift.getAvailableSports());
        assertTrue(skiLift.getAvailableSports().isEmpty());
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

    @Test
    public void default_commissioning_date_is_today() {
        SkiLift skiLift = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS).build();

        assertEquals(LocalDate.now(), skiLift.getCommissioningDate());
    }

    @Test
    public void comment_can_be_null() {
        SkiLift skiLift = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(null).build();

        assertNull(skiLift.getComment());
    }
}