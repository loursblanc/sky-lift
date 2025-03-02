package fr.apsprevoyance.skylift.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.DuplicateEntityException;
import fr.apsprevoyance.skylift.exception.EntityNotFoundException;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.mapper.SkiLiftMapper;
import fr.apsprevoyance.skylift.model.SkiLift;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.entity.SkiLiftEntity;
import fr.apsprevoyance.skylift.repository.entity.SportEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Tag(TestTag.REPOSITORY)
@ExtendWith(MockitoExtension.class)
class SkiLiftRepositoryJpaTest {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_ID = "id";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_STATUS = "status";
    private static final Long COUNT_ZERO = 0L;
    private static final Long COUNT_ONE = 1L;
    private static final String MODEL_NAME = SkiLift.class.getSimpleName();
    private static final String SPORT_MODEL_NAME = "Sport";

    @Mock
    private TypedQuery<SkiLiftEntity> entityQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    @Mock
    private EntityManager entityManager;

    @Mock
    private SkiLiftMapper skiLiftMapper;

    private SkiLiftRepositoryJpa repository;

    private SkiLift validSkiLift;
    private SkiLift skiLiftWithId;
    private SkiLift skiLiftWithEmptyName;
    private SkiLiftEntity skiLiftEntity;
    private SkiLiftEntity persistedEntity;
    private Sport validSport;
    private SportEntity sportEntity;
    private Set<Sport> validSports;
    private Set<SportEntity> sportEntities;

    @BeforeEach
    void setUp() {
        repository = new SkiLiftRepositoryJpa(entityManager, skiLiftMapper);

        validSport = Sport.builder().id(TestConstants.Sport.VALID_ID).name(TestConstants.Sport.VALID_NAME).build();

        sportEntity = new SportEntity(TestConstants.Sport.VALID_ID, TestConstants.Sport.VALID_NAME,
                TestConstants.Sport.VALID_DESCRIPTION, TestConstants.Sport.VALID_SEASON,
                TestConstants.Sport.VALID_ACTIVE);

        validSports = new HashSet<>();
        validSports.add(validSport);

        sportEntities = new HashSet<>();
        sportEntities.add(sportEntity);

        validSkiLift = SkiLift.builder().name(TestConstants.SkiLift.VALID_NAME).type(TestConstants.SkiLift.VALID_TYPE)
                .status(TestConstants.SkiLift.VALID_STATUS).comment(TestConstants.SkiLift.VALID_COMMENT)
                .commissioningDate(TestConstants.SkiLift.VALID_DATE).availableSports(validSports).build();

        skiLiftWithId = SkiLift.builder().id(TestConstants.SkiLift.VALID_ID).name(TestConstants.SkiLift.VALID_NAME)
                .type(TestConstants.SkiLift.VALID_TYPE).status(TestConstants.SkiLift.VALID_STATUS)
                .comment(TestConstants.SkiLift.VALID_COMMENT).commissioningDate(TestConstants.SkiLift.VALID_DATE)
                .availableSports(validSports).build();

        skiLiftWithEmptyName = SkiLift.builder().name("").type(TestConstants.SkiLift.VALID_TYPE)
                .status(TestConstants.SkiLift.VALID_STATUS).comment(TestConstants.SkiLift.VALID_COMMENT)
                .commissioningDate(TestConstants.SkiLift.VALID_DATE).availableSports(validSports).build();

        skiLiftEntity = new SkiLiftEntity();
        skiLiftEntity.setName(TestConstants.SkiLift.VALID_NAME);
        skiLiftEntity.setType(TestConstants.SkiLift.VALID_TYPE);
        skiLiftEntity.setStatus(TestConstants.SkiLift.VALID_STATUS);
        skiLiftEntity.setComment(TestConstants.SkiLift.VALID_COMMENT);
        skiLiftEntity.setCommissioningDate(TestConstants.SkiLift.VALID_DATE);
        skiLiftEntity.setAvailableSports(sportEntities);

        persistedEntity = new SkiLiftEntity();
        persistedEntity.setId(TestConstants.SkiLift.VALID_ID);
        persistedEntity.setName(TestConstants.SkiLift.VALID_NAME);
        persistedEntity.setType(TestConstants.SkiLift.VALID_TYPE);
        persistedEntity.setStatus(TestConstants.SkiLift.VALID_STATUS);
        persistedEntity.setComment(TestConstants.SkiLift.VALID_COMMENT);
        persistedEntity.setCommissioningDate(TestConstants.SkiLift.VALID_DATE);
        persistedEntity.setAvailableSports(sportEntities);
    }

