package fr.apsprevoyance.skylift.repository;

import java.util.List;
import java.util.Optional;

import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.model.SkiLift;

public interface SkiLiftRepository {
    SkiLift create(SkiLift skiLift);

    List<SkiLift> findAll();

    Optional<SkiLift> findById(Long id);

    SkiLift update(SkiLift skiLift);

    void delete(Long id);

    boolean existsById(Long id);

    List<SkiLift> findByTypeAndStatus(SkiLiftType type, SkiLiftStatus status);
}