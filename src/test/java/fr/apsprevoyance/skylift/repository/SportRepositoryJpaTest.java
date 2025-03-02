package fr.apsprevoyance.skylift.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.DuplicateEntityException;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.mapper.SportMapper;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.entity.SportEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Tag(TestTag.REPOSITORY)
@ExtendWith(MockitoExtension.class)
class SportRepositoryJpaTest {

    private static final String JPQL_COUNT_BY_NAME = "SELECT COUNT(s) FROM SportEntity s WHERE s.name = :name";
    private static final String PARAM_NAME = "name";
    private static final Long COUNT_ZERO = 0L;
    private static final Long COUNT_ONE = 1L;
    private static final String MODEL_NAME = Sport.class.getSimpleName();
    private static final String REPOSITORY_CLASS_NAME = SportRepositoryJpa.class.getSimpleName();

    @Mock
    private EntityManager entityManager;

    @Mock
    private SportMapper sportMapper;

    @Mock
    private TypedQuery<Long> query;

    private SportRepositoryJpa repository;

    private Sport validSport;
    private Sport sportWithId;
    private Sport sportWithEmptyName;
    private SportEntity sportEntity;
    private SportEntity persistedEntity;

    @BeforeEach
    void setUp() {
        repository = new SportRepositoryJpa(sportMapper);
        repository.setEntityManager(entityManager);

        validSport = Sport.builder().name(TestConstants.Sport.VALID_NAME)
                .description(TestConstants.Sport.VALID_DESCRIPTION).season(TestConstants.Sport.VALID_SEASON)
                .active(TestConstants.Sport.VALID_ACTIVE).build();

        sportWithId = Sport.builder().id(TestConstants.Sport.VALID_ID).name(TestConstants.Sport.VALID_NAME)
                .description(TestConstants.Sport.VALID_DESCRIPTION).season(TestConstants.Sport.VALID_SEASON)
                .active(TestConstants.Sport.VALID_ACTIVE).build();

        sportWithEmptyName = Sport.builder().name("").description(TestConstants.Sport.VALID_DESCRIPTION)
                .season(TestConstants.Sport.VALID_SEASON).active(TestConstants.Sport.VALID_ACTIVE).build();

        sportEntity = new SportEntity(TestConstants.Sport.VALID_NAME, TestConstants.Sport.VALID_DESCRIPTION,
                TestConstants.Sport.VALID_SEASON, TestConstants.Sport.VALID_ACTIVE);

        persistedEntity = new SportEntity(TestConstants.Sport.VALID_ID, TestConstants.Sport.VALID_NAME,
                TestConstants.Sport.VALID_DESCRIPTION, TestConstants.Sport.VALID_SEASON,
                TestConstants.Sport.VALID_ACTIVE);
    }

    @Test
    void create_with_valid_sport_should_persist_and_return_domain_object() {
        // Arrange
        when(entityManager.createQuery(JPQL_COUNT_BY_NAME, Long.class)).thenReturn(query);
        when(query.setParameter(PARAM_NAME, TestConstants.Sport.VALID_NAME)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(COUNT_ZERO);
        when(sportMapper.toEntityForCreate(validSport)).thenReturn(sportEntity);
        when(sportMapper.toDomain(sportEntity)).thenReturn(sportWithId);

        // Act
        Sport result = repository.create(validSport);

        // Assert
        assertNotNull(result);
        assertEquals(TestConstants.Sport.VALID_ID, result.getId());
        assertEquals(TestConstants.Sport.VALID_NAME, result.getName());
        verify(entityManager).persist(sportEntity);
        verify(entityManager).flush();
    }

    @Test
    void create_with_null_sport_should_throw_nullpointerexception() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            repository.create(null);
        });

        assertEquals(ErrorMessageConstants.Errors.SPORT_NULL, exception.getMessage());
    }

    @Test
    void create_with_predefined_id_should_throw_validationexception() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            repository.create(sportWithId);
        });

        assertEquals(REPOSITORY_CLASS_NAME, exception.getModelName());
        assertEquals(ValidationContextType.PERSISTENCE, exception.getContextType());
        assertEquals(ErrorMessageConstants.Errors.SPORT_ID_PREDEFINED, exception.getValidationErrors().get(0));
    }

    @Test
    void create_with_empty_name_should_throw_validationexception() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            repository.create(sportWithEmptyName);
        });

        assertEquals(MODEL_NAME, exception.getModelName());
        assertEquals(ValidationContextType.PERSISTENCE, exception.getContextType());
        assertEquals(ErrorMessageConstants.Errors.NAME_EMPTY, exception.getValidationErrors().get(0));
    }

    @Test
    void create_with_duplicate_name_should_throw_duplicateentityexception() {
        // Arrange
        when(entityManager.createQuery(JPQL_COUNT_BY_NAME, Long.class)).thenReturn(query);
        when(query.setParameter(PARAM_NAME, TestConstants.Sport.VALID_NAME)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(COUNT_ONE);

        // Act & Assert
        DuplicateEntityException exception = assertThrows(DuplicateEntityException.class, () -> {
            repository.create(validSport);
        });

        assertEquals(MODEL_NAME, exception.getEntityName());
        assertEquals(ErrorMessageConstants.Fields.NAME, exception.getField());
        assertEquals(TestConstants.Sport.VALID_NAME, exception.getValue());
    }
}