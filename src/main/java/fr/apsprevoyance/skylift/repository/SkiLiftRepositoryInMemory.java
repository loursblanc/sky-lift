package fr.apsprevoyance.skylift.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.ValidationException;
import fr.apsprevoyance.skylift.model.SkiLift;

@Repository
public class SkiLiftRepositoryInMemory implements SkiLiftRepository {
    private final List<SkiLift> skiLifts = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public SkiLift create(SkiLift skiLift) {
        Objects.requireNonNull(skiLift, ErrorMessageConstants.Errors.SPORT_NULL);

        if (skiLift.getId() != null) {
            throw new ValidationException(SkiLiftRepositoryInMemory.class.getSimpleName(),
                    ValidationContextType.PERSISTENCE, ErrorMessageConstants.Errors.SPORT_ID_PREDEFINED);
        }

        Long newId = idCounter.getAndIncrement();
        SkiLift newSkiLift = SkiLift.builder().id(newId).name(skiLift.getName()).type(skiLift.getType())
                .status(skiLift.getStatus()).comment(skiLift.getComment()).availableSports(skiLift.getAvailableSports())
                .commissioningDate(skiLift.getCommissioningDate()).build();

        skiLifts.add(newSkiLift);
        return newSkiLift;
    }
}