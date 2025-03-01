package fr.apsprevoyance.skylift.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.EntityNotFoundException;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.model.Sport;

@Tag(TestTag.REPOSITORY)
class SportRepositoryInMemoryTest {

    private static final String ENTITY_NAME = "Sport";
    private static final String SPORT_NAME_1 = "Sport 1";
    private static final String SPORT_NAME_2 = "Sport 2";
    private static final String SPORT_NAME_3 = "Sport 3";
    private static final String SPORT_DESCRIPTION = "Description test";
    private static final Long NONEXISTENT_ID = 9999L;
    private static final Long PREDEFINED_ID = 123L;

    private SportRepositoryInMemory repository;

    @BeforeEach
    void setUp() {
        repository = new SportRepositoryInMemory();
    }

    @Test
    void create_with_valid_sport_assigns_id_and_returns_new_instance() {
        Sport sportToCreate = Sport.builder().name(TestConstants.Sport.VALID_NAME)
                .description(TestConstants.Sport.VALID_DESCRIPTION).active(TestConstants.Sport.VALID_ACTIVE)
                .season(TestConstants.Sport.VALID_SEASON).build();

        Sport createdSport = repository.create(sportToCreate);

        assertNotNull(createdSport);
        assertNotNull(createdSport.getId());
        assertEquals(1L, createdSport.getId());
        assertEquals(sportToCreate.getName(), createdSport.getName());
        assertEquals(sportToCreate.getDescription(), createdSport.getDescription());
        assertEquals(sportToCreate.isActive(), createdSport.isActive());
        assertEquals(sportToCreate.getSeason(), createdSport.getSeason());
    }

    @Test
    void create_assigns_sequential_ids() {
        Sport sport1 = Sport.builder().name(SPORT_NAME_1).season(Season.WINTER).build();
        Sport sport2 = Sport.builder().name(SPORT_NAME_2).season(Season.WINTER).build();
        Sport sport3 = Sport.builder().name(SPORT_NAME_3).season(Season.WINTER).build();

        Sport created1 = repository.create(sport1);
        Sport created2 = repository.create(sport2);
        Sport created3 = repository.create(sport3);

        assertEquals(1L, created1.getId());
        assertEquals(2L, created2.getId());
        assertEquals(3L, created3.getId());
    }

    @Test
    void create_with_null_sport_throws_nullpointerexception() {
        assertThrows(NullPointerException.class, () -> repository.create(null));
    }

    @Test
    void create_with_predefined_id_throws_validationexception() {
        Sport sportWithId = Sport.builder().id(PREDEFINED_ID).name(TestConstants.Sport.VALID_NAME)
                .season(TestConstants.Sport.VALID_SEASON).build();

        ValidationException exception = assertThrows(ValidationException.class, () -> repository.create(sportWithId));

        assertEquals(ValidationContextType.PERSISTENCE, exception.getContextType());
    }

    @Test
    void findAll_returns_all_created_sports() {
        List<Sport> initialSports = repository.findAll();
        assertEquals(0, initialSports.size());

        repository.create(Sport.builder().name(SPORT_NAME_1).season(Season.WINTER).build());
        repository.create(Sport.builder().name(SPORT_NAME_2).season(Season.SUMMER).build());
        repository.create(Sport.builder().name(SPORT_NAME_3).season(Season.BOTH).build());

        List<Sport> result = repository.findAll();

        assertEquals(3, result.size());
        assertEquals(SPORT_NAME_1, result.get(0).getName());
        assertEquals(SPORT_NAME_2, result.get(1).getName());
        assertEquals(SPORT_NAME_3, result.get(2).getName());
    }

    @Test
    void findById_returns_sport_when_found() {
        Sport createdSport = repository.create(
                Sport.builder().name(SPORT_NAME_1).description(SPORT_DESCRIPTION).season(Season.WINTER).build());

        Optional<Sport> result = repository.findById(createdSport.getId());

        assertTrue(result.isPresent());
        assertEquals(createdSport.getId(), result.get().getId());
        assertEquals(SPORT_NAME_1, result.get().getName());
        assertEquals(SPORT_DESCRIPTION, result.get().getDescription());
    }

    @Test
    void findById_returns_empty_optional_when_not_found() {
        Optional<Sport> result = repository.findById(NONEXISTENT_ID);

        assertFalse(result.isPresent());
    }

    @Test
    void findById_throws_exception_when_id_is_null() {
        assertThrows(NullPointerException.class, () -> repository.findById(null));
    }

    @Test
    void update_returns_updated_sport() {
        Sport createdSport = repository.create(Sport.builder().name(SPORT_NAME_1).season(Season.WINTER).build());

        Sport sportToUpdate = Sport.builder().id(createdSport.getId()).name(SPORT_NAME_2).description(SPORT_DESCRIPTION)
                .active(false).season(Season.SUMMER).build();

        Sport updatedSport = repository.update(sportToUpdate);

        assertEquals(createdSport.getId(), updatedSport.getId());
        assertEquals(SPORT_NAME_2, updatedSport.getName());
        assertEquals(SPORT_DESCRIPTION, updatedSport.getDescription());
        assertFalse(updatedSport.isActive());
        assertEquals(Season.SUMMER, updatedSport.getSeason());
    }

    @Test
    void update_throws_exception_when_sport_not_found() {
        Sport sportToUpdate = Sport.builder().id(NONEXISTENT_ID).name(SPORT_NAME_1).season(Season.WINTER).build();

        assertThrows(EntityNotFoundException.class, () -> repository.update(sportToUpdate));
    }

    @Test
    void update_throws_exception_when_sport_is_null() {
        assertThrows(NullPointerException.class, () -> repository.update(null));
    }

    @Test
    void update_throws_exception_when_id_is_null() {
        Sport sportWithoutId = Sport.builder().name(SPORT_NAME_1).season(Season.WINTER).build();

        assertThrows(NullPointerException.class, () -> repository.update(sportWithoutId));
    }

    @Test
    void delete_removes_sport_when_found() {
        Sport createdSport = repository.create(Sport.builder().name(SPORT_NAME_1).season(Season.WINTER).build());

        repository.delete(createdSport.getId());

        List<Sport> remainingSports = repository.findAll();
        assertEquals(0, remainingSports.size());
    }

    @Test
    void delete_throws_exception_when_sport_not_found() {
        assertThrows(EntityNotFoundException.class, () -> repository.delete(NONEXISTENT_ID));
    }

    @Test
    void delete_throws_exception_when_id_is_null() {
        assertThrows(NullPointerException.class, () -> repository.delete(null));
    }

    @Test
    void existsById_returns_true_when_sport_exists() {
        Sport createdSport = repository.create(Sport.builder().name(SPORT_NAME_1).season(Season.WINTER).build());

        boolean result = repository.existsById(createdSport.getId());

        assertTrue(result);
    }

    @Test
    void existsById_returns_false_when_sport_does_not_exist() {
        boolean result = repository.existsById(NONEXISTENT_ID);

        assertFalse(result);
    }

    @Test
    void existsById_throws_exception_when_id_is_null() {
        assertThrows(NullPointerException.class, () -> repository.existsById(null));
    }
}