package fr.apsprevoyance.skylift.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.SportLabels;
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

@Tag("model")
public class SkiLiftTest {

    private SkiLift skiLift;
    private Validator validator;

    private static final Long VALID_ID = 1L;
    private static final String VALID_NAME = "Télésiège du Lac";
    private static final SkiLiftType VALID_TYPE = SkiLiftType.TELESIEGE;
    private static final SkiLiftStatus VALID_STATUS = SkiLiftStatus.OPEN;
    private static final String VALID_COMMENT = "Un commentaire valide";
    private static final LocalDate VALID_DATE = LocalDate.now();

    // Créer un Sport valide pour les tests
    private static final Sport VALID_SPORT = Sport.builder().name(SportLabels.SKI).season(Season.WINTER).active(true)
            .build();
    private static final Set<Sport> VALID_SPORTS = new HashSet<>(Set.of(VALID_SPORT));

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        skiLift = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(VALID_DATE).build();
    }

    // Méthode utilitaire pour créer un Sport
    private Sport createSport(String name) {
        return Sport.builder().name(name).season(Season.WINTER).active(true).build();
    }

    // Méthodes de test originales avec modifications mineures

    @Test
    public void model_with_valid_values_passes_validation() {
        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLift, OnUpdate.class, Default.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void model_without_id_passes_onCreate_validation() {
        SkiLift skiLiftWithoutId = SkiLift.builder().name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(VALID_DATE).build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithoutId, OnCreate.class,
                Default.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void model_without_id_fails_onUpdate_validation() {
        SkiLift skiLiftWithoutId = SkiLift.builder().name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(VALID_DATE).build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithoutId, OnUpdate.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("id")));
    }

    // ... (Toutes les autres méthodes de test originales restent similaires)

    @Test
    public void empty_availableSports_fails_validation() {
        SkiLift skiLiftWithEmptySports = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE)
                .status(VALID_STATUS).comment(VALID_COMMENT).availableSports(new HashSet<>())
                .commissioningDate(VALID_DATE).build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithEmptySports, OnCreate.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(
                v -> v.getPropertyPath().toString().equals("availableSports") && v.getMessage().contains("empty")));
    }

    @Test
    public void builder_handles_null_availableSports() {
        SkiLift skiLiftWithNullSportsHandled = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE)
                .status(VALID_STATUS).comment(VALID_COMMENT).availableSports(null).commissioningDate(VALID_DATE)
                .build();

        assertNotNull(skiLiftWithNullSportsHandled.getAvailableSports());
        assertTrue(skiLiftWithNullSportsHandled.getAvailableSports().isEmpty());
    }

    @Test
    public void builder_addAvailableSport_works() {
        Sport skiSport = createSport(SportLabels.SKI);
        Sport snowboardSport = createSport(SportLabels.SNOWBOARD);

        SkiLift skiLift = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(new HashSet<>()).addAvailableSport(skiSport)
                .addAvailableSport(snowboardSport).commissioningDate(VALID_DATE).build();

        assertEquals(2, skiLift.getAvailableSports().size());
        assertTrue(skiLift.getAvailableSports().stream().anyMatch(s -> s.getName().equals(SportLabels.SKI)));
        assertTrue(skiLift.getAvailableSports().stream().anyMatch(s -> s.getName().equals(SportLabels.SNOWBOARD)));
    }

    // Les autres méthodes de test (equals, hashCode, etc.) restent similaires,
    // avec des ajustements mineurs pour utiliser des objets Sport

    @Test
    public void equals_should_return_true_for_objects_with_same_values() {
        Set<Sport> identicalSports = new HashSet<>(
                Set.of(Sport.builder().name(SportLabels.SKI).season(Season.WINTER).active(true).build()));

        SkiLift identicalSkiLift = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(identicalSports).commissioningDate(VALID_DATE).build();

        assertEquals(skiLift, identicalSkiLift);
    }

    @Test
    public void negative_id_fails_validation() {
        SkiLift skiLiftWithNegativeId = SkiLift.builder().id(-1L).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(VALID_DATE).build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithNegativeId, Default.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("id") && v.getMessage().contains("positive")));
    }

    @Test
    public void empty_name_fails_validation() {
        SkiLift skiLiftWithEmptyName = SkiLift.builder().id(VALID_ID).name("").type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(VALID_DATE).build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithEmptyName, OnCreate.class,
                Default.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void too_short_name_fails_validation() {
        SkiLift skiLiftWithShortName = SkiLift.builder().id(VALID_ID).name("AB").type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(VALID_DATE).build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithShortName, OnCreate.class,
                Default.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void too_long_name_fails_validation() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i <= ValidationConstants.NAME_MAX_LENGTH; i++) {
            longName.append("A");
        }

        SkiLift skiLiftWithLongName = SkiLift.builder().id(VALID_ID).name(longName.toString()).type(VALID_TYPE)
                .status(VALID_STATUS).comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(VALID_DATE)
                .build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithLongName, OnCreate.class,
                Default.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void invalid_name_character_fails_validation() {
        SkiLift skiLiftWithInvalidName = SkiLift.builder().id(VALID_ID).name("Invalid-Name!").type(VALID_TYPE)
                .status(VALID_STATUS).comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(VALID_DATE)
                .build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithInvalidName, OnCreate.class,
                Default.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void null_type_fails_validation() {
        SkiLift skiLiftWithNullType = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(null).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(VALID_DATE).build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithNullType, OnCreate.class,
                Default.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("type")));
    }

    @Test
    public void null_status_fails_validation() {
        SkiLift skiLiftWithNullStatus = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(null)
                .comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(VALID_DATE).build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithNullStatus, OnCreate.class,
                Default.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
    }

    @Test
    public void empty_availableSports_not_fails_validation() {
        SkiLift skiLiftWithEmptySports = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE)
                .status(VALID_STATUS).comment(VALID_COMMENT).availableSports(new HashSet<>())
                .commissioningDate(VALID_DATE).build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithEmptySports);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void null_commissioning_date_fails_validation() {
        SkiLift skiLiftWithNullDate = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE)
                .status(VALID_STATUS).comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(null)
                .build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithNullDate, OnCreate.class,
                Default.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("commissioningDate")));
    }

    @Test
    public void too_old_commissioning_date_fails_validation() {
        SkiLift skiLiftWithOldDate = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE)
                .status(VALID_STATUS).comment(VALID_COMMENT).availableSports(VALID_SPORTS)
                .commissioningDate(ValidationConstants.FIRST_SKILIFT_DATE.minusDays(1)).build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithOldDate, Default.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("commissioningDate")));
    }

    @Test
    public void too_long_comment_fails_validation() {
        StringBuilder longComment = new StringBuilder();
        for (int i = 0; i <= ValidationConstants.DESCRIPTION_MAX_LENGTH; i++) {
            longComment.append("A");
        }

        SkiLift skiLiftWithLongComment = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE)
                .status(VALID_STATUS).comment(longComment.toString()).availableSports(VALID_SPORTS)
                .commissioningDate(VALID_DATE).build();

        Set<ConstraintViolation<SkiLift>> violations = validator.validate(skiLiftWithLongComment, Default.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("comment")));
    }

    @Test
    public void getters_work_correctly() {
        assertEquals(VALID_ID, skiLift.getId());
        assertEquals(VALID_NAME, skiLift.getName());
        assertEquals(VALID_TYPE, skiLift.getType());
        assertEquals(VALID_STATUS, skiLift.getStatus());
        assertEquals(VALID_COMMENT, skiLift.getComment());
        assertEquals(VALID_SPORTS, skiLift.getAvailableSports());
        assertEquals(VALID_DATE, skiLift.getCommissioningDate());
    }

    @Test
    public void equals_should_return_true_for_same_object() {
        SkiLift originalSkiLift = skiLift;
        assertEquals(skiLift, originalSkiLift);
    }

    @Test
    public void equals_should_return_false_for_different_objects() {
        SkiLift differentSkiLift = SkiLift.builder().id(2L).name("Different Lift").type(SkiLiftType.TELESKI)
                .status(SkiLiftStatus.CLOSED).comment("Different Comment").availableSports(new HashSet<>(Set.of()))
                .commissioningDate(LocalDate.now().minusYears(1)).build();

        assertNotEquals(skiLift, differentSkiLift);
    }

    @Test
    public void equals_should_return_false_when_comparing_to_null() {
        assertNotEquals(skiLift, null);
    }

    @Test
    public void equals_should_return_false_when_comparing_to_different_type() {
        assertNotEquals(skiLift, new Object());
    }

    @Test
    public void hashCode_should_be_consistent() {
        SkiLift identicalSkiLift = SkiLift.builder().id(VALID_ID).name(VALID_NAME).type(VALID_TYPE).status(VALID_STATUS)
                .comment(VALID_COMMENT).availableSports(VALID_SPORTS).commissioningDate(VALID_DATE).build();

        assertEquals(skiLift.hashCode(), identicalSkiLift.hashCode());
    }

    @Test
    public void hashCode_should_be_different_for_different_objects() {
        SkiLift differentSkiLift = SkiLift.builder().id(2L).name("Different Lift").type(SkiLiftType.TELESKI)
                .status(SkiLiftStatus.CLOSED).comment("Different Comment").availableSports(new HashSet<>(Set.of()))
                .commissioningDate(LocalDate.now().minusYears(1)).build();

        assertNotEquals(skiLift.hashCode(), differentSkiLift.hashCode());
    }
}