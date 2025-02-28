package fr.apsprevoyance.skylift.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.model.Sport;

@Repository
public class SportRepositoryInMemory implements SportRepository {
    private final List<Sport> sports = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Sport create(Sport sport) {

        Objects.requireNonNull(sport, ErrorMessageConstants.Errors.SPORT_NULL);

        if (sport.getId() != null) {
            throw new ValidationException(SportRepositoryInMemory.class.getSimpleName(),
                    ValidationContextType.PERSISTENCE, ErrorMessageConstants.Errors.SPORT_ID_PREDEFINED);
        }

        Long newId = idCounter.getAndIncrement();
        Sport newSport = Sport.builder().id(newId).name(sport.getName()).description(sport.getDescription())
                .active(sport.isActive()).season(sport.getSeason()).build();

        sports.add(newSport);
        return newSport;
    }

    // @Override
    // public List<Sport> findAll() {
    // return new ArrayList<>(sports);
    // }

}