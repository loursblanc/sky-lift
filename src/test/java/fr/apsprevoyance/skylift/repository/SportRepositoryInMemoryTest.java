package fr.apsprevoyance.skylift.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.model.Sport;

@Tag("repository")
class SportRepositoryInMemoryTest {

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
        // Arrange
        Sport sport1 = Sport.builder().name("Sport 1").season(Season.WINTER).build();
        Sport sport2 = Sport.builder().name("Sport 2").season(Season.WINTER).build();
        Sport sport3 = Sport.builder().name("Sport 3").season(Season.WINTER).build();

        // Act
        Sport created1 = repository.create(sport1);
        Sport created2 = repository.create(sport2);
        Sport created3 = repository.create(sport3);

        // Assert
        assertEquals(1L, created1.getId());
        assertEquals(2L, created2.getId());
        assertEquals(3L, created3.getId());
    }

    @Test
    void create_with_null_sport_throws_nullpointerexception() {
        assertThrows(NullPointerException.class, () -> {
            repository.create(null);
        });
    }

    @Test
    void create_with_predefined_id_throws_validationexception() {

        Sport sportWithId = Sport.builder().id(123L).name(TestConstants.Sport.VALID_NAME)
                .season(TestConstants.Sport.VALID_SEASON).build();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            repository.create(sportWithId);
        });

        assertEquals(ValidationContextType.PERSISTENCE, exception.getContextType());
    }

    /*
     * @Test void findAll_returns_all_created_sports() { // Arrange List<Sport>
     * expected = new ArrayList<>();
     * expected.add(repository.create(Sport.builder().name("Sport 1").season(Season.
     * WINTER).build()));
     * expected.add(repository.create(Sport.builder().name("Sport 2").season(Season.
     * SUMMER).build()));
     * expected.add(repository.create(Sport.builder().name("Sport 3").season(Season.
     * BOTH).build()));
     * 
     * // Act List<Sport> result = repository.findAll();
     * 
     * // Assert assertEquals(3, result.size());
     * assertEquals(expected.get(0).getId(), result.get(0).getId());
     * assertEquals(expected.get(1).getId(), result.get(1).getId());
     * assertEquals(expected.get(2).getId(), result.get(2).getId()); }
     */
}