package fr.apsprevoyance.skylift.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.EntityNotFoundException;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.model.SkiLift;

@Repository
public class SkiLiftRepositoryInMemory implements SkiLiftRepository {
    private static final String ENTITY_NAME = "SkiLift";
    private static final String REPOSITORY_CLASS_NAME = SkiLiftRepositoryInMemory.class.getSimpleName();

    private final List<SkiLift> skiLifts = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public SkiLift create(SkiLift skiLift) {
        Objects.requireNonNull(skiLift, ErrorMessageConstants.Errors.SPORT_NULL);

        if (skiLift.getId() != null) {
            throw new ValidationException(REPOSITORY_CLASS_NAME, ValidationContextType.PERSISTENCE,
                    ErrorMessageConstants.Errors.SPORT_ID_PREDEFINED);
        }

        Long newId = idCounter.getAndIncrement();
        SkiLift newSkiLift = SkiLift.builder().id(newId).name(skiLift.getName()).type(skiLift.getType())
                .status(skiLift.getStatus()).comment(skiLift.getComment()).availableSports(skiLift.getAvailableSports())
                .commissioningDate(skiLift.getCommissioningDate()).build();

        skiLifts.add(newSkiLift);
        return newSkiLift;
    }

    @Override
    public List<SkiLift> findAll() {
        return new ArrayList<>(skiLifts);
    }

    @Override
    public Optional<SkiLift> findById(Long id) {
        Objects.requireNonNull(id, ErrorMessageConstants.Errors.ID_NULL);

        return skiLifts.stream().filter(skiLift -> id.equals(skiLift.getId())).findFirst();
    }

    @Override
    public SkiLift update(SkiLift skiLift) {
        Objects.requireNonNull(skiLift, ErrorMessageConstants.Errors.SPORT_NULL);
        Objects.requireNonNull(skiLift.getId(), ErrorMessageConstants.Errors.ID_NULL);

        int index = -1;
        for (int i = 0; i < skiLifts.size(); i++) {
            if (skiLifts.get(i).getId().equals(skiLift.getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new EntityNotFoundException(ENTITY_NAME, skiLift.getId().toString());
        }

        SkiLift updatedSkiLift = SkiLift.builder().id(skiLift.getId()).name(skiLift.getName()).type(skiLift.getType())
                .status(skiLift.getStatus()).comment(skiLift.getComment()).availableSports(skiLift.getAvailableSports())
                .commissioningDate(skiLift.getCommissioningDate()).build();

        skiLifts.set(index, updatedSkiLift);
        return updatedSkiLift;
    }

    @Override
    public void delete(Long id) {
        Objects.requireNonNull(id, ErrorMessageConstants.Errors.ID_NULL);

        boolean removed = skiLifts.removeIf(skiLift -> skiLift.getId().equals(id));

        if (!removed) {
            throw new EntityNotFoundException(ENTITY_NAME, id.toString());
        }
    }

    @Override
    public boolean existsById(Long id) {
        Objects.requireNonNull(id, ErrorMessageConstants.Errors.ID_NULL);

        return skiLifts.stream().anyMatch(skiLift -> id.equals(skiLift.getId()));
    }

    @Override
    public List<SkiLift> findByTypeAndStatus(SkiLiftType type, SkiLiftStatus status) {
        // TODO Auto-generated method stub
        return null;
    }

}