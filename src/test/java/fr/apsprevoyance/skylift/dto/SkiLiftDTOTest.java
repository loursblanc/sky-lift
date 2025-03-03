package fr.apsprevoyance.skylift.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Tag("dto")
public class SkiLiftDTOTest {

    private SkiLiftDTO dto;
    private Validator validator;

    private static final Long VALID_ID = 1L;
    private static final String VALID_NAME = "Télésiège du Lac";
    private static final SkiLiftType VALID_TYPE = SkiLiftType.TELESIEGE;
    private static final SkiLiftStatus VALID_STATUS = SkiLiftStatus.OPEN;
    private static final String VALID_COMMENT = "Un commentaire valide";
    private static final LocalDate VALID_DATE = LocalDate.now();

    // Créer des Sports valides pour les tests
    private static final Sport SKI_SPORT = Sport.builder().name(SportLabels.SKI).season(Season.WINTER).active(true)
            .build();
    private static final Sport SNOWBOARD_SPORT = Sport.builder().name(SportLabels.SNOWBOARD).season(Season.WINTER)
            .active(true).build();
    private static final Set<Sport> VALID_SPORTS = new HashSet<>(Set.of(SKI_SPORT, SNOWBOARD_SPORT));

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        dto = new SkiLiftDTO();
        dto.setId(VALID_ID);
        dto.setName(VALID_NAME);
        dto.setType(VALID_TYPE);
        dto.setStatus(VALID_STATUS);
        dto.setComment(VALID_COMMENT);
        dto.setAvailableSports(VALID_SPORTS);
        dto.setCommissioningDate(VALID_DATE);
    }

    // Méthode utilitaire pour créer un Sport
    private Sport createSport(String name) {
        return Sport.builder().name(name).season(Season.WINTER).active(true).build();
    }

    // Les méthodes de test restent similaires, avec des ajustements pour utiliser
    // des Sport

    @Test
    public void empty_availableSports_fails_validation() {
        dto.setAvailableSports(new HashSet<>());

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("availableSports")));
    }

    @Test
    public void null_availableSports_is_replaced_with_empty_set() {
        dto.setAvailableSports(null);

        assertNotNull(dto.getAvailableSports());
        assertTrue(dto.getAvailableSports().isEmpty());
    }

    @Test
    public void getters_and_setters_work_correctly() {
        assertEquals(VALID_ID, dto.getId());
        assertEquals(VALID_NAME, dto.getName());
        assertEquals(VALID_TYPE, dto.getType());
        assertEquals(VALID_STATUS, dto.getStatus());
        assertEquals(VALID_COMMENT, dto.getComment());
        assertEquals(VALID_SPORTS, dto.getAvailableSports());
        assertEquals(VALID_DATE, dto.getCommissioningDate());

        // Test setters with new values
        Long newId = 2L;
        String newName = "Nouveau Télésiège";
        SkiLiftType newType = SkiLiftType.TELESKI;
        SkiLiftStatus newStatus = SkiLiftStatus.CLOSED;
        String newComment = "Nouveau commentaire";

        // Créer un nouvel ensemble de Sports
        Sport snowscootSport = createSport(SportLabels.SNOWSCOOT);
        Set<Sport> newSports = new HashSet<>(Set.of(snowscootSport));

        LocalDate newDate = LocalDate.now().plusDays(1);

        dto.setId(newId);
        dto.setName(newName);
        dto.setType(newType);
        dto.setStatus(newStatus);
        dto.setComment(newComment);
        dto.setAvailableSports(newSports);
        dto.setCommissioningDate(newDate);

        assertEquals(newId, dto.getId());
        assertEquals(newName, dto.getName());
        assertEquals(newType, dto.getType());
        assertEquals(newStatus, dto.getStatus());
        assertEquals(newComment, dto.getComment());
        assertEquals(newSports, dto.getAvailableSports());
        assertEquals(newDate, dto.getCommissioningDate());
    }

    @Test
    public void dto_with_valid_values_passes_validation() {
        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto, OnUpdate.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void dto_without_id_passes_onCreate_validation() {
        dto.setId(null);

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto, OnCreate.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void dto_without_id_fails_onUpdate_validation() {
        dto.setId(null);

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto, OnUpdate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("id")));
    }

    @Test
    public void negative_id_fails_validation() {
        dto.setId(-1L);

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("id") && v.getMessage().contains("positive")));
    }

    @Test
    public void empty_name_fails_validation() {
        dto.setName("");

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void too_short_name_fails_validation() {
        dto.setName("AB");

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void too_long_name_fails_validation() {
        // Create a name that is 1 character longer than allowed
        StringBuilder name = new StringBuilder();
        for (int i = 0; i <= ValidationConstants.NAME_MAX_LENGTH; i++) {
            name.append("A");
        }

        dto.setName(name.toString());

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void invalid_name_character_fails_validation() {
        dto.setName("Invalid-Name!"); // Contains invalid characters

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void null_type_fails_validation() {
        dto.setType(null);

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("type")));
    }

    @Test
    public void null_status_fails_validation() {
        dto.setStatus(null);

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
    }

    @Test
    public void null_commissioning_date_fails_validation() {
        dto.setCommissioningDate(null);

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("commissioningDate")));
    }

    @Test
    public void too_old_commissioning_date_fails_validation() {

        dto.setCommissioningDate(ValidationConstants.FIRST_SKILIFT_DATE.minusDays(1));

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("commissioningDate")));
    }

    @Test
    public void null_comment_is_replaced_with_empty_string() {
        dto.setComment(null);

        assertEquals("", dto.getComment());
    }

    @Test
    public void too_long_comment_fails_validation() {

        StringBuilder comment = new StringBuilder();
        for (int i = 0; i <= ValidationConstants.DESCRIPTION_MAX_LENGTH; i++) {
            comment.append("A");
        }

        dto.setComment(comment.toString());

        Set<ConstraintViolation<SkiLiftDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("comment")));
    }

}