package fr.apsprevoyance.skylift.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.JpqlRequest;
import fr.apsprevoyance.skylift.constants.TestConstants;
import fr.apsprevoyance.skylift.constants.TestTag;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.DuplicateEntityException;
import fr.apsprevoyance.skylift.exception.EntityNotFoundException;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.mapper.SportMapper;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.entity.SportEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Tag(TestTag.REPOSITORY)
@ExtendWith(MockitoExtension.class)
class SportRepositoryJpaTest {

    private static final String PARAM_NAME = "name";
    private static final Long COUNT_ZERO = 0L;
    private static final Long COUNT_ONE = 1L;
    private static final String MODEL_NAME = Sport.class.getSimpleName();

    @Mock
    private TypedQuery<SportEntity> entityQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    @Mock
    private EntityManager entityManager;

    @Mock
    private SportMapper sportMapper;

    private SportRepositoryJpa repository;

    private Sport validSport;
    private Sport sportWithId;
    private Sport sportWithEmptyName;
    private SportEntity sportEntity;
    private SportEntity persistedEntity;

    @BeforeEach
    void setUp() {
        repository = new SportRepositoryJpa(entityManager, sportMapper);

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
        when(entityManager.createQuery(JpqlRequest.Sport.COUNT_BY_NAME, Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter(PARAM_NAME, TestConstants.Sport.VALID_NAME)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(COUNT_ZERO);
        when(sportMapper.toEntityForCreate(validSport)).thenReturn(sportEntity);
        when(sportMapper.toDomain(sportEntity)).thenReturn(sportWithId);

        // Act
        Sport result = repository.create(validSport);

        // Assert
        assertNotNull(result);
        assertEquals(TestConstants.Sport.VALID_ID, result.getId());
        assertEquals(TestConstants.Sport.VALID_NAME, result.getName());
        verify(entityManager).persist(sportEntity);
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

        assertEquals(MODEL_NAME, exception.getModelName());
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
        when(entityManager.createQuery(JpqlRequest.Sport.COUNT_BY_NAME, Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter(PARAM_NAME, TestConstants.Sport.VALID_NAME)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(COUNT_ONE);

        // Act & Assert
        DuplicateEntityException exception = assertThrows(DuplicateEntityException.class, () -> {
            repository.create(validSport);
        });

        assertEquals(MODEL_NAME, exception.getEntityName());
        assertEquals(ErrorMessageConstants.Fields.NAME, exception.getField());
        assertEquals(TestConstants.Sport.VALID_NAME, exception.getValue());
    }

    @Test
    void findAll_should_return_list_of_sports() {
        // Arrange
        SportEntity entity1 = new SportEntity(TestConstants.Sport.VALID_NAME, TestConstants.Sport.VALID_DESCRIPTION,
                TestConstants.Sport.VALID_SEASON, TestConstants.Sport.VALID_ACTIVE);

        SportEntity entity2 = new SportEntity(TestConstants.Sport.DIFFERENT_NAME,
                TestConstants.Sport.DIFFERENT_DESCRIPTION, TestConstants.Sport.VALID_SEASON,
                TestConstants.Sport.VALID_ACTIVE);

        Sport domainSport1 = Sport.builder().name(TestConstants.Sport.VALID_NAME)
                .description(TestConstants.Sport.VALID_DESCRIPTION).season(TestConstants.Sport.VALID_SEASON)
                .active(TestConstants.Sport.VALID_ACTIVE).build();

        Sport domainSport2 = Sport.builder().name(TestConstants.Sport.DIFFERENT_NAME)
                .description(TestConstants.Sport.DIFFERENT_DESCRIPTION).season(TestConstants.Sport.VALID_SEASON)
                .active(TestConstants.Sport.VALID_ACTIVE).build();

        when(entityManager.createQuery(JpqlRequest.Sport.FIND_ALL, SportEntity.class)).thenReturn(entityQuery);
        when(entityQuery.getResultList()).thenReturn(Arrays.asList(entity1, entity2));

        when(sportMapper.toDomain(entity1)).thenReturn(domainSport1);
        when(sportMapper.toDomain(entity2)).thenReturn(domainSport2);

        // Act
        List<Sport> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(entityManager).createQuery(JpqlRequest.Sport.FIND_ALL, SportEntity.class);
    }

    @Test
    void findById_with_existing_id_should_return_sport() {
        // Arrange
        Long validId = TestConstants.Sport.VALID_ID;
        SportEntity entity = new SportEntity(validId, TestConstants.Sport.VALID_NAME,
                TestConstants.Sport.VALID_DESCRIPTION, TestConstants.Sport.VALID_SEASON,
                TestConstants.Sport.VALID_ACTIVE);

        Sport domainSport = Sport.builder().id(validId).name(TestConstants.Sport.VALID_NAME)
                .description(TestConstants.Sport.VALID_DESCRIPTION).season(TestConstants.Sport.VALID_SEASON)
                .active(TestConstants.Sport.VALID_ACTIVE).build();

        when(entityManager.createQuery(JpqlRequest.Sport.FIND_BY_ID, SportEntity.class)).thenReturn(entityQuery);
        when(entityQuery.setParameter("id", validId)).thenReturn(entityQuery);
        when(entityQuery.getSingleResult()).thenReturn(entity);
        when(sportMapper.toDomain(entity)).thenReturn(domainSport);

        // Act
        Optional<Sport> result = repository.findById(validId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(domainSport, result.get());
    }

    @Test
    void findById_with_nonexistent_id_should_return_empty_optional() {
        // Arrange
        Long nonExistentId = 999L;

        when(entityManager.createQuery(JpqlRequest.Sport.FIND_BY_ID, SportEntity.class)).thenReturn(entityQuery);
        when(entityQuery.setParameter("id", nonExistentId)).thenReturn(entityQuery);
        when(entityQuery.getSingleResult()).thenThrow(NoResultException.class);

        // Act
        Optional<Sport> result = repository.findById(nonExistentId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void update_should_merge_and_return_updated_sport() {
        // Arrange
        Sport sportToUpdate = Sport.builder().id(TestConstants.Sport.VALID_ID).name(TestConstants.Sport.VALID_NAME)
                .description("Updated Description").season(TestConstants.Sport.VALID_SEASON).active(false).build();

        SportEntity entityToUpdate = new SportEntity(TestConstants.Sport.VALID_ID, TestConstants.Sport.VALID_NAME,
                "Updated Description", TestConstants.Sport.VALID_SEASON, false);

        SportEntity updatedEntity = new SportEntity(TestConstants.Sport.VALID_ID, TestConstants.Sport.VALID_NAME,
                "Updated Description", TestConstants.Sport.VALID_SEASON, false);

        Sport updatedDomainSport = Sport.builder().id(TestConstants.Sport.VALID_ID).name(TestConstants.Sport.VALID_NAME)
                .description("Updated Description").season(TestConstants.Sport.VALID_SEASON).active(false).build();

        when(entityManager.createQuery(JpqlRequest.Sport.COUNT_BY_NAME_EXCLUDING_CURRENT, Long.class))
                .thenReturn(countQuery);
        when(countQuery.setParameter("name", sportToUpdate.getName())).thenReturn(countQuery);
        when(countQuery.setParameter("id", sportToUpdate.getId())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        when(sportMapper.toEntityForUpdate(sportToUpdate)).thenReturn(entityToUpdate);
        when(entityManager.merge(entityToUpdate)).thenReturn(updatedEntity);
        when(sportMapper.toDomain(updatedEntity)).thenReturn(updatedDomainSport);

        // Act
        Sport result = repository.update(sportToUpdate);

        // Assert
        assertNotNull(result);
        assertEquals(updatedDomainSport, result);
        verify(entityManager).merge(entityToUpdate);
    }

    @Test
    void delete_with_existing_id_should_remove_sport() {
        // Arrange
        Long validId = TestConstants.Sport.VALID_ID;
        SportEntity entity = new SportEntity(validId, TestConstants.Sport.VALID_NAME,
                TestConstants.Sport.VALID_DESCRIPTION, TestConstants.Sport.VALID_SEASON,
                TestConstants.Sport.VALID_ACTIVE);

        when(entityManager.createQuery(JpqlRequest.Sport.FIND_BY_ID, SportEntity.class)).thenReturn(entityQuery);
        when(entityQuery.setParameter("id", validId)).thenReturn(entityQuery);
        when(entityQuery.getSingleResult()).thenReturn(entity);

        // Act
        repository.delete(validId);

        // Assert
        verify(entityManager).remove(entity);
    }

    @Test
    void delete_with_nonexistent_id_should_throw_entitynotfoundexception() {
        // Arrange
        Long nonExistentId = 999L;

        when(entityManager.createQuery(JpqlRequest.Sport.FIND_BY_ID, SportEntity.class)).thenReturn(entityQuery);
        when(entityQuery.setParameter("id", nonExistentId)).thenReturn(entityQuery);
        when(entityQuery.getSingleResult()).thenThrow(NoResultException.class);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            repository.delete(nonExistentId);
        });
    }

    @Test
    void existsById_should_return_true_when_sport_exists() {
        // Arrange
        Long validId = TestConstants.Sport.VALID_ID;

        when(entityManager.createQuery(JpqlRequest.Sport.COUNT_BY_ID, Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter("id", validId)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        // Act
        boolean result = repository.existsById(validId);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_should_return_false_when_sport_does_not_exist() {
        // Arrange
        Long nonExistentId = 999L;

        when(entityManager.createQuery(JpqlRequest.Sport.COUNT_BY_ID, Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter("id", nonExistentId)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        // Act
        boolean result = repository.existsById(nonExistentId);

        // Assert
        assertFalse(result);
    }
}