    /*
     * @Test void
     * create_with_valid_skilift_should_persist_and_return_domain_object() {
     * TypedQuery<Long> skiLiftCountQuery = mock(TypedQuery.class); TypedQuery<Long>
     * sportCountQuery = mock(TypedQuery.class);
     * 
     * // Mock avec le nom exact du ski lift
     * lenient().when(entityManager.createQuery(JpqlRequest.SkiLift.COUNT_BY_NAME,
     * Long.class)) .thenReturn(skiLiftCountQuery);
     * lenient().when(skiLiftCountQuery.setParameter(PARAM_NAME,
     * "Télécabine de la Combe")) .thenReturn(skiLiftCountQuery);
     * lenient().when(skiLiftCountQuery.getSingleResult()).thenReturn(COUNT_ZERO);
     * 
     * lenient().when(entityManager.createQuery(JpqlRequest.Sport.COUNT_BY_ID,
     * Long.class)) .thenReturn(sportCountQuery);
     * lenient().when(sportCountQuery.setParameter(PARAM_ID,
     * 111L)).thenReturn(sportCountQuery);
     * lenient().when(sportCountQuery.getSingleResult()).thenReturn(COUNT_ONE);
     * 
     * lenient().when(skiLiftMapper.toEntityForCreate(validSkiLift)).thenReturn(
     * skiLiftEntity); lenient().when(entityManager.find(SportEntity.class,
     * 111L)).thenReturn(sportEntity);
     * lenient().when(skiLiftMapper.toDomain(skiLiftEntity)).thenReturn(
     * skiLiftWithId);
     * 
     * SkiLift result = repository.create(validSkiLift);
     * 
     * assertNotNull(result); assertEquals(TestConstants.SkiLift.VALID_ID,
     * result.getId()); assertEquals("Télécabine de la Combe", result.getName());
     * verify(entityManager).persist(skiLiftEntity); }
     */
    @Test
    void create_with_null_skilift_should_throw_nullpointerexception() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            repository.create(null);
        });

        assertEquals(ErrorMessageConstants.Errors.SKILIFT_NULL, exception.getMessage());
    }

    @Test
    void create_with_predefined_id_should_throw_validationexception() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            repository.create(skiLiftWithId);
        });

        assertEquals(MODEL_NAME, exception.getModelName());
        assertEquals(ValidationContextType.PERSISTENCE, exception.getContextType());
        assertEquals(ErrorMessageConstants.Errors.SKILIFT_ID_PREDEFINED, exception.getValidationErrors().get(0));
    }

    @Test
    void create_with_empty_name_should_throw_validationexception() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            repository.create(skiLiftWithEmptyName);
        });

        assertEquals(MODEL_NAME, exception.getModelName());
        assertEquals(ValidationContextType.PERSISTENCE, exception.getContextType());
        assertEquals(ErrorMessageConstants.Errors.NAME_EMPTY, exception.getValidationErrors().get(0));
    }

    @Test
    void create_with_duplicate_name_should_throw_duplicateentityexception() {
        when(entityManager.createQuery(JpqlRequest.SkiLift.COUNT_BY_NAME, Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter(PARAM_NAME, TestConstants.SkiLift.VALID_NAME)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(COUNT_ONE);

        DuplicateEntityException exception = assertThrows(DuplicateEntityException.class, () -> {
            repository.create(validSkiLift);
        });

        assertEquals(MODEL_NAME, exception.getEntityName());
        assertEquals(ErrorMessageConstants.Fields.NAME, exception.getField());
        assertEquals(TestConstants.SkiLift.VALID_NAME, exception.getValue());
    }

    @Test
    void create_with_non_existent_sport_should_throw_entitynotfoundexception() {
        lenient().when(entityManager.createQuery(JpqlRequest.SkiLift.COUNT_BY_NAME, Long.class)).thenReturn(countQuery);
        lenient().when(countQuery.setParameter(PARAM_NAME, TestConstants.SkiLift.VALID_NAME)).thenReturn(countQuery);
        lenient().when(countQuery.getSingleResult()).thenReturn(COUNT_ZERO);

        // Mock de la requête pour vérifier l'existence du sport
        TypedQuery<Long> sportCountQuery = mock(TypedQuery.class);
        lenient().when(entityManager.createQuery(JpqlRequest.Sport.COUNT_BY_ID, Long.class))
                .thenReturn(sportCountQuery);
        lenient().when(sportCountQuery.setParameter(PARAM_ID, TestConstants.Sport.VALID_ID))
                .thenReturn(sportCountQuery);
        lenient().when(sportCountQuery.getSingleResult()).thenReturn(COUNT_ZERO);

        lenient().when(skiLiftMapper.toEntityForCreate(validSkiLift)).thenReturn(skiLiftEntity);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            repository.create(validSkiLift);
        });

        assertEquals(SPORT_MODEL_NAME, exception.getEntityName());
        assertEquals(TestConstants.Sport.VALID_ID.toString(), exception.getIdentifier());
    }

    @Test
    void findAll_should_return_list_of_skilifts() {
        SkiLiftEntity entity1 = new SkiLiftEntity();
        entity1.setName(TestConstants.SkiLift.VALID_NAME);

        SkiLiftEntity entity2 = new SkiLiftEntity();
        entity2.setName(TestConstants.SkiLift.DIFFERENT_NAME);

        SkiLift domainSkiLift1 = SkiLift.builder().name(TestConstants.SkiLift.VALID_NAME).build();

        SkiLift domainSkiLift2 = SkiLift.builder().name(TestConstants.SkiLift.DIFFERENT_NAME).build();

        when(entityManager.createQuery(JpqlRequest.SkiLift.FIND_ALL, SkiLiftEntity.class)).thenReturn(entityQuery);
        when(entityQuery.getResultList()).thenReturn(Arrays.asList(entity1, entity2));

        when(skiLiftMapper.toDomain(entity1)).thenReturn(domainSkiLift1);
        when(skiLiftMapper.toDomain(entity2)).thenReturn(domainSkiLift2);

        List<SkiLift> result = repository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(entityManager).createQuery(JpqlRequest.SkiLift.FIND_ALL, SkiLiftEntity.class);
    }

    @Test
    void findById_with_existing_id_should_return_skilift() {
        Long validId = TestConstants.SkiLift.VALID_ID;

        when(entityManager.find(SkiLiftEntity.class, validId)).thenReturn(persistedEntity);
        when(skiLiftMapper.toDomain(persistedEntity)).thenReturn(skiLiftWithId);

        Optional<SkiLift> result = repository.findById(validId);

        assertTrue(result.isPresent());
        assertEquals(skiLiftWithId, result.get());
    }

    @Test
    void findById_with_nonexistent_id_should_return_empty_optional() {
        Long nonExistentId = TestConstants.SkiLift.NON_EXISTENT_ID;

        when(entityManager.find(SkiLiftEntity.class, nonExistentId)).thenReturn(null);

        Optional<SkiLift> result = repository.findById(nonExistentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void update_should_update_and_return_updated_skilift() {
        SkiLift skiLiftToUpdate = SkiLift.builder().id(TestConstants.SkiLift.VALID_ID)
                .name(TestConstants.SkiLift.UPDATED_NAME).type(TestConstants.SkiLift.UPDATED_TYPE)
                .status(TestConstants.SkiLift.UPDATED_STATUS).comment(TestConstants.SkiLift.UPDATED_COMMENT)
                .commissioningDate(TestConstants.SkiLift.UPDATED_DATE).availableSports(validSports).build();

        SkiLiftEntity existingEntity = new SkiLiftEntity();
        existingEntity.setId(TestConstants.SkiLift.VALID_ID);
        existingEntity.setName(TestConstants.SkiLift.VALID_NAME);

        when(entityManager.createQuery("SELECT s FROM SkiLiftEntity s WHERE s.id = :id", SkiLiftEntity.class))
                .thenReturn(entityQuery);
        when(entityQuery.setParameter(PARAM_ID, TestConstants.SkiLift.VALID_ID)).thenReturn(entityQuery);
        when(entityQuery.getResultList()).thenReturn(Arrays.asList(existingEntity));

        when(entityManager.createQuery(JpqlRequest.SkiLift.COUNT_BY_NAME_EXCEPT_ID, Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter(PARAM_NAME, TestConstants.SkiLift.UPDATED_NAME)).thenReturn(countQuery);
        when(countQuery.setParameter(PARAM_ID, TestConstants.SkiLift.VALID_ID)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(COUNT_ZERO);

        when(entityManager.find(SportEntity.class, TestConstants.Sport.VALID_ID)).thenReturn(sportEntity);
        when(skiLiftMapper.toDomain(existingEntity)).thenReturn(skiLiftToUpdate);

        SkiLift result = repository.update(skiLiftToUpdate);

        assertNotNull(result);
        assertEquals(TestConstants.SkiLift.UPDATED_NAME, existingEntity.getName());
        assertEquals(TestConstants.SkiLift.UPDATED_TYPE, existingEntity.getType());
        assertEquals(TestConstants.SkiLift.UPDATED_STATUS, existingEntity.getStatus());
        assertEquals(TestConstants.SkiLift.UPDATED_COMMENT, existingEntity.getComment());
        assertEquals(TestConstants.SkiLift.UPDATED_DATE, existingEntity.getCommissioningDate());
    }

    @Test
    void update_with_nonexistent_id_should_throw_entitynotfoundexception() {
        SkiLift skiLiftWithNonExistentId = SkiLift.builder().id(TestConstants.SkiLift.NON_EXISTENT_ID)
                .name(TestConstants.SkiLift.VALID_NAME).build();

        when(entityManager.createQuery("SELECT s FROM SkiLiftEntity s WHERE s.id = :id", SkiLiftEntity.class))
                .thenReturn(entityQuery);
        when(entityQuery.setParameter(PARAM_ID, TestConstants.SkiLift.NON_EXISTENT_ID)).thenReturn(entityQuery);
        when(entityQuery.getResultList()).thenReturn(Arrays.asList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            repository.update(skiLiftWithNonExistentId);
        });

        assertEquals(MODEL_NAME, exception.getEntityName());
        assertEquals(TestConstants.SkiLift.NON_EXISTENT_ID.toString(), exception.getIdentifier());
    }

    @Test
    void delete_with_existing_id_should_remove_skilift() {
        Long validId = TestConstants.SkiLift.VALID_ID;

        SkiLiftEntity entityToDelete = new SkiLiftEntity();
        entityToDelete.setId(validId);
        entityToDelete.setAvailableSports(new HashSet<>());

        when(entityManager.find(SkiLiftEntity.class, validId)).thenReturn(entityToDelete);

        repository.delete(validId);

        verify(entityManager).remove(entityToDelete);
        verify(entityManager).flush();
    }

    @Test
    void delete_with_nonexistent_id_should_throw_entitynotfoundexception() {
        Long nonExistentId = TestConstants.SkiLift.NON_EXISTENT_ID;

        when(entityManager.find(SkiLiftEntity.class, nonExistentId)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            repository.delete(nonExistentId);
        });

        assertEquals(MODEL_NAME, exception.getEntityName());
        assertEquals(nonExistentId.toString(), exception.getIdentifier());
    }

    @Test
    void existsById_should_return_true_when_skilift_exists() {
        Long validId = TestConstants.SkiLift.VALID_ID;

        when(entityManager.createQuery(JpqlRequest.SkiLift.COUNT_BY_ID, Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter(PARAM_ID, validId)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(COUNT_ONE);

        boolean result = repository.existsById(validId);

        assertTrue(result);
    }

    @Test
    void existsById_should_return_false_when_skilift_does_not_exist() {
        Long nonExistentId = TestConstants.SkiLift.NON_EXISTENT_ID;

        when(entityManager.createQuery(JpqlRequest.SkiLift.COUNT_BY_ID, Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter(PARAM_ID, nonExistentId)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(COUNT_ZERO);

        boolean result = repository.existsById(nonExistentId);

        assertFalse(result);
    }

    @Test
    void findByTypeAndStatus_should_return_filtered_list() {
        SkiLiftType type = TestConstants.SkiLift.VALID_TYPE;
        SkiLiftStatus status = TestConstants.SkiLift.VALID_STATUS;

        SkiLiftEntity entity1 = new SkiLiftEntity();
        entity1.setType(type);
        entity1.setStatus(status);

        SkiLift domainSkiLift1 = SkiLift.builder().type(type).status(status).build();

        // Simuler la création dynamique de la requête
        when(entityManager.createQuery(
                "SELECT s FROM SkiLiftEntity s WHERE 1=1 AND s.type = :type AND s.status = :status",
                SkiLiftEntity.class)).thenReturn(entityQuery);
        when(entityQuery.setParameter(PARAM_TYPE, type)).thenReturn(entityQuery);
        when(entityQuery.setParameter(PARAM_STATUS, status)).thenReturn(entityQuery);
        when(entityQuery.getResultList()).thenReturn(Arrays.asList(entity1));

        when(skiLiftMapper.toDomainList(Arrays.asList(entity1))).thenReturn(Arrays.asList(domainSkiLift1));

        List<SkiLift> result = repository.findByTypeAndStatus(type, status);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(type, result.get(0).getType());
        assertEquals(status, result.get(0).getStatus());
    }

    @Test
    void findByTypeAndStatus_with_only_type_should_filter_by_type() {
        SkiLiftType type = TestConstants.SkiLift.VALID_TYPE;

        SkiLiftEntity entity1 = new SkiLiftEntity();
        entity1.setType(type);

        SkiLift domainSkiLift1 = SkiLift.builder().type(type).build();

        // Simuler la création dynamique de la requête avec Map de paramètres
        when(entityManager.createQuery("SELECT s FROM SkiLiftEntity s WHERE 1=1 AND s.type = :type",
                SkiLiftEntity.class)).thenReturn(entityQuery);
        when(entityQuery.setParameter(PARAM_TYPE, type)).thenReturn(entityQuery);
        when(entityQuery.getResultList()).thenReturn(Arrays.asList(entity1));

        when(skiLiftMapper.toDomainList(Arrays.asList(entity1))).thenReturn(Arrays.asList(domainSkiLift1));

        List<SkiLift> result = repository.findByTypeAndStatus(type, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(type, result.get(0).getType());
    }

    @Test
    void findByTypeAndStatus_with_no_filters_should_return_all() {
        SkiLiftEntity entity1 = new SkiLiftEntity();
        entity1.setType(TestConstants.SkiLift.VALID_TYPE);

        SkiLiftEntity entity2 = new SkiLiftEntity();
        entity2.setType(TestConstants.SkiLift.DIFFERENT_TYPE);

        List<SkiLiftEntity> allEntities = Arrays.asList(entity1, entity2);

        SkiLift domainSkiLift1 = SkiLift.builder().type(TestConstants.SkiLift.VALID_TYPE).build();
        SkiLift domainSkiLift2 = SkiLift.builder().type(TestConstants.SkiLift.DIFFERENT_TYPE).build();
        List<SkiLift> domainSkiLifts = Arrays.asList(domainSkiLift1, domainSkiLift2);

        // Simuler la création dynamique de la requête sans paramètres
        when(entityManager.createQuery("SELECT s FROM SkiLiftEntity s WHERE 1=1", SkiLiftEntity.class))
                .thenReturn(entityQuery);
        when(entityQuery.getResultList()).thenReturn(allEntities);

        when(skiLiftMapper.toDomainList(allEntities)).thenReturn(domainSkiLifts);

        List<SkiLift> result = repository.findByTypeAndStatus(null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}