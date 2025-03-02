package fr.apsprevoyance.skylift.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.DuplicateEntityException;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.mapper.SportMapper;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.repository.entity.SportEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Primary
public class SportRepositoryJpa implements SportRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final SportMapper sportMapper;

    private static final String MODEL_NAME = Sport.class.getSimpleName();
    private static final String REPOSITORY_CLASS_NAME = SportRepositoryJpa.class.getSimpleName();

    public SportRepositoryJpa(SportMapper sportMapper) {
        this.sportMapper = sportMapper;
    }

    @Override
    @Transactional
    public Sport create(Sport sport) {
        Objects.requireNonNull(sport, ErrorMessageConstants.Errors.SPORT_NULL);

        if (sport.getId() != null) {
            throw new ValidationException(REPOSITORY_CLASS_NAME, ValidationContextType.PERSISTENCE,
                    ErrorMessageConstants.Errors.SPORT_ID_PREDEFINED);
        }

        String sportName = sport.getName();
        if (sportName == null || sportName.isEmpty()) {
            throw new ValidationException(MODEL_NAME, ValidationContextType.PERSISTENCE,
                    ErrorMessageConstants.Errors.NAME_EMPTY);
        }

        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(s) FROM SportEntity s WHERE s.name = :name",
                Long.class);
        query.setParameter("name", sportName);

        if (query.getSingleResult() > 0) {
            throw new DuplicateEntityException(MODEL_NAME, ErrorMessageConstants.Fields.NAME, sportName);
        }

        SportEntity entity = sportMapper.toEntityForCreate(sport);

        entityManager.persist(entity);
        entityManager.flush();

        return sportMapper.toDomain(entity);
    }

    @Override
    public List<Sport> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Sport> findById(Long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Sport update(Sport sport) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean existsById(Long id) {
        // TODO Auto-generated method stub
        return false;
    }

}
