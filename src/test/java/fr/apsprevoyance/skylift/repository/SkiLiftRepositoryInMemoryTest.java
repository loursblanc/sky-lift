package fr.apsprevoyance.skylift.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.SportLabels;
import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.exception.EntityNotFoundException;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.model.SkiLift;
import fr.apsprevoyance.skylift.model.Sport;

@Tag(TestTag.REPOSITORY)
class SkiLiftRepositoryInMemoryTest {

    private static final class TestConstants {
        static final String ENTITY_NAME = "SkiLift";
        static final Long NONEXISTENT_ID = 9999L;
        static final Long PREDEFINED_ID = 123L;
        static final String LIFT_NAME_1 = "Télésiège des Marmottes";
        static final String LIFT_NAME_2 = "Télécabine de la Combe";
        static final String LIFT_NAME_3 = "Téléski du Glacier";
    }

    private SkiLiftRepositoryInMemory repository;

    @BeforeEach
    void setUp() {
        repository = new SkiLiftRepositoryInMemory();
    }

    private SkiLift createValidSkiLift(String name) {
        // Créer un Sport à partir du label
        Sport skiSport = Sport.builder().name(SportLabels.SKI).season(Season.WINTER).active(true).build();

        return SkiLift.builder().name(name).type(SkiLiftType.TELESIEGE).status(SkiLiftStatus.OPEN)
                .availableSports(Set.of(skiSport)).commissioningDate(LocalDate.now()).build();
    }

    @Test
    void create_withValidSkiLift_shouldAssignIdAndReturnNewInstance() {
        SkiLift skiLiftToCreate = createValidSkiLift(TestConstants.LIFT_NAME_1);

        SkiLift createdSkiLift = repository.create(skiLiftToCreate);

        assertNotNull(createdSkiLift);
        assertNotNull(createdSkiLift.getId());
        assertEquals(1L, createdSkiLift.getId());
        assertEquals(skiLiftToCreate.getName(), createdSkiLift.getName());
    }

    @Test
    void create_shouldAssignSequentialIds() {
        SkiLift skiLift1 = createValidSkiLift(TestConstants.LIFT_NAME_1);
        SkiLift skiLift2 = createValidSkiLift(TestConstants.LIFT_NAME_2);
        SkiLift skiLift3 = createValidSkiLift(TestConstants.LIFT_NAME_3);

        SkiLift created1 = repository.create(skiLift1);
        SkiLift created2 = repository.create(skiLift2);
        SkiLift created3 = repository.create(skiLift3);

        assertEquals(1L, created1.getId());
        assertEquals(2L, created2.getId());
        assertEquals(3L, created3.getId());
    }

    @Test
    void create_withNullSkiLift_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.create(null),
                ErrorMessageConstants.Errors.SPORT_NULL);
    }

    @Test
    void create_withPreDefinedId_shouldThrowValidationException() {
        SkiLift originalSkiLift = createValidSkiLift(TestConstants.LIFT_NAME_1);

        Sport skiSport = Sport.builder().name(SportLabels.SKI).season(Season.WINTER).active(true).build();

        SkiLift skiLiftWithId = SkiLift.builder().id(TestConstants.PREDEFINED_ID).name(originalSkiLift.getName())
                .type(originalSkiLift.getType()).status(originalSkiLift.getStatus()).availableSports(Set.of(skiSport))
                .commissioningDate(originalSkiLift.getCommissioningDate()).build();

        assertThrows(ValidationException.class, () -> repository.create(skiLiftWithId));
    }

    @Test
    void findAll_shouldReturnAllCreatedSkiLifts() {
        repository.create(createValidSkiLift(TestConstants.LIFT_NAME_1));
        repository.create(createValidSkiLift(TestConstants.LIFT_NAME_2));
        repository.create(createValidSkiLift(TestConstants.LIFT_NAME_3));

        List<SkiLift> result = repository.findAll();

        assertEquals(3, result.size());
        assertEquals(TestConstants.LIFT_NAME_1, result.get(0).getName());
        assertEquals(TestConstants.LIFT_NAME_2, result.get(1).getName());
        assertEquals(TestConstants.LIFT_NAME_3, result.get(2).getName());
    }

    @Test
    void findById_shouldReturnSkiLiftWhenFound() {
        SkiLift createdSkiLift = repository.create(createValidSkiLift(TestConstants.LIFT_NAME_1));

        Optional<SkiLift> result = repository.findById(createdSkiLift.getId());

        assertTrue(result.isPresent());
        assertEquals(createdSkiLift.getId(), result.get().getId());
        assertEquals(TestConstants.LIFT_NAME_1, result.get().getName());
    }

    @Test
    void findById_shouldReturnEmptyOptionalWhenNotFound() {
        Optional<SkiLift> result = repository.findById(TestConstants.NONEXISTENT_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldThrowExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> repository.findById(null), ErrorMessageConstants.Errors.ID_NULL);
    }

    @Test
    void update_shouldUpdateSkiLift() {
        SkiLift createdSkiLift = repository.create(createValidSkiLift(TestConstants.LIFT_NAME_1));

        Sport snowboardSport = Sport.builder().name(SportLabels.SNOWBOARD).season(Season.WINTER).active(true).build();

        SkiLift skiLiftToUpdate = SkiLift.builder().id(createdSkiLift.getId()).name(TestConstants.LIFT_NAME_2)
                .type(SkiLiftType.TELESKI).status(SkiLiftStatus.CLOSED).availableSports(Set.of(snowboardSport))
                .commissioningDate(LocalDate.now().minusYears(1)).build();

        SkiLift updatedSkiLift = repository.update(skiLiftToUpdate);

        assertEquals(createdSkiLift.getId(), updatedSkiLift.getId());
        assertEquals(TestConstants.LIFT_NAME_2, updatedSkiLift.getName());
        assertEquals(SkiLiftType.TELESKI, updatedSkiLift.getType());
        assertEquals(SkiLiftStatus.CLOSED, updatedSkiLift.getStatus());
    }

    @Test
    void update_shouldThrowExceptionWhenSkiLiftNotFound() {
        Sport skiSport = Sport.builder().name(SportLabels.SKI).season(Season.WINTER).active(true).build();

        SkiLift skiLiftToUpdate = SkiLift.builder().id(TestConstants.NONEXISTENT_ID).name(TestConstants.LIFT_NAME_1)
                .type(SkiLiftType.TELESIEGE).status(SkiLiftStatus.OPEN).availableSports(Set.of(skiSport))
                .commissioningDate(LocalDate.now()).build();

        assertThrows(EntityNotFoundException.class, () -> repository.update(skiLiftToUpdate));
    }

    @Test
    void delete_shouldRemoveSkiLift() {
        SkiLift createdSkiLift = repository.create(createValidSkiLift(TestConstants.LIFT_NAME_1));

        repository.delete(createdSkiLift.getId());

        List<SkiLift> remainingSkiLifts = repository.findAll();
        assertEquals(0, remainingSkiLifts.size());
    }

    @Test
    void delete_shouldThrowExceptionWhenSkiLiftNotFound() {
        assertThrows(EntityNotFoundException.class, () -> repository.delete(TestConstants.NONEXISTENT_ID));
    }

    @Test
    void existsById_shouldReturnTrueWhenSkiLiftExists() {
        SkiLift createdSkiLift = repository.create(createValidSkiLift(TestConstants.LIFT_NAME_1));

        boolean result = repository.existsById(createdSkiLift.getId());

        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalseWhenSkiLiftDoesNotExist() {
        boolean result = repository.existsById(TestConstants.NONEXISTENT_ID);

        assertFalse(result);
    }
}