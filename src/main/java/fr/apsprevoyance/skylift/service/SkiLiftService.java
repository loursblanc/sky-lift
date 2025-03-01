package fr.apsprevoyance.skylift.service;

import java.util.List;

import fr.apsprevoyance.skylift.dto.SkiLiftDTO;

public interface SkiLiftService {

    SkiLiftDTO createSkiLift(SkiLiftDTO skiLiftDTO);

    List<SkiLiftDTO> findAllSkiLifts();

    SkiLiftDTO findSkiLiftById(Long id);

    SkiLiftDTO updateSkiLift(SkiLiftDTO skiLiftDTO);

    void deleteSkiLift(Long id);

    boolean skiLiftExists(Long id);
}