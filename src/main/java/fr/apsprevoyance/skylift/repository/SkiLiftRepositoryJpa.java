package fr.apsprevoyance.skylift.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
import fr.apsprevoyance.skylift.mapper.SkiLiftMapper;
import fr.apsprevoyance.skylift.model.SkiLift;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.entity.SkiLiftEntity;
import fr.apsprevoyance.skylift.repository.entity.SportEntity;
import jakarta.persistence.EntityManager;

@Repository
@Primary
public class SkiLiftRepositoryJpa implements SkiLiftRepository {

    private final EntityManager entityManager;
    private final SkiLiftMapper skiLiftMapper;

    private static final String MODEL_NAME = SkiLift.class.getSimpleName();

    public SkiLiftRepositoryJpa(EntityManager entityManager, SkiLiftMapper skiLiftMapper) {
        this.entityManager = Objects.requireNonNull(entityManager,
                String.format(ErrorMessageConstants.Validation.FIELD_NULL, "EntityManager"));
        this.skiLiftMapper = Objects.requireNonNull(skiLiftMapper,
                String.format(ErrorMessageConstants.Validation.FIELD_NULL, "SkiLiftMapper"));
    }

    @Override
    @Transactional
    public SkiLift create(SkiLift skiLift) {
        Objects.requireNonNull(skiLift, ErrorMessageConstants.Errors.SKILIFT_NULL);

        if (skiLift.getId() != null) {
            throw new ValidationException(MODEL_NAME, ValidationContextType.PERSISTENCE,
                    ErrorMessageConstants.Errors.SKILIFT_ID_PREDEFINED);
        }

        String skiLiftName = skiLift.getName();
        if (skiLiftName == null || skiLiftName.isEmpty()) {
            throw new ValidationException(MODEL_NAME, ValidationContextType.PERSISTENCE,
                    ErrorMessageConstants.Errors.NAME_EMPTY);
        }

        long count = entityManager.createQuery(JpqlRequest.SkiLift.COUNT_BY_NAME, Long.class)
                .setParameter("name", skiLiftName).getSingleResult();

        if (count > 0) {
            throw new DuplicateEntityException(MODEL_NAME, ErrorMessageConstants.Fields.NAME, skiLiftName);
        }

        Set<Sport> availableSports = skiLift.getAvailableSports();
        if (availableSports != null && !availableSports.isEmpty()) {
            for (Sport sport : availableSports) {
                if (sport.getId() == null) {
                    throw new ValidationException(MODEL_NAME, ValidationContextType.PERSISTENCE,
                            String.format(ErrorMessageConstants.Errors.SPORT_ID_NULL, sport.getName()));
                }

                long sportCount = entityManager.createQuery(JpqlRequest.Sport.COUNT_BY_ID, Long.class)
                        .setParameter("id", sport.getId()).getSingleResult();

                if (sportCount == 0) {
                    throw new EntityNotFoundException("Sport", sport.getId().toString());
                }
            }
        }

        SkiLiftEntity entity = skiLiftMapper.toEntityForCreate(skiLift);

        Set<SportEntity> managedSportEntities = new HashSet<>();
        for (SportEntity sportEntity : entity.getAvailableSports()) {
            SportEntity managedSport = entityManager.find(SportEntity.class, sportEntity.getId());
            if (managedSport == null) {
                throw new EntityNotFoundException("Sport", sportEntity.getId().toString());
            }
            managedSportEntities.add(managedSport);
        }
        entity.setAvailableSports(managedSportEntities);

        entityManager.persist(entity);

        return skiLiftMapper.toDomain(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkiLift> findAll() {
        List<SkiLiftEntity> entities = entityManager.createQuery(JpqlRequest.SkiLift.FIND_ALL, SkiLiftEntity.class)
                .getResultList();

        return entities.stream().map(skiLiftMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SkiLift> findById(Long id) {
        Objects.requireNonNull(id, String.format(ErrorMessageConstants.Validation.FIELD_NULL, "id"));

        SkiLiftEntity entity = entityManager.find(SkiLiftEntity.class, id);

        if (entity == null) {
            return Optional.empty();
        }

        return Optional.of(skiLiftMapper.toDomain(entity));
    }

    @Override
    @Transactional
    public SkiLift update(SkiLift skiLift) {
        Objects.requireNonNull(skiLift, ErrorMessageConstants.Errors.SKILIFT_NULL);
        Objects.requireNonNull(skiLift.getId(), ErrorMessageConstants.Errors.SKILIFT_ID_NULL);

        System.out.println("SkiLiftId " + skiLift.getId());
        List<SkiLiftEntity> entities = entityManager
                .createQuery("SELECT s FROM SkiLiftEntity s WHERE s.id = :id", SkiLiftEntity.class)
                .setParameter("id", skiLift.getId()).getResultList();

        if (entities.isEmpty()) {
            throw new EntityNotFoundException(MODEL_NAME, skiLift.getId().toString());
        }

        SkiLiftEntity existingEntity = entities.get(0);

        String skiLiftName = skiLift.getName();
        if (skiLiftName == null || skiLiftName.isEmpty()) {
            throw new ValidationException(MODEL_NAME, ValidationContextType.PERSISTENCE,
                    ErrorMessageConstants.Errors.NAME_EMPTY);
        }

        if (!existingEntity.getName().equals(skiLiftName)) {
            long count = entityManager.createQuery(JpqlRequest.SkiLift.COUNT_BY_NAME_EXCEPT_ID, Long.class)
                    .setParameter("name", skiLiftName).setParameter("id", skiLift.getId()).getSingleResult();

            if (count > 0) {
                throw new DuplicateEntityException(MODEL_NAME, ErrorMessageConstants.Fields.NAME, skiLiftName);
            }
        }

        existingEntity.setName(skiLift.getName());
        existingEntity.setType(skiLift.getType());
        existingEntity.setStatus(skiLift.getStatus());
        existingEntity.setComment(skiLift.getComment());
        existingEntity.setCommissioningDate(skiLift.getCommissioningDate());

        if (skiLift.getAvailableSports() != null) {
            Set<SportEntity> managedSportEntities = new HashSet<>();
            for (Sport sport : skiLift.getAvailableSports()) {
                SportEntity managedSport = entityManager.find(SportEntity.class, sport.getId());
                if (managedSport == null) {
                    throw new EntityNotFoundException("Sport", sport.getId().toString());
                }
                managedSportEntities.add(managedSport);
            }
            existingEntity.setAvailableSports(managedSportEntities);
        }

        entityManager.flush();

        return skiLiftMapper.toDomain(existingEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id, String.format(ErrorMessageConstants.Validation.FIELD_NULL, "id"));

        SkiLiftEntity entityToDelete = entityManager.find(SkiLiftEntity.class, id);

        if (entityToDelete == null) {
            throw new EntityNotFoundException(MODEL_NAME, id.toString());
        }

        entityToDelete.setAvailableSports(new HashSet<>());

        entityManager.remove(entityToDelete);

        entityManager.flush();
    }

    @Override
    public boolean existsById(Long id) {
        Objects.requireNonNull(id, String.format(ErrorMessageConstants.Validation.FIELD_NULL, "id"));

        Long count = entityManager.createQuery(JpqlRequest.SkiLift.COUNT_BY_ID, Long.class).setParameter("id", id)
                .getSingleResult();

        return count > 0;
    }

}
