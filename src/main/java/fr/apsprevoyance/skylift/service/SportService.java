// SportService.java
package fr.apsprevoyance.skylift.service;

import java.util.List;

import fr.apsprevoyance.skylift.dto.SportDTO;

public interface SportService {
    SportDTO createSport(SportDTO sportDTO);

    List<SportDTO> findAllSports();

    SportDTO findSportById(Long id);

    SportDTO updateSport(SportDTO sportDTO);

    void deleteSport(Long id);

    boolean sportExists(Long id);

    boolean areSportsValid(List<String> sportNames);
}