package fr.apsprevoyance.skylift.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.EntityNotFoundException;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.model.Sport;

@Repository
public class SportRepositoryInMemory implements SportRepository {
    private static final String ENTITY_NAME = "Sport";
    private static final String REPOSITORY_CLASS_NAME = SportRepositoryInMemory.class.getSimpleName();

    private final List<Sport> sports = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Sport create(Sport sport) {
        Objects.requireNonNull(sport, ErrorMessageConstants.Errors.SPORT_NULL);

        if (sport.getId() != null) {
            throw new ValidationException(REPOSITORY_CLASS_NAME, ValidationContextType.PERSISTENCE,
                    ErrorMessageConstants.Errors.SPORT_ID_PREDEFINED);
        }

        Long newId = idCounter.getAndIncrement();
        Sport newSport = Sport.builder().id(newId).name(sport.getName()).description(sport.getDescription())
                .active(sport.isActive()).season(sport.getSeason()).build();

        sports.add(newSport);
        return newSport;
    }

    @Override
    public List<Sport> findAll() {
        return new ArrayList<>(sports);
    }

    @Override
    public Optional<Sport> findById(Long id) {
        Objects.requireNonNull(id, ErrorMessageConstants.Errors.ID_NULL);

        return sports.stream().filter(sport -> id.equals(sport.getId())).findFirst();
    }

    @Override
    public Sport update(Sport sport) {
        Objects.requireNonNull(sport, ErrorMessageConstants.Errors.SPORT_NULL);
        Objects.requireNonNull(sport.getId(), ErrorMessageConstants.Errors.ID_NULL);

        int index = -1;
        for (int i = 0; i < sports.size(); i++) {
            if (sports.get(i).getId().equals(sport.getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new EntityNotFoundException(ENTITY_NAME, sport.getId().toString());
        }

        Sport updatedSport = Sport.builder().id(sport.getId()).name(sport.getName()).description(sport.getDescription())
                .active(sport.isActive()).season(sport.getSeason()).build();

        sports.set(index, updatedSport);
        return updatedSport;
    }

    @Override
    public void delete(Long id) {
        Objects.requireNonNull(id, ErrorMessageConstants.Errors.ID_NULL);

        boolean removed = sports.removeIf(sport -> sport.getId().equals(id));

        if (!removed) {
            throw new EntityNotFoundException(ENTITY_NAME, id.toString());
        }
    }

    @Override
    public boolean existsById(Long id) {
        Objects.requireNonNull(id, ErrorMessageConstants.Errors.ID_NULL);

        return sports.stream().anyMatch(sport -> id.equals(sport.getId()));
    }
}