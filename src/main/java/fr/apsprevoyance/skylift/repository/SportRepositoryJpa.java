package fr.apsprevoyance.skylift.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.JpqlRequest;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.DuplicateEntityException;
import fr.apsprevoyance.skylift.exception.EntityNotFoundException;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.mapper.SportMapper;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.entity.SportEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

@Repository
@Primary

public class SportRepositoryJpa implements SportRepository {

    private final EntityManager entityManager;
    private final SportMapper sportMapper;

    private static final String MODEL_NAME = Sport.class.getSimpleName();

    public SportRepositoryJpa(EntityManager entityManager, SportMapper sportMapper) {
        this.entityManager = Objects.requireNonNull(entityManager,
                String.format(ErrorMessageConstants.Validation.FIELD_NULL, "EntityManager"));
        this.sportMapper = Objects.requireNonNull(sportMapper,
                String.format(ErrorMessageConstants.Validation.FIELD_NULL, "SportMapper"));
    }

    @Override
    @Transactional
    public Sport create(Sport sport) {
        Objects.requireNonNull(sport, ErrorMessageConstants.Errors.SPORT_NULL);

        if (sport.getId() != null) {
            throw new ValidationException(MODEL_NAME, ValidationContextType.PERSISTENCE,
                    ErrorMessageConstants.Errors.SPORT_ID_PREDEFINED);
        }

        String sportName = sport.getName();
        if (sportName == null || sportName.isEmpty()) {
            throw new ValidationException(MODEL_NAME, ValidationContextType.PERSISTENCE,
                    ErrorMessageConstants.Errors.NAME_EMPTY);
        }

        long count = entityManager.createQuery(JpqlRequest.Sport.COUNT_BY_NAME, Long.class)
                .setParameter("name", sportName).getSingleResult();

        if (count > 0) {
            throw new DuplicateEntityException(MODEL_NAME, ErrorMessageConstants.Fields.NAME, sportName);
        }

        SportEntity entity = sportMapper.toEntityForCreate(sport);
        entityManager.persist(entity);

        return sportMapper.toDomain(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sport> findAll() {
        List<SportEntity> entities = entityManager.createQuery(JpqlRequest.Sport.FIND_ALL, SportEntity.class)
                .getResultList();

        return entities.stream().map(sportMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sport> findById(Long id) {
        Objects.requireNonNull(id, ErrorMessageConstants.Errors.ID_NULL);

        try {
            SportEntity entity = entityManager.createQuery(JpqlRequest.Sport.FIND_BY_ID, SportEntity.class)
                    .setParameter("id", id).getSingleResult();

            return Optional.of(sportMapper.toDomain(entity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Sport update(Sport sport) {
        Objects.requireNonNull(sport, ErrorMessageConstants.Errors.SPORT_NULL);
        Objects.requireNonNull(sport.getId(), ErrorMessageConstants.Errors.ID_NULL);

        long count = entityManager.createQuery(JpqlRequest.Sport.COUNT_BY_NAME_EXCLUDING_CURRENT, Long.class)
                .setParameter("name", sport.getName()).setParameter("id", sport.getId()).getSingleResult();

        if (count > 0) {
            throw new DuplicateEntityException(MODEL_NAME, ErrorMessageConstants.Fields.NAME, sport.getName());
        }

        SportEntity entityToUpdate = sportMapper.toEntityForUpdate(sport);
        SportEntity updatedEntity = entityManager.merge(entityToUpdate);

        return sportMapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id, ErrorMessageConstants.Errors.ID_NULL);

        try {
            SportEntity entity = entityManager.createQuery(JpqlRequest.Sport.FIND_BY_ID, SportEntity.class)
                    .setParameter("id", id).getSingleResult();

            entityManager.remove(entity);
        } catch (NoResultException e) {
            throw new EntityNotFoundException(MODEL_NAME, id.toString());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        Objects.requireNonNull(id, ErrorMessageConstants.Errors.ID_NULL);

        Long count = entityManager.createQuery(JpqlRequest.Sport.COUNT_BY_ID, Long.class).setParameter("id", id)
                .getSingleResult();

        return count > 0;
    }
}