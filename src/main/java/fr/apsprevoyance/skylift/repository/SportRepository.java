// SportRepository.java
package fr.apsprevoyance.skylift.repository;

import java.util.List;
import java.util.Optional;

import fr.apsprevoyance.skylift.model.Sport;

public interface SportRepository {
    Sport create(Sport sport);

    List<Sport> findAll();

    Optional<Sport> findById(Long id);

    Sport update(Sport sport);

    void delete(Long id);

    boolean existsById(Long id);
}